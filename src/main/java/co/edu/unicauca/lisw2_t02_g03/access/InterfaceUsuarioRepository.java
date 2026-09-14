package co.edu.unicauca.lisw2_t02_g03.access;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;

import java.util.List;

public interface InterfaceUsuarioRepository {
    boolean save(Usuario usuario);
    List<Usuario> list();
    Usuario findByLogin(String login);
    boolean updateEstado(EstadoUsuario estado, String login);
}
