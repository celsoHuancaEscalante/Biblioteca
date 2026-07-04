
package Datos;

import ClaseBase.Autor;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class AutorAD {
    
    public List<Autor> obtenerTodos() {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT idAutor, primerNombre, segundoNombre, primerApellido, segundoApellido FROM autor";
        
        try (Connection conn = ConnectMySQL.conn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("primerNombre"),
                    rs.getString("segundoNombre"),
                    rs.getString("primerApellido"),
                    rs.getString("segundoApellido")
                );
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return autores;
    }

    public List<Autor> buscarPorNombre(String texto) {
        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT idAutor, primerNombre, segundoNombre, primerApellido, segundoApellido " +
                     "FROM autor WHERE primerNombre LIKE ? OR segundoNombre LIKE ? OR " +
                     "primerApellido LIKE ? OR segundoApellido LIKE ?";
                
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String busqueda = "%" + texto + "%";
            pstmt.setString(1, busqueda);
            pstmt.setString(2, busqueda);
            pstmt.setString(3, busqueda);
            pstmt.setString(4, busqueda);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("primerNombre"),
                    rs.getString("segundoNombre"),
                    rs.getString("primerApellido"),
                    rs.getString("segundoApellido")
                );
                autores.add(autor);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return autores;
    }
    

    public boolean insertar(Autor autor) {
        String sql = "INSERT INTO autor (primerNombre, segundoNombre, primerApellido, segundoApellido) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, autor.getPrimerNombre());
            pstmt.setString(2, autor.getSegundoNombre());
            pstmt.setString(3, autor.getPrimerApellido());
            pstmt.setString(4, autor.getSegundoApellido());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    

    public boolean actualizar(Autor autor) {
        String sql = "UPDATE autor SET primerNombre = ?, segundoNombre = ?, " +
                     "primerApellido = ?, segundoApellido = ? WHERE idAutor = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, autor.getPrimerNombre());
            pstmt.setString(2, autor.getSegundoNombre());
            pstmt.setString(3, autor.getPrimerApellido());
            pstmt.setString(4, autor.getSegundoApellido());
            pstmt.setInt(5, autor.getIdAutor());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    

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
    

