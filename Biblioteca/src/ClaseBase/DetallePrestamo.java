package ClaseBase;

public class DetallePrestamo {
    private int idDetalle;
    private Ejemplar ejemplar;
    private Prestamo prestamo;
    private double precioPrestamoAplicado;
    private double garantiaAplicada;
    private double multaPorDiaAplicada;

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public double getPrecioPrestamoAplicado() {
        return precioPrestamoAplicado;
    }

    public void setPrecioPrestamoAplicado(double precioPrestamoAplicado) {
        this.precioPrestamoAplicado = precioPrestamoAplicado;
    }

    public double getGarantiaAplicada() {
        return garantiaAplicada;
    }

    public void setGarantiaAplicada(double garantiaAplicada) {
        this.garantiaAplicada = garantiaAplicada;
    }

    public double getMultaPorDiaAplicada() {
        return multaPorDiaAplicada;
    }

    public void setMultaPorDiaAplicada(double multaPorDiaAplicada) {
        this.multaPorDiaAplicada = multaPorDiaAplicada;
    }
    
    
}
