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

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LinkedList<DetallePrestamo> getDetalles() {
        return detalles;
    }

    public void setDetalles(LinkedList<DetallePrestamo> detalles) {
        this.detalles = detalles;
    }
    
    
}
