package model.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//*******************************************************************//
//OBS: tempoPreparo FOI AMASSADO NO LIXO ; INSERI O ID COMO ATRIBUTO
//*******************************************************************//
public class Receita {
    private String titulo;
    private String descricao;
    private ArrayList<Ingrediente> ingredientes;
    private String modoPreparo;
    private Double tempoPreparo;
    private int id;

    public Receita(){
        this.ingredientes = new ArrayList<>();
    }

    public Receita(String titulo, String descricao, ArrayList<Ingrediente> ingredientes, String modoPreparo, Double tempoPreparo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.tempoPreparo = tempoPreparo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public Double getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(Double tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Receita{" +
                "titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ingredientes=" + ingredientes +
                ", modoPreparo='" + modoPreparo + '\'' +
                ", tempoPreparo=" + tempoPreparo +
                '}';
    }
    public void incluir(Connection conn){
        String sqlInsert = "INSERT INTO RECEITA(id, titulo, descricao, modo_preparo) VALUES(?,?,?,?)";

        PreparedStatement stm = null;
        try{
            stm = conn.prepareStatement(sqlInsert);
            stm.setInt(1,getId());
            stm.setString(1,getTitulo());
            stm.setString(1,getDescricao());
            stm.setString(1,getModoPreparo());

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
        String sqlUpdate = "UPDATE RECEITA SET Titulo = ?, Descricao = ?, modo_preparo = ? WHERE id = ?";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlUpdate);
            stm.setString(1,getTitulo());
            stm.setString(2,getDescricao());
            stm.setString(3,getModoPreparo());
            stm.setInt(4,getId());
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
        String sqlExcluir = "DELETE FROM RECEITA WHERE id = ?";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setInt(1,getId());
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
        String sqlCarregar = "SELECT titulo, descricao, modo_preparo FROM RECEITA WHERE id = ?";
        ResultSet rs;
        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlCarregar);
            stm.setInt(1,getId());
            rs = stm.executeQuery();
            if(rs.next()){
                this.setTitulo(rs.getString(1));
                this.setDescricao(rs.getString(2));
                this.setModoPreparo(rs.getString(3));
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
