package server.dao.interfaces;

import shared.entities.Usuario;

import java.util.List;

public interface UsuarioDao {

    void create(Usuario usuario);
    Usuario read(String email);
    void update(Usuario usuario);
    void delete(String email);

    List<Usuario> readAll();
}
