
package LogicaReporteIngresos;

import ClaseBase.*;
import ConnectXampp.ConnectMySQL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class ReporteLogica {
    

    public ReporteLogica() {
    }
    
    public long calcularDiasRetraso (java.sql.Date fechaVencimientoSql, java.sql.Date fechaDevolucionSql) {
        // CONDICIÓN: Si cualquiera de las dos fechas de la base de datos es null retorna en 0 doas de penalizacion
        if (fechaVencimientoSql == null || fechaDevolucionSql == null ){
            return 0;
        }
        
        // TRASPASO DE API: Convierte el formato antiguo 'java.sql.Date' a la API 'LocalDate'
        LocalDate fechaVencimiento = fechaVencimientoSql.toLocalDate();
        LocalDate fechaDevolucion = fechaDevolucionSql.toLocalDate();
        
        // CONDICIÓN: Verifica si el día de devolución ocurrió cronológicamente después del vencimiento
        if (fechaDevolucion.isAfter(fechaVencimiento)) {
            // Retorna calculando la distancia exacta en días entre ambas fechas y la devuelve
            return ChronoUnit.DAYS.between(fechaVencimiento, fechaDevolucion);
        }
        return 0;
        }
    
    public double calcularTotalconMulta (double precioBase, long diasRetraso, double multaDiaria) {
        return precioBase + (diasRetraso * multaDiaria); 
    }
    
    /**
     * Extrae de forma masiva el historial de todos los préstamos que ya han sido cerrados (devueltos).
     * @return Un ArrayList cargado con objetos 'ReporteFila', listos para ser consumidos por la tabla gráfica.
     */    
    public ArrayList <ReporteFila> obtenerHistorialGeneral () {
        // Instanciamiento de la lista dinámica vacía 
        ArrayList <ReporteFila> lista = new ArrayList<>();
        // Consulta sql relacional con multiples uniones
        String sql = "SELECT p.IDPrestamo AS id_prestamo, dp.IDEjemplar AS id_ejemplar, p.DNICliente AS dni, "
           + "p.FechaPrestamo AS fecha_prestamo, p.FechaDevolucion AS fecha_devolucion, p.FechaVencimiento AS fecha_vencimiento, "
           + "CONCAT(c.PrimerNombre, ' ', c.PrimerApellido) AS nombres, l.titulo, dp.PrecioPrestamoAplicado AS precio_base, dp.MultaPorDiaAplicada AS multa_diaria, g.Nombre AS nombre_genero " 
           + "FROM detalle_prestamo dp "
           + "JOIN prestamos p ON dp.IDPrestamo = p.IDPrestamo "
           + "JOIN clientes c ON p.DNICliente = c.DNI "
           + "JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar "
           + "JOIN libros l ON e.IDLibro = l.IDLibro "
           + "JOIN categorias cat ON l.IDCategoria = cat.IDCategoria "
           + "JOIN generos g ON l.IDGenero = g.IDGenero "
           + "WHERE p.FechaDevolucion IS NOT NULL"
           + " ORDER BY p.IDPrestamo ASC";
        
        try {
            // Solicita a la clase ConnectMySQL que abra el canal de comunicación
            Connection cn = ConnectMySQL.conn();
            // Prepara la estructura de la consulta SQL 
            PreparedStatement pst = cn.prepareStatement(sql);
            // Dispara la orden en MySQL y almacena el puntero de las filas devueltas en 'rs'
            ResultSet rs = pst.executeQuery(); 
            // Define el formato para convertir 
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            
            // Recorre el ResultSet fila por fila mientras existan registros hacia adelante
            while (rs.next()) {                
             // 1. Reconstruimos de los objetos o datos
                Cliente cliente = new Cliente();
                cliente.setDni(rs.getString("dni"));
            
                Prestamo prestamo = new Prestamo();
                // Asignamos datos usando conversión limpia
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
                prestamo.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                prestamo.setCliente(cliente);
                
                Genero genero = new Genero();
                genero.setNombre(rs.getString("nombre_genero"));
                
                
                Libro libro = new Libro();
                libro.setTitulo(rs.getString("titulo"));
                libro.setGenero(genero);
                
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setLibro(libro);

                DetallePrestamo detalle = new DetallePrestamo();
                detalle.setEjemplar(ejemplar);
                detalle.setPrecioPrestamoAplicado(rs.getDouble("precio_base"));
                
                // 2. Cálculos financieros
                // Extrae la fecha de vencimiento original de la fila 
                java.sql.Date fechaVenc = rs.getDate("fecha_vencimiento");
                // Extrae la fecha de devolución real de la fila 
                java.sql.Date fechaDevol = rs.getDate("fecha_devolucion");
                // Invoca a 'calcularDiasRetraso' pasándole las fechas obtenidas
                long diasRetraso = calcularDiasRetraso(fechaVenc, fechaDevol);
                // Invoca a 'calcularTotalconMulta' procesando los montos económicos y el retraso
                double total = calcularTotalconMulta(rs.getDouble("precio_base"), diasRetraso, rs.getDouble("multa_diaria"));
                
                // 3. Empaquetamos todo usando composición pura
                ReporteFila reporteFila = new ReporteFila(prestamo, detalle, diasRetraso, total);
             lista.add(reporteFila);
            }
            
            rs.close();
            pst.close();
            cn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error... " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Extrae el historial de préstamos cerrados restringiendo los resultados a un rango de fechas específico.
     * @param de Fecha de inicio del filtro temporal (java.util.Date).
     * @param hasta Fecha de fin del filtro temporal (java.util.Date).
     * @return Un ArrayList con los objetos 'ReporteFila' que encajaron dentro del rango establecido.
     */
    
    public ArrayList <ReporteFila> obtenerHistorialFiltrado (java.util.Date de, java.util.Date hasta){
       ArrayList <ReporteFila> lista = new ArrayList<>();
       // Consulta sql paramtetrizada
        String sql = "SELECT p.IDPrestamo AS id_prestamo, dp.IDEjemplar AS id_ejemplar, p.DNICliente AS dni, "
           + "p.FechaPrestamo AS fecha_prestamo, p.FechaDevolucion AS fecha_devolucion, p.FechaVencimiento AS fecha_vencimiento, "
           + "CONCAT(c.PrimerNombre, ' ', c.PrimerApellido) AS nombres, l.titulo, dp.PrecioPrestamoAplicado AS precio_base, dp.MultaPorDiaAplicada AS multa_diaria, g.Nombre AS nombre_genero " 
           + "FROM detalle_prestamo dp "
           + "JOIN prestamos p ON dp.IDPrestamo = p.IDPrestamo "
           + "JOIN clientes c ON p.DNICliente = c.DNI "
           + "JOIN ejemplares e ON dp.IDEjemplar = e.IDEjemplar "
           + "JOIN libros l ON e.IDLibro = l.IDLibro "
           + "JOIN categorias cat ON l.IDCategoria = cat.IDCategoria "
           + "JOIN generos g ON l.IDGenero = g.IDGenero "
           + "WHERE p.FechaPrestamo BETWEEN ? AND ? " 
           + "AND p.FechaDevolucion IS NOT NULL"
           + " ORDER BY p.IDPrestamo ASC";
        
        try {
            // Conecta con el servidor 
            Connection cn = ConnectMySQL.conn();
            // Prepara la consulta parametrizada
            PreparedStatement pst = cn.prepareStatement(sql);
            
            // Convierte la fecha de inicio de Java a formato de fecha compatible 
            java.sql.Date fechaInicioSql = new java.sql.Date(de.getTime());
            // Convierte la fecha de fin de Java a formato de fecha compatible
            java.sql.Date fechaFinSql = new java.sql.Date(hasta.getTime());
            
            //Asignamos las fechas en orden
            pst.setDate(1, fechaInicioSql);
            pst.setDate(2, fechaFinSql);
            
             // Lanza la consulta filtrada y obtiene filas
            ResultSet rs = pst.executeQuery(); 
            //Formato de fecha
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            
             // Recorre el ResultSet fila por fila mientras existan registros hacia adelante
            while (rs.next()) {                
             // 1. Reconstruimos de los objetos o datos 
                Cliente cliente = new Cliente();
                cliente.setDni(rs.getString("dni")); 
                
                Prestamo prestamo = new Prestamo();
                // Asignamos datos usando conversión limpia a localDate
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
                prestamo.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                prestamo.setCliente(cliente);
                
                Genero genero = new Genero();
                genero.setNombre(rs.getString("nombre_genero"));

                Libro libro = new Libro();
                libro.setTitulo(rs.getString("titulo"));
                libro.setGenero(genero);
                
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setLibro(libro);

                DetallePrestamo detalle = new DetallePrestamo();
                detalle.setEjemplar(ejemplar);
                detalle.setPrecioPrestamoAplicado(rs.getDouble("precio_base"));
                
                // 2. Cálculos financieros
                java.sql.Date fechaVenc = rs.getDate("fecha_vencimiento");
                java.sql.Date fechaDevol = rs.getDate("fecha_devolucion");
             
             // Invoca a 'calcularDiasRetraso' pasándole las fechas obtenidas
             long diasRestraso = calcularDiasRetraso(fechaVenc, fechaDevol);
             // Invoca a 'calcularTotalconMulta' pasándole las fechas obtenidas
             double total = calcularTotalconMulta(rs.getDouble("precio_base"), diasRestraso, rs.getDouble("multa_diaria"));
             
             //Instanciamos el objeto fila y lo agregamos
             ReporteFila reporteFila = new ReporteFila(prestamo, detalle, diasRestraso, total);
             lista.add(reporteFila);
            }
            
            rs.close();
            pst.close();
            cn.close();
            
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error... " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Consulta la base de datos para extraer los años únicos (sin repetir) en los que existen registros de ganancias.
     * @return Un ArrayList cargado con números enteros (Integer) ordenados descendentemente.
     */
    public ArrayList<Integer> obtenerAniosConGanacias () {
        // Instancia una lista dinámica vacía para alojar los años únicos encontrados
        ArrayList<Integer> anios = new ArrayList<>();
        //Consulta sql
        String sql = "SELECT DISTINCT YEAR(p.FechaPrestamo) AS anio "
                + "FROM prestamos p "
                + "WHERE p.FechaDevolucion IS NOT NULL "
                + "ORDER BY anio DESC";
        try {            
            // Conexion con la base de datos
            Connection cn = ConnectMySQL.conn();
            // Condicion: si el conector de la base de datos es igual a un null, falla 
            if (cn == null) {
            JOptionPane.showMessageDialog(null, "Error");
            return anios;
            }
            // Prepara la consulta parametrizada
            PreparedStatement pst = cn.prepareStatement(sql);
            // Lanza la consulta
            ResultSet rs = pst.executeQuery();
            
            //bucle donde recorre fila por fila mientras existan registros hacia adelante
            while (rs.next()) {       
                //Extrae el valor de anio y lo agrega a la lista
                anios.add(rs.getInt("anio"));
            }
            rs.close();
            pst.close();
            cn.close();
            //Captura de Fallos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener años de ganacia: " + e.getMessage());
        }
        return anios;
    }
    
}
