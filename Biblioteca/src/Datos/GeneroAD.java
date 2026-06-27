
package Datos;

import ClaseBase.Genero;
import ConnectXampp.ConnectMySQL;
import java.sql.*;
import java.util.*;

public class GeneroAD {
    
    public List<Genero> obtenerTodos(){
        List<Genero> generos=new ArrayList<>();
        String sql= "SELECT idGnero, nombre FROM genero";
        
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
}
