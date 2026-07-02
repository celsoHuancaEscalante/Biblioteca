
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
        if (fechaVencimientoSql == null || fechaDevolucionSql == null ){
            return 0;
        }
        
        LocalDate fechaVencimiento = fechaVencimientoSql.toLocalDate();
        LocalDate fechaDevolucion = fechaDevolucionSql.toLocalDate();
            
        if (fechaDevolucion.isAfter(fechaVencimiento)) {
            return ChronoUnit.DAYS.between(fechaVencimiento, fechaDevolucion);
        }
        return 0;
        }
    
    public double calcularTotalconMulta (double precioBase, long diasRetraso, double multaDiaria) {
        return precioBase + (diasRetraso * multaDiaria); 
    }
    
    public ArrayList <ReporteFila> obtenerHistorialGeneral () {
        ArrayList <ReporteFila> lista = new ArrayList<>();
        // Consulta
        String sql = "SELECT p.id_prestamo, dp.id_ejemplar, p.dni, p.fecha_prestamo, p.fecha_devolucion, p.fecha_vencimiento, "
           + "c.nombres, l.titulo, cat.costo_mora AS precio_base, dp.costo_mora AS multa_diaria "
           + "FROM detalle_prestamo dp "
           + "JOIN prestamos p ON dp.id_prestamo = p.id_prestamo "
           + "JOIN clientes c ON p.dni = c.dni "
           + "JOIN ejemplares e ON dp.id_ejemplar = e.id_ejemplar "
           + "JOIN libros l ON e.id_libro = l.id_libro "
           + "JOIN categorias cat ON l.id_categoria = cat.id_categoria "
           + "WHERE p.fecha_devolucion IS NOT NULL"
           + " ORDER BY p.id_prestamo ASC";
        
        try {
            //Conetctar la clase "ConnectMySql"
            Connection cn = ConnectMySQL.conn();
            // Preparamos la orden
            PreparedStatement pst = cn.prepareStatement(sql);
            // Traemos los datos de XAMPP
            ResultSet rs = pst.executeQuery(); 
            //Formato de fecha
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            
            while (rs.next()) {                
             // 1. Reconstruimos los objetos base necesarios paso a paso
                Cliente cliente = new Cliente();
                cliente.setDni(rs.getString("dni"));
                
                Prestamo prestamo = new Prestamo();
                // Asignamos datos usando conversión limpia a LocalDate
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
                prestamo.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                prestamo.setCliente(cliente);
                
                Libro libro = new Libro();
                libro.setTitulo(rs.getString("titulo"));
                
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setLibro(libro);

                DetallePrestamo detalle = new DetallePrestamo();
                detalle.setEjemplar(ejemplar);
                detalle.setPrecioPrestamoAplicado(rs.getDouble("precio_base"));
                
                // 2. Cálculos financieros
                java.sql.Date fechaVenc = rs.getDate("fecha_vencimiento");
                java.sql.Date fechaDevol = rs.getDate("fecha_devolucion");
                long diasRetraso = calcularDiasRetraso(fechaVenc, fechaDevol);
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
    
    public ArrayList <ReporteFila> obtenerHistorialFiltrado (java.util.Date de, java.util.Date hasta){
       ArrayList <ReporteFila> lista = new ArrayList<>();
       // Consulta
        String sql = "SELECT p.id_prestamo, dp.id_ejemplar, p.dni, p.fecha_prestamo, p.fecha_devolucion, p.fecha_vencimiento, "
           + "c.nombres, l.titulo, cat.costo_mora AS precio_base, dp.costo_mora AS multa_diaria "
           + "FROM detalle_prestamo dp "
           + "JOIN prestamos p ON dp.id_prestamo = p.id_prestamo "
           + "JOIN clientes c ON p.dni = c.dni "
           + "JOIN ejemplares e ON dp.id_ejemplar = e.id_ejemplar "
           + "JOIN libros l ON e.id_libro = l.id_libro "
           + "JOIN categorias cat ON l.id_categoria = cat.id_categoria "
           + "WHERE p.fecha_prestamo BETWEEN ? AND ? " 
           + "AND p.fecha_devolucion IS NOT NULL"
           + " ORDER BY p.id_prestamo ASC";
        
        try {
            //Conetctar la clase "ConnectMySql"
            Connection cn = ConnectMySQL.conn();
            // Preparamos la orden
            PreparedStatement pst = cn.prepareStatement(sql);
            
            //Convertimos las fechas
            java.sql.Date fechaInicioSql = new java.sql.Date(de.getTime());
            java.sql.Date fechaFinSql = new java.sql.Date(hasta.getTime());
            
            //Asignamos las fechas en orden
            pst.setDate(1, fechaInicioSql);
            pst.setDate(2, fechaFinSql);
            
            // Traemos los datos de XAMPP
            ResultSet rs = pst.executeQuery(); 
            //Formato de fecha
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            
            while (rs.next()) {                
             // 1. Reconstruimos los objetos base necesarios paso a paso
                Cliente cliente = new Cliente();
                cliente.setDni(rs.getString("dni")); 
                
                Prestamo prestamo = new Prestamo();
                // Asignamos datos usando conversión limpia a LocalDate
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion").toLocalDate());
                prestamo.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
                prestamo.setCliente(cliente);

                Libro libro = new Libro();
                libro.setTitulo(rs.getString("titulo"));
                
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setIdEjemplar(rs.getInt("id_ejemplar"));
                ejemplar.setLibro(libro);

                DetallePrestamo detalle = new DetallePrestamo();
                detalle.setEjemplar(ejemplar);
                detalle.setPrecioPrestamoAplicado(rs.getDouble("precio_base"));
                
                // 2. Cálculos financieros
                java.sql.Date fechaVenc = rs.getDate("fecha_vencimiento");
                java.sql.Date fechaDevol = rs.getDate("fecha_devolucion");
             
             //le pasamos el vencimiento y la devolucion 
             long diasRestraso = calcularDiasRetraso(fechaVenc, fechaDevol);
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
    
}
