package model.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Ingrediente {

    private String nome;
    private int categoria;
    private Date validade;
    private int quantidade;

    public Ingrediente(){

    }

    public Ingrediente(String nome, int categoria, Date validade, int quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.validade = validade;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "nome='" + nome + '\'' +
                ", categoria=" + categoria +
                ", validade=" + validade +
                ", quantidade=" + quantidade +
                '}';
    }
    public void incluir(Connection conn){
        String sqlInsert = "INSERT INTO ingrediente(nome, categoria) VALUES(?,?)";

        PreparedStatement stm = null;
        try{
            stm = conn.prepareStatement(sqlInsert);
            stm.setString(1,getNome());
            stm.setInt(2,getCategoria());
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            try{
                conn.rollback();
            }
            catch(SQLException e1){
                e1.getStackTrace();
            }
        }
        finally{
            if(stm!=null){
                try{
                    stm.close();
                } catch (SQLException e1) {
                    e1.getStackTrace();
                }
            }
        }
    }
    public void alterar(Connection conn) {
        String sqlUpdate = "UPDATE INGREDIENTE SET Cateogira = ? WHERE Nome = ?";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlUpdate);
            stm.setInt(1,getCategoria());
            stm.setString(2,getNome());
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            try{
                conn.rollback();
            }
            catch(SQLException e1){
                e1.getStackTrace();
            }
        }
        finally{
            if(stm!=null){
                try{
                    stm.close();
                } catch (SQLException e1) {
                    e1.getStackTrace();
                }
            }
        }
    }
    public void excluir(Connection conn) {
        String sqlExcluir = "DELETE FROM INGREDIENTE WHERE Nome = ?";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1,getNome());
            stm.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            try{
                conn.rollback();
            }
            catch(SQLException e1){
                e1.getStackTrace();
            }
        }
        finally{
            if(stm!=null){
                try{
                    stm.close();
                } catch (SQLException e1) {
                    e1.getStackTrace();
                }
            }
        }

    }
    public void carregar(Connection conn) {
        String sqlCarregar = "SELECT Categoria FROM Ingrediente WHERE Nome = ?";
        ResultSet rs;
        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlCarregar);
            stm.setString(1,getNome());
            rs = stm.executeQuery();
            if(rs.next()){
                this.setCategoria(rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try{
                conn.rollback();
            }
            catch(SQLException e1){
                e1.getStackTrace();
            }
        }
        finally{
            if(stm!=null){
                try{
                    stm.close();
                } catch (SQLException e1) {
                    e1.getStackTrace();
                }
            }
        }

    }
}
