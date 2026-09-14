package co.edu.unicauca.lisw2_t02_g03.access;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Statement;


public class UsuarioRepository implements InterfaceUsuarioRepository {
    private DataBaseManager databaseManager;

    public UsuarioRepository(DataBaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean save(Usuario usuario) {
        // Implementación para guardar el usuario en la base de datos
        try {
            //Validar usuario
            if (usuario == null || usuario.getLogin().isBlank()) {
                return false;
            }

            String sql = "INSERT INTO Usuario ( Login, NombreCompleto, Rol, Estado, Contrasena ) "
                    + "VALUES ( ?, ?, ?, ?, ? )";

            databaseManager.connect();
            Connection conn = databaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario.getLogin());
            pstmt.setString(2, usuario.getNombreCompleto());
            pstmt.setString(3, usuario.getRol().name());
            pstmt.setString(4, usuario.getEstado().name());
            pstmt.setString(5, usuario.getContrasena());
            pstmt.executeUpdate();
            //this.disconnect();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
             return false;
        } finally {
            databaseManager.disconnect();
        }
    }

    @Override
    public List<Usuario> list() {
        // Implementación para listar todos los usuarios desde la base de datos
        List<Usuario> usuarios = new ArrayList<>();
        try {

            String sql = "SELECT Login, NombreCompleto, Rol, Estado, Contrasena FROM Usuario";
            databaseManager.connect();
            Connection conn = databaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Usuario newUsuario = new Usuario();
                newUsuario.setLogin(rs.getString("Login"));
                newUsuario.setNombreCompleto(rs.getString("NombreCompleto"));
                newUsuario.setRol(Rol.valueOf(rs.getString("Rol")));
                newUsuario.setEstado(EstadoUsuario.valueOf(rs.getString("Estado")));
                newUsuario.setContrasena(rs.getString("Contrasena"));
                usuarios.add(newUsuario);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            databaseManager.disconnect();
        }
        return usuarios;
    }

    @Override
    public Usuario findByLogin(String login) {
        // Implementación para buscar un usuario por su login en la base de datos
        try {
            String sql = "SELECT Login, NombreCompleto, Rol, Estado, Contrasena FROM Usuario WHERE Login = ?";
            databaseManager.connect();
            Connection conn = databaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setLogin(rs.getString("Login"));
                usuario.setNombreCompleto(rs.getString("NombreCompleto"));
                usuario.setRol(Rol.valueOf(rs.getString("Rol")));
                usuario.setEstado(EstadoUsuario.valueOf(rs.getString("Estado")));
                usuario.setContrasena(rs.getString("Contrasena"));
                return usuario;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally { 
            databaseManager.disconnect();
        }
        return null; // Retorna null si no se encuentra el usuario  
    }

    @Override
    public boolean updateEstado(EstadoUsuario estado, String login) {
        // Implementación para actualizar un usuario en la base de datos
        try {
            String sql = "UPDATE Usuario SET Estado = ? WHERE Login = ?";
            databaseManager.connect();
            Connection conn = databaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, estado.name());
            pstmt.setString(2, login);
            int rowsAffected = pstmt.executeUpdate();
            //this.disconnect();
            return rowsAffected > 0; // Retorna true si se actualizó al menos una fila
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioRepository.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            databaseManager.disconnect();
        }
    }
    
}
