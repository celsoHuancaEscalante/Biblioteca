package ClaseBase;

import java.time.LocalDate;
import java.util.LinkedList;

public class Prestamo {
    private int idPrestamo;
    private Cliente cliente;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private LocalDate fechaVencimiento;
    private LinkedList<DetallePrestamo> detalles;
}
