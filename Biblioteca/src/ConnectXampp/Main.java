
package ConnectXampp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class Main {
    
    static Connection conn;
    static PreparedStatement pst;
    
    public static void main(String[] args) {
        
        conn = ConnectMySQL.conn();
        
        if (conn!=null) {
            JOptionPane.showMessageDialog(null, "Conexion exitosa");
        } else {
            JOptionPane.showMessageDialog(null, "Conexion fallida");
        }
        
    }
    
}
