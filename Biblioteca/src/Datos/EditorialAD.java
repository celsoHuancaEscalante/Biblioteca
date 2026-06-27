
package Datos;

import ClaseBase.Editorial;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class EditorialAD {
    

public List<Editorial> obtenerTodos() {
        List<Editorial> editoriales = new ArrayList<>();
        String sql = "SELECT idEditorial, nombre FROM editorial";
        
        try (Connection conn = ConnectMySQL.conn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Editorial editorial = new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("nombre")
                );
                editoriales.add(editorial);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return editoriales;
    }
    
    /**
     * Busca editoriales por nombre
     */
    public List<Editorial> buscarPorNombre(String nombre) {
        List<Editorial> editoriales = new ArrayList<>();
        String sql = "SELECT idEditorial, nombre FROM editorial WHERE nombre LIKE ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Editorial editorial = new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("nombre")
                );
                editoriales.add(editorial);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return editoriales;
    }
    
    /**
     * Inserta una nueva editorial
     */
    public boolean insertar(Editorial editorial) {
        String sql = "INSERT INTO editorial (nombre) VALUES (?)";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, editorial.getNombre());
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza una editorial existente
     */
    public boolean actualizar(Editorial editorial) {
        String sql = "UPDATE editorial SET nombre = ? WHERE idEditorial = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, editorial.getNombre());
            pstmt.setInt(2, editorial.getIdEditorial());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina una editorial
     */
    public boolean eliminar(int idEditorial) {
        String sql = "DELETE FROM editorial WHERE idEditorial = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idEditorial);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

}
