package Datos_Prestamos;

import ClaseBase.Prestamo;
import ConnectXampp.ConnectMySQL;
import Modelo_Prestamos.Prestamos_Tabla;

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

    "DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) AS dias_entre_fechas, " +

    "GREATEST(DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo, 0) AS dias_atraso, " +

    "CASE " +
    "   WHEN e.Estado = 'Perdido' THEN 'Perdido' " +
    "   WHEN e.Estado = 'Disponible' THEN 'Devuelto' " +
    "   WHEN p.FechaDevolucion IS NOT NULL THEN 'Devuelto' " +
    "   WHEN (DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo) >= 100 THEN 'Perdido' " +
    "   WHEN (DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo) > 0 THEN 'Atrasado' " +
    "   ELSE 'Prestado' " +
    "END AS estado_prestamo, " +

    "GREATEST(DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo, 0) * dp.MultaPorDiaAplicada AS multa_acumulada, " +

    "CASE " +
    "   WHEN e.Estado = 'Disponible' THEN 0 " +
    "   WHEN p.FechaDevolucion IS NOT NULL THEN 0 " +
    "   WHEN e.Estado = 'Perdido' THEN " +
    "       dp.PrecioPrestamoAplicado + dp.GarantiaAplicada + " +
    "       (GREATEST(DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo, 0) * dp.MultaPorDiaAplicada) + 100 " +
    "   WHEN (DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo) >= 100 THEN " +
    "       dp.PrecioPrestamoAplicado + dp.GarantiaAplicada + " +
    "       (GREATEST(DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo, 0) * dp.MultaPorDiaAplicada) + 100 " +
    "   ELSE " +
    "       dp.PrecioPrestamoAplicado + dp.GarantiaAplicada + " +
    "       (GREATEST(DATEDIFF(p.FechaVencimiento, p.FechaPrestamo) - cat.DiasMaximo, 0) * dp.MultaPorDiaAplicada) " +
    "END AS pago_total " +

    "FROM prestamos p " +
    "INNER JOIN clientes c ON p.DNICliente = c.DNI " +
    "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
    "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
    "INNER JOIN libros l ON e.IDLibro = l.IDLibro " +
    "INNER JOIN categorias cat ON l.IDCategoria = cat.IDCategoria ";

if (dni != null) {
    sql += "WHERE c.DNI LIKE ? ";
}

