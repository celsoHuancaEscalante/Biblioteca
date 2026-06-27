package ClaseBase;
import ConnectXampp.ConnectMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class GestionCliente {

    public void agregar(Cliente c) {
        String sql = "INSERT INTO clientes (DNI, PrimerNombre,PrimerApellido, Telefono, Correo, FechaRegistro) VALUES (?,?,?,?,?,?)";
        try {
            Connection con = ConnectMySQL.conn();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getDni());
            ps.setString(2, c.getPrimerNombre());
            ps.setString(3, c.getPrimerApellido());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setDate(6, java.sql.Date.valueOf(c.getFechaRegistro()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cliente registrado");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
