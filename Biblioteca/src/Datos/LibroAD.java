
package Datos;

import ClaseBase.*;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class LibroAD {
    
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.idLibro, l.titulo, l.anioPublicacion, l.stock, " +
                     "a.idAutor, a.primerNombre, a.segundoNombre, a.primerApellido, a.segundoApellido, " +
                     "e.idEditorial, e.nombre AS editorial_nombre, " +
                     "g.idGenero, g.nombre AS genero_nombre " +
                     "FROM libro l " +
                     "LEFT JOIN autor a ON l.idAutor = a.idAutor " +
                     "LEFT JOIN editorial e ON l.idEditorial = e.idEditorial " +
                     "LEFT JOIN genero g ON l.idGenero = g.idGenero " +
                     "ORDER BY l.titulo";
        
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
                
                Editorial editorial = new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("editorial_nombre")
                );
                
                Genero genero = new Genero(
                    rs.getInt("idGenero"),
                    rs.getString("genero_nombre")
                );
                
                Libro libro = new Libro(
                    rs.getInt("idLibro"),
                    genero,
                    autor,
                    editorial,
                    rs.getString("titulo"),
                    rs.getInt("stock"),
                    rs.getInt("anioPublicacion")
                );
                
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return libros;
    }
    

    public List<Libro> buscarPorTitulo(String titulo) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.idLibro, l.titulo, l.anioPublicacion, l.stock, " +
                     "a.idAutor, a.primerNombre, a.segundoNombre, a.primerApellido, a.segundoApellido, " +
                     "e.idEditorial, e.nombre AS editorial_nombre, " +
                     "g.idGenero, g.nombre AS genero_nombre " +
                     "FROM libro l " +
                     "LEFT JOIN autor a ON l.idAutor = a.idAutor " +
                     "LEFT JOIN editorial e ON l.idEditorial = e.idEditorial " +
                     "LEFT JOIN genero g ON l.idGenero = g.idGenero " +
                     "WHERE l.titulo LIKE ? " +
                     "ORDER BY l.titulo";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, titulo + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("primerNombre"),
                    rs.getString("segundoNombre"),
                    rs.getString("primerApellido"),
                    rs.getString("segundoApellido")
                );
                
                Editorial editorial = new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("editorial_nombre")
                );
                
                Genero genero = new Genero(
                    rs.getInt("idGenero"),
                    rs.getString("genero_nombre")
                );
                
                Libro libro = new Libro(
                    rs.getInt("idLibro"),
                    genero,
                    autor,
                    editorial,
                    rs.getString("titulo"),
                    rs.getInt("stock"),
                    rs.getInt("anioPublicacion")
                );
                
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return libros;
    }
    

    public List<Libro> filtrar(Genero genero, Autor autor) {
        List<Libro> libros = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT l.idLibro, l.titulo, l.anioPublicacion, l.stock, " +
            "a.idAutor, a.primerNombre, a.segundoNombre, a.primerApellido, a.segundoApellido, " +
            "e.idEditorial, e.nombre AS editorial_nombre, " +
            "g.idGenero, g.nombre AS genero_nombre " +
            "FROM libro l " +
            "LEFT JOIN autor a ON l.idAutor = a.idAutor " +
            "LEFT JOIN editorial e ON l.idEditorial = e.idEditorial " +
            "LEFT JOIN genero g ON l.idGenero = g.idGenero " +
            "WHERE 1=1 "
        );
        
        if (genero != null && genero.getIdGenero() > 0) {
            sql.append("AND g.idGenero = ").append(genero.getIdGenero()).append(" ");
        }
        
        if (autor != null && autor.getIdAutor() > 0) {
            sql.append("AND a.idAutor = ").append(autor.getIdAutor()).append(" ");
        }
        
        sql.append("ORDER BY l.titulo");
        
        try (Connection conn = ConnectMySQL.conn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
            
            while (rs.next()) {
                Autor a = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("primerNombre"),
                    rs.getString("segundoNombre"),
                    rs.getString("primerApellido"),
                    rs.getString("segundoApellido")
                );
                
                Editorial e = new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("editorial_nombre")
                );
                
                Genero g = new Genero(
                    rs.getInt("idGenero"),
                    rs.getString("genero_nombre")
                );
                
                Libro libro = new Libro(
                    rs.getInt("idLibro"),
                    g,
                    a,
                    e,
                    rs.getString("titulo"),
                    rs.getInt("stock"),
                    rs.getInt("anioPublicacion")
                );
                
                libros.add(libro);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return libros;
    }
    

    public int insertar(Libro libro) {
        String sql = "INSERT INTO libro (titulo, idAutor, idEditorial, idGenero, " +
                     "anioPublicacion, stock) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, libro.getTitulo());
            pstmt.setInt(2, libro.getAutor().getIdAutor());
            pstmt.setInt(3, libro.getEditorial().getIdEditorial());
            pstmt.setInt(4, libro.getGenero().getIdGenero());
            pstmt.setInt(5, libro.getAnioPublicacion());
            pstmt.setInt(6, libro.getStock());
            
            int filasAfectadas = pstmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return -1;
    }
    

    public boolean actualizar(Libro libro) {
        String sql = "UPDATE libro SET titulo = ?, idAutor = ?, idEditorial = ?, " +
                     "idGenero = ?, anioPublicacion = ?, stock = ? WHERE idLibro = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, libro.getTitulo());
            pstmt.setInt(2, libro.getAutor().getIdAutor());
            pstmt.setInt(3, libro.getEditorial().getIdEditorial());
            pstmt.setInt(4, libro.getGenero().getIdGenero());
            pstmt.setInt(5, libro.getAnioPublicacion());
            pstmt.setInt(6, libro.getStock());
            pstmt.setInt(7, libro.getIdLibro());
            
            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    

    public boolean eliminar(int idLibro) {
        String sql = "DELETE FROM libro WHERE idLibro = ?";
        
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
    

    public Libro obtenerPorId(int idLibro) {
        String sql = "SELECT l.idLibro, l.titulo, l.anioPublicacion, l.stock, " +
                     "a.idAutor, a.primerNombre, a.segundoNombre, a.primerApellido, a.segundoApellido, " +
                     "e.idEditorial, e.nombre AS editorial_nombre, " +
                     "g.idGenero, g.nombre AS genero_nombre " +
                     "FROM libro l " +
                     "LEFT JOIN autor a ON l.idAutor = a.idAutor " +
                     "LEFT JOIN editorial e ON l.idEditorial = e.idEditorial " +
                     "LEFT JOIN genero g ON l.idGenero = g.idGenero " +
                     "WHERE l.idLibro = ?";
        
        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLibro);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Autor autor = new Autor(
                    rs.getInt("idAutor"),
                    rs.getString("primerNombre"),
                    rs.getString("segundoNombre"),
                    rs.getString("primerApellido"),
                    rs.getString("segundoApellido")
                );
                
                Editorial editorial = new Editorial(
                    rs.getInt("idEditorial"),
                    rs.getString("editorial_nombre")
                );
                
                Genero genero = new Genero(
                    rs.getInt("idGenero"),
                    rs.getString("genero_nombre")
                );
                
                return new Libro(
                    rs.getInt("idLibro"),
                    genero,
                    autor,
                    editorial,
                    rs.getString("titulo"),
                    rs.getInt("stock"),
                    rs.getInt("anioPublicacion")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        return null;
    }
}