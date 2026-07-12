
package DatosChelso;

import ClaseBase.Genero;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class GeneroAD {
    
    public List<Genero> obtenerTodos(){
        List<Genero> generos=new ArrayList<>();
        String sql= "SELECT idGenero, nombre FROM generos";
        
        try (Connection conn= ConnectMySQL.conn();
             Statement stmt= conn.createStatement();
             ResultSet rs= stmt.executeQuery(sql)){
            
            while(rs.next()){
                Genero genero=new Genero(rs.getInt("idGenero"),rs.getString("nombre"));
                generos.add(genero);
            }
            
        } catch (SQLException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return generos;
    }
    public List<Genero> buscarPorNombre(String nombre) {
        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT idGenero, nombre FROM generos WHERE nombre LIKE ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nombre + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Genero genero = new Genero(
                    rs.getInt("idGenero"),
                    rs.getString("nombre")
                );
                generos.add(genero);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return generos;
    }
    
    public boolean insertar(Genero genero) {
        String sql = "INSERT INTO generos (nombre) VALUES (?)";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, genero.getNombre());
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizar(Genero genero) {
        String sql = "UPDATE generos SET nombre = ? WHERE idGenero = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, genero.getNombre());
            pstmt.setInt(2, genero.getIdGenero());
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminar(int idGenero) {
        String sql = "DELETE FROM generos WHERE idGenero = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idGenero);
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