sql += "ORDER BY p.IDPrestamo DESC";

        try (Connection conn = ConnectMySQL.conn();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (dni != null) {
                pst.setString(1, dni + "%");
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
                        rs.getDouble("pago_total")
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

    public boolean modificarPrestamo(int idPrestamo, String dniNuevo, String valorLibro,LocalDate fechaPrestamoNueva, LocalDate fechaEntregaNueva)
    {

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
    "UPDATE prestamos SET DNICliente = ?, FechaPrestamo = ?, FechaVencimiento = ? WHERE IDPrestamo = ?";

            PreparedStatement pstUpdatePrestamo = conn.prepareStatement(sqlUpdatePrestamo);
            pstUpdatePrestamo.setString(1, dniNuevo);
pstUpdatePrestamo.setDate(2, java.sql.Date.valueOf(fechaPrestamoNueva));
pstUpdatePrestamo.setDate(3, java.sql.Date.valueOf(fechaEntregaNueva));
pstUpdatePrestamo.setInt(4, idPrestamo);
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

    public boolean existeClientePorDni(String dni) {

    mensaje = null;

    String sql = "SELECT DNI FROM clientes WHERE DNI = ?";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, dni);

        ResultSet rs = pst.executeQuery();

        return rs.next();

    } catch (SQLException e) {
        mensaje = "Error al validar DNI: " + e.getMessage();
        return false;
    }
}
    
    public int obtenerDiasMaximosPorLibro(int idLibro) {

    mensaje = null;

    String sql =
        "SELECT c.DiasMaximo " +
        "FROM libros l " +
        "INNER JOIN categorias c ON l.IDCategoria = c.IDCategoria " +
        "WHERE l.IDLibro = ?";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idLibro);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getInt("DiasMaximo");
        }

        mensaje = "No existe un libro con ese código.";
        return -1;

    } catch (SQLException e) {
        mensaje = "Error al obtener días máximos: " + e.getMessage();
        return -1;
    }
}
    
    public int obtenerDiasMaximosPorPrestamo(int idPrestamo) {

    mensaje = null;

    String sql =
        "SELECT c.DiasMaximo " +
        "FROM prestamos p " +
        "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
        "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
        "INNER JOIN libros l ON e.IDLibro = l.IDLibro " +
        "INNER JOIN categorias c ON l.IDCategoria = c.IDCategoria " +
        "WHERE p.IDPrestamo = ?";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idPrestamo);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getInt("DiasMaximo");
        }

        mensaje = "No se encontró la categoría del libro para este préstamo.";
        return -1;

    } catch (SQLException e) {
        mensaje = "Error al obtener días máximos del préstamo: " + e.getMessage();
        return -1;
    }
}
    
    public boolean cambiarEstadoPrestamoPorDobleClick(int idPrestamo, String estadoActual) {

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

        if (!rsEjemplar.next()) {
            mensaje = "No se encontró el ejemplar del préstamo.";
            conn.rollback();
            return false;
        }

        int idEjemplar = rsEjemplar.getInt("IDEjemplar");

        if (estadoActual.equalsIgnoreCase("Devuelto")) {

            String sqlPrestamo =
                "UPDATE prestamos SET FechaDevolucion = NULL WHERE IDPrestamo = ?";

            PreparedStatement pstPrestamo = conn.prepareStatement(sqlPrestamo);
            pstPrestamo.setInt(1, idPrestamo);
            pstPrestamo.executeUpdate();

            String sqlEjemplarPrestado =
                "UPDATE ejemplares SET Estado = 'Prestado' WHERE IDEjemplar = ?";

            PreparedStatement pstPrestado = conn.prepareStatement(sqlEjemplarPrestado);
            pstPrestado.setInt(1, idEjemplar);
            pstPrestado.executeUpdate();

            mensaje = "El préstamo volvió a estado Prestado.";

        } else {

            String sqlPrestamo =
                "UPDATE prestamos SET FechaDevolucion = CURDATE() WHERE IDPrestamo = ?";

            PreparedStatement pstPrestamo = conn.prepareStatement(sqlPrestamo);
            pstPrestamo.setInt(1, idPrestamo);
            pstPrestamo.executeUpdate();

            String sqlEjemplarDisponible =
                "UPDATE ejemplares SET Estado = 'Disponible' WHERE IDEjemplar = ?";

            PreparedStatement pstDisponible = conn.prepareStatement(sqlEjemplarDisponible);
            pstDisponible.setInt(1, idEjemplar);
            pstDisponible.executeUpdate();

            mensaje = "El préstamo fue marcado como Devuelto.";
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            mensaje = "Error al revertir cambios: " + ex.getMessage();
        }

        mensaje = "Error al cambiar estado del préstamo: " + e.getMessage();
        return false;

    } finally {
        cerrarConexion(conn);
    }
}
    
