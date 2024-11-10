package server.dao.implementations;


import client.model.builder.IngredienteBuilder;
import client.model.builder.ReceitaBuilder;
import server.dao.interfaces.UsuarioDao;
import server.db.DB;
import server.db.DBException;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import shared.entities.Usuario;
import shared.util.DateParser;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDaoJDBC implements UsuarioDao {

    @Override
    public void create(Usuario usuario) {

        Connection conn = DB.getConnection();
        String sqlInsert = "INSERT INTO usuario(nome, email, senha_hash) VALUES(?,?,?)";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlInsert);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getEmail());
            stm.setString(3, usuario.getSenha());
            stm.execute();


        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    @Override
    public Usuario read(String email) {

        Connection conn = DB.getConnection();
        String sqlSelect = "SELECT nome, email, senha_hash FROM usuario WHERE email = ?";
        PreparedStatement stm = null;
        ResultSet rs = null;

        try{

            stm = conn.prepareStatement(sqlSelect);
            stm.setString(1, email);
            rs = stm.executeQuery();

            Usuario usuario = null;

            if (rs.next()) {
                usuario = instantiateUsuario(rs);
                usuario.setDespensa(readDespensa(email));
                usuario.setReceitasPublicadas(readReceitasPublicadas(email));
            }

            return usuario;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }

    }

    private ArrayList<Receita> readReceitasPublicadas(String email) {

        Connection conn = DB.getConnection();
        String sqlSelect = "SELECT id, titulo, descricao, modo_preparo, email_usuario FROM receita WHERE email_usuario = ?";

        PreparedStatement stm = null;
        ResultSet rs = null;

        try{

            stm = conn.prepareStatement(sqlSelect);
            stm.setString(1, email);
            rs = stm.executeQuery();

            ArrayList<Receita> receitasPublicadas = new ArrayList<>();

            while (rs.next()) {

                ReceitaBuilder receitaBuilder = new ReceitaBuilder(rs.getInt("id"));

                receitaBuilder.titulo(rs.getString("titulo"))
                        .descricao(rs.getString("descricao"))
                        .instrucoes(rs.getString("modo_preparo"))
                        .emailAutor(rs.getString("email_usuario"));

                receitasPublicadas.add(receitaBuilder.build());

            }

            return receitasPublicadas;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }

    }

    private ArrayList<Ingrediente> readDespensa(String email) {

        Connection conn = DB.getConnection();
        String sqlSelect = "SELECT email, nome_ingrediente, validade, quantidade FROM despensa WHERE email = ?";

        PreparedStatement stm = null;
        ResultSet rs = null;

        try{

            stm = conn.prepareStatement(sqlSelect);
            stm.setString(1, email);
            rs = stm.executeQuery();

            ArrayList<Ingrediente> despensa = new ArrayList<>();

            while (rs.next()) {

                IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

                ingredienteBuilder.nome(rs.getString("nome_ingrediente"))
                        .validade(rs.getDate("validade"))
                        .quantidade(rs.getInt("quantidade"));

                despensa.add(ingredienteBuilder.build());

            }

            return despensa;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }

    }

    private Usuario instantiateUsuario(ResultSet rs) throws SQLException {

        Usuario usuario = new Usuario();
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha_hash"));
        return usuario;

    }

    @Override
    public void update(Usuario usuario) {

        Connection conn = DB.getConnection();
        String sqlUpdate = "UPDATE usuario SET nome = ?, senha_hash = ? WHERE email = ?";
        PreparedStatement stm = null;
        String email = usuario.getEmail();

        try{

            stm = conn.prepareStatement(sqlUpdate);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getSenha());
            stm.setString(3, email);
            stm.execute();

            deleteDespensa(email);
            createDespensa(usuario);
            deleteReceitasPublicadas(email);
            createReceitasPublicadas(usuario);

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    private void createReceitasPublicadas(Usuario usuario) {

        Connection conn = DB.getConnection();
        String sqlInsert = "INSERT INTO receita(id, titulo, descricao, modo_preparo, email_usuario) VALUES(?,?,?,?,?)";
        PreparedStatement stm = null;
        String email = usuario.getEmail();

        try{

            for (Receita receita : usuario.getReceitasPublicadas()) {

                stm = conn.prepareStatement(sqlInsert);
                stm.setInt(1, receita.getId());
                stm.setString(2, receita.getTitulo());
                stm.setString(3, receita.getDescricao());
                stm.setString(4, receita.getModoPreparo());
                stm.setString(5, email);
                stm.execute();

            }

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    private void createDespensa(Usuario usuario) {

        Connection conn = DB.getConnection();
        String sqlInsert = "INSERT INTO despensa(email, nome_ingrediente, validade, quantidade) VALUES(?,?,?,?)";
        PreparedStatement stm = null;
        String email = usuario.getEmail();

        try{

            for (Ingrediente ingrediente : usuario.getDespensa()) {

                stm = conn.prepareStatement(sqlInsert);
                stm.setString(1, email);
                stm.setString(2, ingrediente.getNome());
                stm.setString(3, DateParser.parseDateDB(ingrediente.getValidade()));
                stm.setInt(4, ingrediente.getQuantidade());
                stm.execute();

            }


        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }


    }

    @Override
    public void delete(String email) {

        Connection conn = DB.getConnection();
        String sqlExcluir = "DELETE FROM usuario WHERE email = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1, email);
            stm.execute();

            deleteDespensa(email);
            deleteReceitasPublicadas(email);

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    private void deleteReceitasPublicadas(String email) {

        Connection conn = DB.getConnection();
        String sqlExcluir = "DELETE FROM receita WHERE email_usuario = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1, email);
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    private void deleteDespensa(String email) {

        Connection conn = DB.getConnection();
        String sqlExcluir = "DELETE FROM despensa WHERE email = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1, email);
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    @Override
    public List<Usuario> readAll() {

        Connection conn = DB.getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = conn.prepareStatement("SELECT email FROM usuario");

            rs = st.executeQuery();
            List<Usuario> usuarioList = new ArrayList<>();

            while(rs.next()) {
                usuarioList.add(read(rs.getString("email")));
            }

            return usuarioList;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }

    }
}



