
package DatosChelso;

import ClaseBase.Ejemplar;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class EjemplarAD {
    

    public boolean insertarEjemplares(int idLibro, int cantidad) {
        String sql = "INSERT INTO ejemplares (idLibro, estado) VALUES (?, 'disponible')";
        
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
    

    public int contarDisponibles(int idLibro) {
        String sql = "SELECT COUNT(*) as cantidad FROM ejemplares WHERE idLibro = ? AND estado = 'disponible'";
        
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
    

    public boolean eliminarPorLibro(int idLibro) {
        String sql = "DELETE FROM ejemplares WHERE idLibro = ?";
        
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
