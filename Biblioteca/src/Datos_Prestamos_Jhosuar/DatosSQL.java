package Datos_Prestamos_Jhosuar;

import ClaseBase.Prestamo;
import ConnectXampp.ConnectMySQL;
import Modelo_Prestamos_Jhosuar.Prestamos_Tabla;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.time.LocalDate;
import java.util.ArrayList;

public class DatosSQL {

    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public ArrayList<Prestamos_Tabla> listarPrestamos() {
        return consultarPrestamos(null);
    }

    public ArrayList<Prestamos_Tabla> buscarPrestamosPorDni(String dni) {
        return consultarPrestamos(dni);
    }

    private ArrayList<Prestamos_Tabla> consultarPrestamos(String dni) {

        mensaje = null;

        ArrayList<Prestamos_Tabla> lista = new ArrayList<>();

        String sql =
            "SELECT " +
            "p.IDPrestamo AS id, " +
            "c.DNI AS dni, " +
            "CONCAT(c.PrimerNombre, ' ', c.PrimerApellido) AS usuario, " +
            "l.Titulo AS libro, " +
            "p.FechaPrestamo AS fecha_prestamo, " +
            "p.FechaVencimiento AS fecha_entrega, " +
            "CASE " +
            "   WHEN p.FechaDevolucion IS NOT NULL THEN 'Devuelto' " +
            "   WHEN CURDATE() > p.FechaVencimiento THEN 'Atrasado' " +
            "   ELSE 'Prestado' " +
            "END AS estado_prestamo, " +
            "CASE " +
            "   WHEN p.FechaDevolucion IS NULL AND CURDATE() > p.FechaVencimiento " +
            "   THEN DATEDIFF(CURDATE(), p.FechaVencimiento) * dp.MultaPorDiaAplicada " +
            "   WHEN p.FechaDevolucion IS NOT NULL AND p.FechaDevolucion > p.FechaVencimiento " +
            "   THEN DATEDIFF(p.FechaDevolucion, p.FechaVencimiento) * dp.MultaPorDiaAplicada " +
            "   ELSE 0 " +
            "END AS multa_acumulada " +
            "FROM prestamos p " +
            "INNER JOIN clientes c ON p.DNICliente = c.DNI " +
            "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
            "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
            "INNER JOIN libros l ON e.IDLibro = l.IDLibro ";

        if (dni != null) {
            sql += "WHERE c.DNI = ? ";
        }

        sql += "ORDER BY p.IDPrestamo DESC";

        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (dni != null) {
                pst.setString(1, dni);
            }

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Prestamos_Tabla prestamo = new Prestamos_Tabla(
                    rs.getInt("id"),
                    rs.getString("dni"),
                    rs.getString("usuario"),
                    rs.getString("libro"),
                    rs.getDate("fecha_prestamo").toLocalDate(),
                    rs.getDate("fecha_entrega").toLocalDate(),
                    rs.getString("estado_prestamo"),
                    rs.getDouble("multa_acumulada")
                );

                lista.add(prestamo);
            }

        } catch (SQLException e) {
            mensaje = "Error al consultar préstamos: " + e.getMessage();
        }

        return lista;
    }

    public boolean registrarPrestamo(Prestamo prestamo, int idLibro) {

        mensaje = null;

        Connection conn = ConnectMySQL.conn();

        if (conn == null) {
            mensaje = "No se pudo conectar a la base de datos.";
            return false;
        }

        try {
            conn.setAutoCommit(false);

            String dni = prestamo.getCliente().getDni();

            if (!existeCliente(conn, dni)) {
                mensaje = "El DNI ingresado no existe.";
                conn.rollback();
                return false;
            }

            if (prestamo.getFechaVencimiento() == null) {
                mensaje = "La fecha de entrega no puede estar vacía.";
                conn.rollback();
                return false;
            }

            String sqlEjemplar =
                "SELECT e.IDEjemplar, c.PrecioPrestamo, c.Garantia, c.MultaPorDia " +
                "FROM ejemplares e " +
                "INNER JOIN libros l ON e.IDLibro = l.IDLibro " +
                "INNER JOIN categorias c ON l.IDCategoria = c.IDCategoria " +
                "WHERE l.IDLibro = ? AND e.Estado = 'Disponible' " +
                "LIMIT 1";

            PreparedStatement pstEjemplar = conn.prepareStatement(sqlEjemplar);
            pstEjemplar.setInt(1, idLibro);
            ResultSet rsEjemplar = pstEjemplar.executeQuery();

            if (!rsEjemplar.next()) {
                mensaje = "No hay ejemplares disponibles para este libro.";
                conn.rollback();
                return false;
            }

            int idEjemplar = rsEjemplar.getInt("IDEjemplar");
            double precioPrestamo = rsEjemplar.getDouble("PrecioPrestamo");
            double garantia = rsEjemplar.getDouble("Garantia");
            double multaPorDia = rsEjemplar.getDouble("MultaPorDia");

            String sqlPrestamo =
                "INSERT INTO prestamos " +
                "(DNICliente, FechaPrestamo, FechaDevolucion, FechaVencimiento) " +
                "VALUES (?, CURDATE(), NULL, ?)";

            PreparedStatement pstPrestamo = conn.prepareStatement(
                sqlPrestamo,
                Statement.RETURN_GENERATED_KEYS
            );

            pstPrestamo.setString(1, dni);
            pstPrestamo.setDate(2, java.sql.Date.valueOf(prestamo.getFechaVencimiento()));
            pstPrestamo.executeUpdate();

            ResultSet rsKey = pstPrestamo.getGeneratedKeys();

            int idPrestamo;

            if (rsKey.next()) {
                idPrestamo = rsKey.getInt(1);
            } else {
                mensaje = "No se pudo generar el ID del préstamo.";
                conn.rollback();
                return false;
            }

            String sqlDetalle =
                "INSERT INTO detalle_prestamo " +
                "(IDEjemplar, IDPrestamo, PrecioPrestamoAplicado, GarantiaAplicada, MultaPorDiaAplicada) " +
                "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle);
            pstDetalle.setInt(1, idEjemplar);
            pstDetalle.setInt(2, idPrestamo);
            pstDetalle.setDouble(3, precioPrestamo);
            pstDetalle.setDouble(4, garantia);
            pstDetalle.setDouble(5, multaPorDia);
            pstDetalle.executeUpdate();

            String sqlActualizarEjemplar =
                "UPDATE ejemplares SET Estado = 'Prestado' WHERE IDEjemplar = ?";

            PreparedStatement pstActualizar = conn.prepareStatement(sqlActualizarEjemplar);
            pstActualizar.setInt(1, idEjemplar);
            pstActualizar.executeUpdate();

            conn.commit();

            mensaje = "Préstamo registrado correctamente. ID: " + idPrestamo;
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                mensaje = "Error al revertir cambios: " + ex.getMessage();
            }

            mensaje = "Error al registrar préstamo: " + e.getMessage();
            return false;

        } finally {
            cerrarConexion(conn);
        }
    }

    public boolean modificarPrestamo(int idPrestamo, String dniNuevo, String valorLibro, LocalDate fechaEntregaNueva) {

        mensaje = null;

        Connection conn = ConnectMySQL.conn();

        if (conn == null) {
            mensaje = "No se pudo conectar a la base de datos.";
            return false;
        }

        try {
            conn.setAutoCommit(false);

            if (!existeCliente(conn, dniNuevo)) {
                mensaje = "El DNI ingresado no existe.";
                conn.rollback();
                return false;
            }

            String sqlActual =
                "SELECT dp.IDEjemplar, e.IDLibro " +
                "FROM detalle_prestamo dp " +
                "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
                "WHERE dp.IDPrestamo = ?";

            PreparedStatement pstActual = conn.prepareStatement(sqlActual);
            pstActual.setInt(1, idPrestamo);
            ResultSet rsActual = pstActual.executeQuery();

            if (!rsActual.next()) {
                mensaje = "No se encontró el detalle del préstamo.";
                conn.rollback();
                return false;
            }

            int idEjemplarActual = rsActual.getInt("IDEjemplar");
            int idLibroActual = rsActual.getInt("IDLibro");

            int idLibroNuevo = idLibroActual;
            boolean cambiarLibro = false;

            try {
                idLibroNuevo = Integer.parseInt(valorLibro);
                cambiarLibro = idLibroNuevo != idLibroActual;
            } catch (NumberFormatException e) {
                cambiarLibro = false;
            }

            String sqlUpdatePrestamo =
                "UPDATE prestamos SET DNICliente = ?, FechaVencimiento = ? WHERE IDPrestamo = ?";

            PreparedStatement pstUpdatePrestamo = conn.prepareStatement(sqlUpdatePrestamo);
            pstUpdatePrestamo.setString(1, dniNuevo);
            pstUpdatePrestamo.setDate(2, java.sql.Date.valueOf(fechaEntregaNueva));
            pstUpdatePrestamo.setInt(3, idPrestamo);
            pstUpdatePrestamo.executeUpdate();

            if (cambiarLibro) {

                String sqlDisponible =
                    "SELECT e.IDEjemplar, c.PrecioPrestamo, c.Garantia, c.MultaPorDia " +
                    "FROM ejemplares e " +
                    "INNER JOIN libros l ON e.IDLibro = l.IDLibro " +
                    "INNER JOIN categorias c ON l.IDCategoria = c.IDCategoria " +
                    "WHERE e.IDLibro = ? AND e.Estado = 'Disponible' " +
                    "LIMIT 1";

                PreparedStatement pstDisponible = conn.prepareStatement(sqlDisponible);
                pstDisponible.setInt(1, idLibroNuevo);
                ResultSet rsDisponible = pstDisponible.executeQuery();

                if (!rsDisponible.next()) {
                    String estado = obtenerEstadoLibro(conn, idLibroNuevo);
                    mensaje = "El libro está en estado: " + estado;
                    conn.rollback();
                    return false;
                }

                int idEjemplarNuevo = rsDisponible.getInt("IDEjemplar");
                double precioPrestamo = rsDisponible.getDouble("PrecioPrestamo");
                double garantia = rsDisponible.getDouble("Garantia");
                double multaPorDia = rsDisponible.getDouble("MultaPorDia");

                String sqlLiberar =
                    "UPDATE ejemplares SET Estado = 'Disponible' WHERE IDEjemplar = ?";

                PreparedStatement pstLiberar = conn.prepareStatement(sqlLiberar);
                pstLiberar.setInt(1, idEjemplarActual);
                pstLiberar.executeUpdate();

                String sqlOcupar =
                    "UPDATE ejemplares SET Estado = 'Prestado' WHERE IDEjemplar = ?";

                PreparedStatement pstOcupar = conn.prepareStatement(sqlOcupar);
                pstOcupar.setInt(1, idEjemplarNuevo);
                pstOcupar.executeUpdate();

                String sqlUpdateDetalle =
                    "UPDATE detalle_prestamo " +
                    "SET IDEjemplar = ?, PrecioPrestamoAplicado = ?, GarantiaAplicada = ?, MultaPorDiaAplicada = ? " +
                    "WHERE IDPrestamo = ?";

                PreparedStatement pstUpdateDetalle = conn.prepareStatement(sqlUpdateDetalle);
                pstUpdateDetalle.setInt(1, idEjemplarNuevo);
                pstUpdateDetalle.setDouble(2, precioPrestamo);
                pstUpdateDetalle.setDouble(3, garantia);
                pstUpdateDetalle.setDouble(4, multaPorDia);
                pstUpdateDetalle.setInt(5, idPrestamo);
                pstUpdateDetalle.executeUpdate();
            }

            conn.commit();

            mensaje = "Cambios guardados correctamente.";
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                mensaje = "Error al revertir cambios: " + ex.getMessage();
            }

            mensaje = "Error al guardar cambios: " + e.getMessage();
            return false;

        } finally {
            cerrarConexion(conn);
        }
    }

    public boolean eliminarPrestamo(int idPrestamo) {

        mensaje = null;

        Connection conn = ConnectMySQL.conn();

        if (conn == null) {
            mensaje = "No se pudo conectar a la base de datos.";
            return false;
        }

        try {
            conn.setAutoCommit(false);

            String sqlEjemplar =
                "SELECT IDEjemplar FROM detalle_prestamo WHERE IDPrestamo = ?";

            PreparedStatement pstEjemplar = conn.prepareStatement(sqlEjemplar);
            pstEjemplar.setInt(1, idPrestamo);
            ResultSet rsEjemplar = pstEjemplar.executeQuery();

            int idEjemplar = -1;

            if (rsEjemplar.next()) {
                idEjemplar = rsEjemplar.getInt("IDEjemplar");
            }

            String sqlDetalle =
                "DELETE FROM detalle_prestamo WHERE IDPrestamo = ?";

            PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle);
            pstDetalle.setInt(1, idPrestamo);
            pstDetalle.executeUpdate();

            String sqlPrestamo =
                "DELETE FROM prestamos WHERE IDPrestamo = ?";

            PreparedStatement pstPrestamo = conn.prepareStatement(sqlPrestamo);
            pstPrestamo.setInt(1, idPrestamo);
            pstPrestamo.executeUpdate();

            if (idEjemplar != -1) {
                String sqlLiberar =
                    "UPDATE ejemplares SET Estado = 'Disponible' WHERE IDEjemplar = ?";

                PreparedStatement pstLiberar = conn.prepareStatement(sqlLiberar);
                pstLiberar.setInt(1, idEjemplar);
                pstLiberar.executeUpdate();
            }

            conn.commit();

            mensaje = "Préstamo eliminado correctamente.";
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                mensaje = "Error al revertir cambios: " + ex.getMessage();
            }

            mensaje = "Error al eliminar préstamo: " + e.getMessage();
            return false;

        } finally {
            cerrarConexion(conn);
        }
    }

    private boolean existeCliente(Connection conn, String dni) throws SQLException {

        String sql = "SELECT DNI FROM clientes WHERE DNI = ?";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, dni);

        ResultSet rs = pst.executeQuery();

        return rs.next();
    }

    private String obtenerEstadoLibro(Connection conn, int idLibro) throws SQLException {

        String sql =
            "SELECT Estado FROM ejemplares WHERE IDLibro = ? LIMIT 1";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, idLibro);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getString("Estado");
        }

        return "No existe";
    }

    private void cerrarConexion(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            mensaje = "Error al cerrar conexión: " + e.getMessage();
        }
    }
}