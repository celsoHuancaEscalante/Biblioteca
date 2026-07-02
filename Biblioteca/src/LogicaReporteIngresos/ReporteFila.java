
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
    
    //metodos puente
    public int getIdPrestamo() {
        return prestamo.getIdPrestamo();
    }

    public int getIdejemplar() {
        return detalle.getEjemplar().getIdEjemplar();
    }

    public String getFechaPrestamo() {
        if (prestamo.getFechaPrestamo() != null) {
            return prestamo.getFechaPrestamo().format(formateador);
        }
        return "";
    }

    public String getFechaDevolucion() {
        if (prestamo.getFechaDevolucion()!= null) {
            return prestamo.getFechaDevolucion().format(formateador);
        }
        return "";
    }

    public String getUsuario() {
        return prestamo.getCliente().getDni(); 
    }

    public String getLibro() {
        return detalle.getEjemplar().getLibro().getTitulo();
    }

    public double getCosto() {
        return detalle.getPrecioPrestamoAplicado();
    }
    
}