public String obtenerDetallePrestamoHTML(int idPrestamo) {

    mensaje = null;

    String sql =
        "SELECT " +
        "p.IDPrestamo, " +
        "p.FechaPrestamo, " +
        "p.FechaVencimiento, " +
        "p.FechaDevolucion, " +
        "c.DNI, " +
        "CONCAT(c.PrimerNombre, ' ', c.PrimerApellido) AS usuario, " +
        "dp.IDDetalle, " +
        "dp.IDEjemplar, " +
        "dp.PrecioPrestamoAplicado, " +
        "dp.GarantiaAplicada, " +
        "dp.MultaPorDiaAplicada, " +
        "e.Estado AS EstadoEjemplar, " +
        "cat.DiasMaximo " +
        "FROM prestamos p " +
        "INNER JOIN clientes c ON p.DNICliente = c.DNI " +
        "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
        "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
        "INNER JOIN libros l ON e.IDLibro = l.IDLibro " +
        "INNER JOIN categorias cat ON l.IDCategoria = cat.IDCategoria " +
        "WHERE p.IDPrestamo = ?";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idPrestamo);

        ResultSet rs = pst.executeQuery();

        if (!rs.next()) {
            mensaje = "No se encontraron detalles para este préstamo.";
            return null;
        }

        int idDetalle = rs.getInt("IDDetalle");
        int idEjemplar = rs.getInt("IDEjemplar");

        String dni = rs.getString("DNI");
        String usuario = rs.getString("usuario");
        String estadoEjemplar = rs.getString("EstadoEjemplar");

        LocalDate fechaPrestamo = rs.getDate("FechaPrestamo").toLocalDate();
        LocalDate fechaEntrega = rs.getDate("FechaVencimiento").toLocalDate();

        java.sql.Date fechaDevolucionSQL = rs.getDate("FechaDevolucion");

        double precio = rs.getDouble("PrecioPrestamoAplicado");
        double garantia = rs.getDouble("GarantiaAplicada");
        double multaPorDia = rs.getDouble("MultaPorDiaAplicada");

        int diasMaximos = rs.getInt("DiasMaximo");

        long diasEntrePrestamoYEntrega = java.time.temporal.ChronoUnit.DAYS.between(
                fechaPrestamo,
                fechaEntrega
        );

        long diasAtraso = diasEntrePrestamoYEntrega - diasMaximos;

        if (diasAtraso < 0) {
            diasAtraso = 0;
        }

        String estadoPrestamo;

        if (estadoEjemplar.equalsIgnoreCase("Perdido")) {
            estadoPrestamo = "Perdido";

        } else if (estadoEjemplar.equalsIgnoreCase("Disponible")) {
            estadoPrestamo = "Devuelto";

        } else if (fechaDevolucionSQL != null) {
            estadoPrestamo = "Devuelto";

        } else if (diasAtraso >= 100) {
            estadoPrestamo = "Perdido";

        } else if (diasAtraso > 0) {
            estadoPrestamo = "Atrasado";

        } else {
            estadoPrestamo = "Prestado";
        }

        boolean devuelto = estadoPrestamo.equalsIgnoreCase("Devuelto");
        boolean bloqueado = estadoPrestamo.equalsIgnoreCase("Perdido");

        double multaTotal = diasAtraso * multaPorDia;
        double pagoReinscripcion = 100.00;
        double pagoTotal;

        if (devuelto) {
            pagoTotal = 0.00;

        } else if (bloqueado) {
            pagoTotal = precio + garantia + multaTotal + pagoReinscripcion;

        } else {
            pagoTotal = precio + garantia + multaTotal;
        }

        String estadoUsuarioTexto;
        String estadoUsuarioHTML;

        if (bloqueado) {
            estadoUsuarioTexto = "Bloqueado";
            estadoUsuarioHTML = "<b><font color='red'>Bloqueado</font></b>";
        } else {
            estadoUsuarioTexto = "Activo";
            estadoUsuarioHTML = "<font color='green'>Activo</font>";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<h2>Detalles de Préstamo</h2>");

        sb.append("<p>");
        sb.append("• <b>ID Préstamo:</b> ").append(idPrestamo).append("<br>");
        sb.append("• <b>ID Detalle Usuario:</b> ").append(idDetalle).append("<br>");
        sb.append("• <b>DNI Usuario:</b> ").append(dni).append("<br>");
        sb.append("• <b>Usuario:</b> ").append(usuario).append("<br>");
        sb.append("• <b>ID Ejemplar:</b> ").append(idEjemplar).append("<br>");
        sb.append("• <b>Estado Préstamo:</b> ").append(estadoPrestamo).append("<br>");
        sb.append("• <b>Estado Usuario:</b> ").append(estadoUsuarioHTML).append("<br>");
        sb.append("</p>");

        sb.append("<hr>");

        sb.append("<p>");
        sb.append("<b>Datos numéricos:</b><br>");
        sb.append("• <b>Precio Categoría:</b> S/ ").append(String.format("%.2f", precio)).append("<br>");
        sb.append("• <b>+</b><br>");
        sb.append("• <b>Garantía:</b> S/ ").append(String.format("%.2f", garantia)).append("<br>");

        if (devuelto) {

            sb.append("• <b>Pago Total:</b> Cancelado");

        } else if (bloqueado) {

            sb.append("• <b>+</b><br>");
            sb.append("• <b>Días de retraso:</b> ").append(diasAtraso).append("<br>");
            sb.append("• <b>×</b><br>");
            sb.append("• <b>Multa retraso:</b> S/ ").append(String.format("%.2f", multaPorDia)).append("<br>");
            sb.append("• <b>+</b><br>");
            sb.append("• <b>Pago de Reinscripción:</b> S/ ").append(String.format("%.2f", pagoReinscripcion)).append("<br>");
            sb.append("• <b>Pago Total:</b> S/ ").append(String.format("%.2f", pagoTotal));

        } else if (diasAtraso > 0) {

            sb.append("• <b>+</b><br>");
            sb.append("• <b>Días de retraso:</b> ").append(diasAtraso).append("<br>");
            sb.append("• <b>×</b><br>");
            sb.append("• <b>Multa retraso:</b> S/ ").append(String.format("%.2f", multaPorDia)).append("<br>");
            sb.append("• <b>Pago Total:</b> S/ ").append(String.format("%.2f", pagoTotal));

        } else {

            sb.append("• <b>Pago Total:</b> S/ ").append(String.format("%.2f", pagoTotal));
        }

        sb.append("</p>");

        sb.append("<hr>");

        sb.append("<p>");

        sb.append("</html>");

        return sb.toString();

    } catch (SQLException e) {
        mensaje = "Error al obtener detalles del préstamo: " + e.getMessage();
        return null;
    }
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
    
    public double[] obtenerDatosPagoPorPrestamo(int idPrestamo) {

    mensaje = null;

    String sql =
        "SELECT PrecioPrestamoAplicado, GarantiaAplicada, MultaPorDiaAplicada " +
        "FROM detalle_prestamo " +
        "WHERE IDPrestamo = ?";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setInt(1, idPrestamo);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            double precioPrestamo = rs.getDouble("PrecioPrestamoAplicado");
            double garantia = rs.getDouble("GarantiaAplicada");
            double multaPorDia = rs.getDouble("MultaPorDiaAplicada");

            return new double[]{precioPrestamo, garantia, multaPorDia};
        }

        mensaje = "No se encontraron datos de pago para este préstamo.";
        return null;

    } catch (SQLException e) {
        mensaje = "Error al obtener datos de pago: " + e.getMessage();
        return null;
    }
}

    public String obtenerTituloLibroDisponible(int idLibro) {

    mensaje = null;

    String sqlExiste =
        "SELECT Titulo FROM libros WHERE IDLibro = ?";

    String sqlDisponible =
        "SELECT l.Titulo " +
        "FROM libros l " +
        "INNER JOIN ejemplares e ON l.IDLibro = e.IDLibro " +
        "WHERE l.IDLibro = ? AND e.Estado = 'Disponible' " +
        "LIMIT 1";

    try (Connection conn = ConnectMySQL.conn()) {

        PreparedStatement pstExiste = conn.prepareStatement(sqlExiste);
        pstExiste.setInt(1, idLibro);
        ResultSet rsExiste = pstExiste.executeQuery();

        if (!rsExiste.next()) {
            mensaje = "El libro no está registrado.";
            return null;
        }

        PreparedStatement pstDisponible = conn.prepareStatement(sqlDisponible);
        pstDisponible.setInt(1, idLibro);
        ResultSet rsDisponible = pstDisponible.executeQuery();

        if (rsDisponible.next()) {
            return rsDisponible.getString("Titulo");
        }

        mensaje = "El libro no está disponible.";
        return null;

    } catch (SQLException e) {
        mensaje = "Error al validar libro: " + e.getMessage();
        return null;
    }
}
    
    public String validarDniParaEdicion(String dni, int idPrestamoActual) {

    mensaje = null;

    String sqlCliente =
        "SELECT CONCAT(PrimerNombre, ' ', PrimerApellido) AS usuario " +
        "FROM clientes " +
        "WHERE DNI = ?";

    String sqlPerdido =
        "SELECT COUNT(*) AS cantidad " +
        "FROM prestamos p " +
        "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
        "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
        "WHERE p.DNICliente = ? " +
        "AND p.IDPrestamo <> ? " +
        "AND (e.Estado = 'Perdido' " +
        "OR (p.FechaDevolucion IS NULL AND DATEDIFF(CURDATE(), p.FechaVencimiento) >= 100))";

    String sqlPrestamoActivo =
        "SELECT COUNT(*) AS cantidad " +
        "FROM prestamos p " +
        "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
        "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
        "WHERE p.DNICliente = ? " +
        "AND p.IDPrestamo <> ? " +
        "AND p.FechaDevolucion IS NULL " +
        "AND e.Estado <> 'Disponible'";

    try (Connection conn = ConnectMySQL.conn()) {

        PreparedStatement pstCliente = conn.prepareStatement(sqlCliente);
        pstCliente.setString(1, dni);
        ResultSet rsCliente = pstCliente.executeQuery();

        if (!rsCliente.next()) {
            mensaje = "Poner una ID Valida.";
            return null;
        }

        String usuario = rsCliente.getString("usuario");

        PreparedStatement pstPerdido = conn.prepareStatement(sqlPerdido);
        pstPerdido.setString(1, dni);
        pstPerdido.setInt(2, idPrestamoActual);
        ResultSet rsPerdido = pstPerdido.executeQuery();

        if (rsPerdido.next() && rsPerdido.getInt("cantidad") > 0) {
            mensaje = "Usuario bloqueado por pérdida de ejemplar.";
            return null;
        }

        PreparedStatement pstPrestamo = conn.prepareStatement(sqlPrestamoActivo);
        pstPrestamo.setString(1, dni);
        pstPrestamo.setInt(2, idPrestamoActual);
        ResultSet rsPrestamo = pstPrestamo.executeQuery();

        if (rsPrestamo.next() && rsPrestamo.getInt("cantidad") > 0) {
            mensaje = "Este usuario ya tiene un préstamo.";
            return null;
        }

        return usuario;

    } catch (SQLException e) {
        mensaje = "Error al validar usuario: " + e.getMessage();
        return null;
    }
}
    
    public boolean validarUsuarioPuedeRegistrarPrestamo(String dni) {

    mensaje = null;

    String sqlCliente =
        "SELECT DNI FROM clientes WHERE DNI = ?";

    String sqlPerdido =
        "SELECT COUNT(*) AS cantidad " +
        "FROM prestamos p " +
        "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
        "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
        "WHERE p.DNICliente = ? " +
        "AND (e.Estado = 'Perdido' " +
        "OR (p.FechaDevolucion IS NULL AND DATEDIFF(CURDATE(), p.FechaVencimiento) >= 100))";

    String sqlPrestamoActivo =
        "SELECT COUNT(*) AS cantidad " +
        "FROM prestamos p " +
        "INNER JOIN detalle_prestamo dp ON p.IDPrestamo = dp.IDPrestamo " +
        "INNER JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar " +
        "WHERE p.DNICliente = ? " +
        "AND p.FechaDevolucion IS NULL " +
        "AND e.Estado <> 'Disponible'";

    try (Connection conn = ConnectMySQL.conn()) {

        PreparedStatement pstCliente = conn.prepareStatement(sqlCliente);
        pstCliente.setString(1, dni);
        ResultSet rsCliente = pstCliente.executeQuery();

        if (!rsCliente.next()) {
            mensaje = "Poner un usuario ID válido.";
            return false;
        }

        PreparedStatement pstPerdido = conn.prepareStatement(sqlPerdido);
        pstPerdido.setString(1, dni);
        ResultSet rsPerdido = pstPerdido.executeQuery();

        if (rsPerdido.next() && rsPerdido.getInt("cantidad") > 0) {
            mensaje = "Usuario bloqueado por pérdida de ejemplar. No se puede registrar préstamo.";
            return false;
        }

        PreparedStatement pstActivo = conn.prepareStatement(sqlPrestamoActivo);
        pstActivo.setString(1, dni);
        ResultSet rsActivo = pstActivo.executeQuery();

        if (rsActivo.next() && rsActivo.getInt("cantidad") > 0) {
            mensaje = "Este usuario ya tiene un préstamo activo, atrasado o pendiente. No se puede registrar otro préstamo.";
            return false;
        }

        return true;

    } catch (SQLException e) {
        mensaje = "Error al validar usuario para préstamo: " + e.getMessage();
        return false;
    }
}
    
    
}