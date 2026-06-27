
package Datos;

import ClaseBase.Ejemplar;
import ClaseBase.Libro;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class EjemplarAD {
    
    public List<Ejemplar> obtenerPorLibro(int idLibro) {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT idEjemplar, estado FROM ejemplar WHERE idLibro = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLibro);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Ejemplar ejemplar = new Ejemplar(
                    rs.getInt("idEjemplar"),
                    null,  // No traemos el libro completo aquí
                    rs.getString("estado")
                );
                ejemplares.add(ejemplar);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return ejemplares;
    }
    
    /**
     * Obtiene todos los ejemplares
     */
    public List<Ejemplar> obtenerTodos() {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT idEjemplar, idLibro, estado FROM ejemplar";
        
        try (Connection conn = ConnectMySQL.conn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Ejemplar ejemplar = new Ejemplar(
                    rs.getInt("idEjemplar"),
                    null,
                    rs.getString("estado")
                );
                ejemplares.add(ejemplar);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return ejemplares;
    }
    
    /**
     * Inserta ejemplares cuando se registra un libro
     * Crea 'cantidad' ejemplares nuevos en estado 'disponible'
     */
    public boolean insertarEjemplares(int idLibro, int cantidad) {
        String sql = "INSERT INTO ejemplar (idLibro, estado) VALUES (?, 'disponible')";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (int i = 0; i < cantidad; i++) {
                pstmt.setInt(1, idLibro);
                pstmt.addBatch();  // Agrega a un lote
            }
            
            pstmt.executeBatch();  // Ejecuta todos de una vez
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza el estado de un ejemplar
     */
    public boolean actualizarEstado(int idEjemplar, String nuevoEstado) {
        String sql = "UPDATE ejemplar SET estado = ? WHERE idEjemplar = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, idEjemplar);
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene la cantidad de ejemplares disponibles de un libro
     */
    public int contarDisponibles(int idLibro) {
        String sql = "SELECT COUNT(*) as cantidad FROM ejemplar WHERE idLibro = ? AND estado = 'disponible'";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLibro);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Elimina todos los ejemplares de un libro
     */
    public boolean eliminarPorLibro(int idLibro) {
        String sql = "DELETE FROM ejemplar WHERE idLibro = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLibro);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
}
