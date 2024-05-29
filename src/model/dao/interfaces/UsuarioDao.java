package model.dao.interfaces;

import model.entities.Usuario;

public interface UsuarioDao {

    void create(Usuario usuario);
    Usuario read(String email);
    void update(Usuario usuario);
    void delete(String email);

}
