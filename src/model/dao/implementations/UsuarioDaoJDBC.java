package model.dao.implementations;

import db.DB;
import db.DBException;
import model.dao.interfaces.UsuarioDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoJDBC implements UsuarioDao {

    private Connection conn;

    public UsuarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Usuario usuario) {
        String sqlInsert = "INSERT INTO usuario(nome, email, senha_hash) VALUES(?,?,?)";
        PreparedStatement stm = null;
        try {


            stm = conn.prepareStatement(sqlInsert);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getEmail());
            stm.setString(3, usuario.getSenha());
            stm.execute();

            for (Ingrediente ingrediente : usuario.getDespensa()) {
                String sqlInsertDespensa = "INSERT INTO despensa(email, nome_ingrediente, validade, quantidade) VALUES (?,?,?,?)";
                try (PreparedStatement stmtDespensa = conn.prepareStatement(sqlInsertDespensa)) {
                    stmtDespensa.setString(1, usuario.getEmail());
                    stmtDespensa.setString(2, ingrediente.getNome());
                    stmtDespensa.setString(3, ingrediente.getValidade().toString());
                    stmtDespensa.setInt(4, ingrediente.getQuantidade());
                    stmtDespensa.executeUpdate();
                }
            }

            for (Receita receita : usuario.getReceitasPublicadas()) {
                String sqlInsertReceita = "INSERT INTO receita(titulo, descricao, modo_preparo,email_usuario) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtReceita = conn.prepareStatement(sqlInsertReceita, Statement.RETURN_GENERATED_KEYS)) {
                    stmtReceita.setString(1, receita.getTitulo());
                    stmtReceita.setString(2, receita.getDescricao());
                    stmtReceita.setString(3, receita.getModoPreparo());
                    stmtReceita.setString(4,receita.getEmailAutor());
                    stmtReceita.executeUpdate();

                    ResultSet rs = stmtReceita.getGeneratedKeys();
                    if (rs.next()) {
                        receita.setId(rs.getInt(1));
                    }

                    for (Ingrediente ingrediente : receita.getIngredientes()) {
                        String sqlInsertReceitaIngrediente = "INSERT INTO receita_ingrediente(id_receita, nome_ingrediente, quantidade) VALUES (?, ?, ?)";
                        try (PreparedStatement stmtReceitaIngrediente = conn.prepareStatement(sqlInsertReceitaIngrediente)) {
                            stmtReceitaIngrediente.setInt(1, receita.getId());
                            stmtReceitaIngrediente.setString(2, ingrediente.getNome());
                            stmtReceitaIngrediente.setInt(3, ingrediente.getQuantidade());
                            stmtReceitaIngrediente.executeUpdate();
                        }
                    }
                }
            }


        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }
    }

    @Override
    public Usuario read(String email) {
        String sqlCarregar = "SELECT nome, email, senha_hash FROM usuario WHERE email = ?";
        ResultSet rs = null;
        PreparedStatement stm = null;

        try {
            stm = conn.prepareStatement(sqlCarregar);
            stm.setString(1, email);
            rs = stm.executeQuery();

            if (rs.next()) {
                Usuario usuario = instantiateUsuario(rs);
                usuario.setDespensa(readDespensa(rs.getString("email")));
                usuario.setReceitasPublicadas(readReceitas(rs.getString("email")));
                return usuario;
            }
            return null;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stm);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Usuario usuario) {
        String sqlUpdateUsuario = "UPDATE usuario SET nome = ?, senha_hash = ? WHERE email = ?";
        String sqlDeleteDespensa = "DELETE FROM despensa WHERE email = ?";
        String sqlDeleteReceitas = "DELETE FROM receita WHERE id IN (SELECT id_receita FROM receita_ingrediente)";
        PreparedStatement stm = null;

        try {


            stm = conn.prepareStatement(sqlUpdateUsuario);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getSenha());
            stm.setString(3, usuario.getEmail());
            stm.executeUpdate();

            stm = conn.prepareStatement(sqlDeleteDespensa);
            stm.setString(1, usuario.getEmail());
            stm.executeUpdate();

            for (Ingrediente ingrediente : usuario.getDespensa()) {
                String sqlInsertDespensa = "INSERT INTO despensa(email, nome_ingrediente, validade, quantidade) VALUES (?,?,?,?)";
                try (PreparedStatement stmtDespensa = conn.prepareStatement(sqlInsertDespensa)) {
                    stmtDespensa.setString(1, usuario.getEmail());
                    stmtDespensa.setString(2, ingrediente.getNome());
                    stmtDespensa.setString(3, ingrediente.getValidade().toString());
                    stmtDespensa.setInt(4, ingrediente.getQuantidade());
                    stmtDespensa.executeUpdate();
                }
            }

            stm = conn.prepareStatement(sqlDeleteReceitas);
            stm.executeUpdate();

            for (Receita receita : usuario.getReceitasPublicadas()) {
                String sqlInsertReceita = "INSERT INTO receita(titulo, descricao, modo_preparo,email_usuario) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtReceita = conn.prepareStatement(sqlInsertReceita, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    stmtReceita.setString(1, receita.getTitulo());
                    stmtReceita.setString(2, receita.getDescricao());
                    stmtReceita.setString(3, receita.getModoPreparo());
                    stmtReceita.executeUpdate();

                    ResultSet rs = stmtReceita.getGeneratedKeys();
                    if (rs.next()) {
                        receita.setId(rs.getInt(1));
                    }

                    for (Ingrediente ingrediente : receita.getIngredientes()) {
                        String sqlInsertReceitaIngrediente = "INSERT INTO receita_ingrediente(id_receita, nome_ingrediente, quantidade) VALUES (?, ?, ?)";
                        try (PreparedStatement stmtReceitaIngrediente = conn.prepareStatement(sqlInsertReceitaIngrediente)) {
                            stmtReceitaIngrediente.setInt(1, receita.getId());
                            stmtReceitaIngrediente.setString(2, ingrediente.getNome());
                            stmtReceitaIngrediente.setInt(3, ingrediente.getQuantidade());
                            stmtReceitaIngrediente.executeUpdate();
                        }
                    }
                }
            }


        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }
    }

    @Override
    public void delete(String email) {
        String sqlDeleteUsuario = "DELETE FROM usuario WHERE email = ?";
        String sqlDeleteDespensa = "DELETE FROM despensa WHERE email = ?";
        String sqlDeleteReceitas = "DELETE FROM receita WHERE id IN (SELECT id_receita FROM receita_ingrediente WHERE email = ?)";
        PreparedStatement stm = null;

        try {


            stm = conn.prepareStatement(sqlDeleteDespensa);
            stm.setString(1, email);
            stm.executeUpdate();

            stm = conn.prepareStatement(sqlDeleteReceitas);
            stm.setString(1, email);
            stm.executeUpdate();

            stm = conn.prepareStatement(sqlDeleteUsuario);
            stm.setString(1, email);
            stm.executeUpdate();


        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }
    }

    @Override
    public List<Usuario> readAll() {
        String sqlReadAllUsuarios = "SELECT * FROM usuario ORDER BY nome";
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(sqlReadAllUsuarios);
            rs = st.executeQuery();
            List<Usuario> usuarioList = new ArrayList<>();

            while (rs.next()) {
                Usuario usuario = instantiateUsuario(rs);
                usuarioList.add(usuario);
            }

            return usuarioList;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Usuario instantiateUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha_hash"));
        usuario.setDespensa(readDespensa(rs.getString("email")));
        usuario.setReceitasPublicadas(readReceitas(rs.getString("email")));
        return usuario;
    }

    private ArrayList<Ingrediente> readDespensa(String email) throws SQLException {
        String sqlCarregarDespensa = "SELECT nome_ingrediente, validade, quantidade FROM despensa WHERE email = ?";
        PreparedStatement stmDespensa = conn.prepareStatement(sqlCarregarDespensa);
        stmDespensa.setString(1, email);
        ResultSet rsDespensa = stmDespensa.executeQuery();
        ArrayList<Ingrediente> despensa = new ArrayList<>();

        try {
            while (rsDespensa.next()) {
                Ingrediente ingrediente = new Ingrediente();
                ingrediente.setNome(rsDespensa.getString("nome_ingrediente"));
                ingrediente.setValidade(Date.valueOf(rsDespensa.getString("validade")));
                ingrediente.setQuantidade(rsDespensa.getInt("quantidade"));
                despensa.add(ingrediente);
            }
            return despensa;
        } finally {
            DB.closeStatement(stmDespensa);
            DB.closeResultSet(rsDespensa);
        }
    }

    private ArrayList<Receita> readReceitas(String email) throws SQLException {
        String sqlCarregarReceitas = "SELECT receita.id, receita.titulo, receita.descricao, receita.modo_preparo " +
                "FROM receita " +
                "WHERE receita.email_usuario = ?";
        PreparedStatement stmReceitas = conn.prepareStatement(sqlCarregarReceitas);
        stmReceitas.setString(1, email);
        ResultSet rsReceitas = stmReceitas.executeQuery();
        ArrayList<Receita> receitasPublicadas = new ArrayList<>();

        try {
            while (rsReceitas.next()) {
                Receita receita = new Receita();
                receita.setId(rsReceitas.getInt("id"));
                receita.setTitulo(rsReceitas.getString("titulo"));
                receita.setDescricao(rsReceitas.getString("descricao"));
                receita.setModoPreparo(rsReceitas.getString("modo_preparo"));

                String sqlCarregarIngredientesReceita = "SELECT nome_ingrediente, quantidade FROM receita_ingrediente WHERE id_receita = ?";
                try (PreparedStatement stmIngredientes = conn.prepareStatement(sqlCarregarIngredientesReceita)) {
                    stmIngredientes.setInt(1, receita.getId());
                    ResultSet rsIngredientes = stmIngredientes.executeQuery();
                    ArrayList<Ingrediente> ingredientes = new ArrayList<>();
                    while (rsIngredientes.next()) {
                        Ingrediente ingrediente = new Ingrediente();
                        ingrediente.setNome(rsIngredientes.getString("nome_ingrediente"));
                        ingrediente.setQuantidade(rsIngredientes.getInt("quantidade"));
                        ingredientes.add(ingrediente);
                    }
                    receita.setIngredientes(ingredientes);
                }
                receitasPublicadas.add(receita);
            }
            return receitasPublicadas;
        } finally {
            DB.closeStatement(stmReceitas);
            DB.closeResultSet(rsReceitas);
        }
    }

}



