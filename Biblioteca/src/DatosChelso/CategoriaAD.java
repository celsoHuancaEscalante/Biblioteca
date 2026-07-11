
package DatosChelso;

import ClaseBase.Categoria;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class CategoriaAD {
    
    
    public List<Categoria> obtenerTodas() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT idCategoria, nombre FROM categoria";
        
        try (Connection conn = ConnectMySQL.conn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Categoria cat = new Categoria(
                    rs.getInt("idCategoria"),
                    rs.getString("nombre")  
                );
                categorias.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return categorias;
    }
    
    
    public Categoria obtenerPorNombre(String nombre) {
        String sql = "SELECT idCategoria, nombre FROM categoria WHERE nombre = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Categoria(
                    rs.getInt("idCategoria"),
                    rs.getString("nombre") 
                );
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return null;
    }
    
    
    public Categoria obtenerPorId(int idCategoria) {
        String sql = "SELECT idCategoria, nombre FROM categoria WHERE idCategoria = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idCategoria);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Categoria(
                    rs.getInt("idCategoria"),
                    rs.getString("nombre")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return null;
    }

    
}
