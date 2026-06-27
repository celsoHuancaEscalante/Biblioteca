
package Datos;

import ClaseBase.Autor;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class AutorAD {
    
    public List<Autor> obtenerTodos() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT idAutor, nombreApellido FROM autor";
        
        try (Connection conn = ConnectMySQL.conn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("Nombre Apellido")
                );
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return autores;
    }
    
    /**
     * Busca autores por nombre o apellido
     */
    public List<Autor> buscarPorNombre(String nombre) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT idAutor, nombreApellido FROM autor WHERE nombreApellido LIKE ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nombre + "%");
            pstmt.setString(2, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("nombreApellido")
                );
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return autores;
    }
    
    /**
     * Inserta un nuevo autor
     */
    public boolean insertar(Autor autor) {
        String sql = "INSERT INTO autor (primerNombre, segundoNombre, primerApellido, " +
                     "segundoApellido) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, autor.getNombreApellido());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un autor existente
     */
    public boolean actualizar(Autor autor) {
        String sql = "UPDATE autor SET primerNombre = ?, segundoNombre = ?, " +
                     "primerApellido = ?, segundoApellido = ? WHERE idAutor = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, autor.getNombreApellido());
            pstmt.setInt(2, autor.getIdAutor());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina un autor
     */
    public boolean eliminar(int idAutor) {
        String sql = "DELETE FROM autor WHERE idAutor = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idAutor);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
    

