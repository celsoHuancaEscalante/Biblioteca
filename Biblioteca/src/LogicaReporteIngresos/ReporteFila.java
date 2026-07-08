
package LogicaReporteIngresos;
import ClaseBase.DetallePrestamo;
import ClaseBase.Prestamo;
import java.time.format.DateTimeFormatter;


public class ReporteFila {
    private Prestamo prestamo;
    private DetallePrestamo detalle;
    private long diasRetraso;
    private double total;

    public ReporteFila(Prestamo prestamo, DetallePrestamo detalle, long diasRetraso, double total) {
        this.prestamo = prestamo;
        this.detalle = detalle;
        this.diasRetraso = diasRetraso;
        this.total = total;
    }

    private static final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public Prestamo getPrestamo() {
        return prestamo;
    }

    public DetallePrestamo getDetalle() {
        return detalle;
    }

    public long getDiasRetraso() {
        return diasRetraso;
    }

    public double getTotal() {
        return total;
    }
    
    //Método puente para obtener directamente sin tener que hacer getPrestamo().getIdPrestamo().
    
    public int getIdPrestamo() {
        return prestamo.getIdPrestamo();
    }

    // Llamada encadenada: Accede a 'detalle', de ahí al 'Ejemplar' y finalmente retorna su identificador numérico
    public int getIdejemplar() {
        return detalle.getEjemplar().getIdEjemplar();
    }

    public String getFechaPrestamo() {
        // Conducion si la fecha del prestamo es diferente a null, transforma la fecha y retorna como un string
        if (prestamo.getFechaPrestamo() != null) {
            return prestamo.getFechaPrestamo().format(formateador);
        }
        return "";
    }

    public String getFechaDevolucion() {
        // Conducion si la fecha de devolucion es diferente a null, transforma la fecha y retorna como un string
        if (prestamo.getFechaDevolucion()!= null) {
            return prestamo.getFechaDevolucion().format(formateador);
        }
        return "";
    }

    public String getUsuario() {
        // Navegación jerárquica: Del préstamo viaja al objeto 'Cliente' y de ahí extrae y retorna el String del DNI
        return prestamo.getCliente().getDni(); 
    }

    public String getLibro() {
        // Navegación profunda: Del detalle va al ejemplar, del ejemplar al libro base, y retorna su título en texto
        return detalle.getEjemplar().getLibro().getTitulo();
    }

    public double getCosto() {
        return detalle.getPrecioPrestamoAplicado();
    }
    
}
