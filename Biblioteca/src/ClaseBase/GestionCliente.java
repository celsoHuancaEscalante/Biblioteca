package ClaseBase;
import ConnectXampp.ConnectMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class GestionCliente {

    public boolean agregar(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (DNI, PrimerNombre, PrimerApellido, Telefono, Correo, FechaRegistro) VALUES (?,?,?,?,?,?)";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getPrimerNombre());
            ps.setString(3, c.getPrimerApellido());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setDate(6, java.sql.Date.valueOf(c.getFechaRegistro()));
            int filas = ps.executeUpdate();
            return filas > 0;
        }
        
    }

    public boolean tienePrestamos(String dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE DNICliente = ?";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public boolean eliminar(String dni) throws SQLException {
        if (tienePrestamos(dni)) {
            return false; // la Vista decide qué mensaje mostrar según el motivo
        }

        String sql = "DELETE FROM clientes WHERE DNI = ?";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY FechaRegistro ASC";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getString("DNI"),
                        rs.getString("PrimerNombre"),
                        rs.getString("PrimerApellido"),
                        rs.getString("Telefono"),
                        rs.getString("Correo")
                ));
            }
        }
        return lista;
    }

    public String calcularEstado(String dni) throws SQLException {
        String sql = "SELECT COUNT(*) FROM prestamos WHERE DNICliente = ? AND FechaDevolucion IS NULL";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0 ? "DEUDOR" : "AL DIA";
            }
        }
        return "AL DIA";
    }

    public double calcularMulta(String dni) throws SQLException {
        String sql = "SELECT SUM(dp.MultaPorDiaAplicada * DATEDIFF(CURDATE(), p.FechaVencimiento)) "
                + "FROM detalle_prestamo dp "
                + "JOIN prestamos p ON dp.IDPrestamo = p.IDPrestamo "
                + "WHERE p.DNICliente = ? AND p.FechaDevolucion IS NULL "
                + "AND CURDATE() > p.FechaVencimiento";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }

    public Cliente obtenerPorDni(String dni) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE DNI = ?";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getString("DNI"),
                        rs.getString("PrimerNombre"),
                        rs.getString("PrimerApellido"),
                        rs.getString("Telefono"),
                        rs.getString("Correo")
                );
            }
        }
        return null;
    }

    public boolean actualizar(Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET PrimerNombre=?, PrimerApellido=?, Telefono=?, Correo=? WHERE DNI=?";
        try (Connection con = ConnectMySQL.conn(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getPrimerNombre());
            ps.setString(2, c.getPrimerApellido());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getDni());
            int filas = ps.executeUpdate();
            return filas > 0;
        }
    }
}