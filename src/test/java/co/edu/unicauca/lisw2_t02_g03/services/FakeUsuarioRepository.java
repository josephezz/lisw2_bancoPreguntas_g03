package co.edu.unicauca.lisw2_t02_g03.services;

import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import java.util.ArrayList;
import java.util.List;

class FakeUsuarioRepository implements InterfaceUsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();

    @Override
    public boolean save(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        if (findByLogin(usuario.getLogin()) != null) {
            return false;
        }

        usuarios.add(usuario);
        return true;
    }

    @Override
    public List<Usuario> list() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public Usuario findByLogin(String login) {

        for (Usuario usuario : usuarios) {

            if (usuario.getLogin().equals(login)) {
                return usuario;
            }
        }

        return null;
    }

    @Override
    public boolean updateEstado(
            EstadoUsuario estado,
            String login) {

        Usuario usuario = findByLogin(login);

        if (usuario == null) {
            return false;
        }

        usuario.setEstado(estado);
        return true;
    }
}