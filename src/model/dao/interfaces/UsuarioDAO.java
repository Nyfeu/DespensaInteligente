package model.dao.interfaces;

import model.entities.Usuario;

public interface UsuarioDAO {

    void create(Usuario usuario);
    Usuario read(Integer user_id);
    void update(Usuario usuario);
    void delete(Integer user_id);

}
