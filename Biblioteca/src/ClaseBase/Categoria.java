
package ClaseBase;

public class Categoria implements Mostrar{
    
    private int idCategoria;
    private String nombre;
    private double precioPrestamo;
    private int diasMaximos;
    private double multaPorDia;
    private double garantia;

    public Categoria(int idCategoria, String nombre, double precioPrestamo, int diasMaximos, double multaPorDia, double garantia) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.precioPrestamo = precioPrestamo;
        this.diasMaximos = diasMaximos;
        this.multaPorDia = multaPorDia;
        this.garantia = garantia;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecioPrestamo() {
        return precioPrestamo;
    }

    public void setPrecioPrestamo(double precioPrestamo) {
        this.precioPrestamo = precioPrestamo;
    }

    public int getDiasMaximos() {
        return diasMaximos;
    }

    public void setDiasMaximos(int diasMaximos) {
        this.diasMaximos = diasMaximos;
    }

    public double getMultaPorDia() {
        return multaPorDia;
    }

    public void setMultaPorDia(double multaPorDia) {
        this.multaPorDia = multaPorDia;
    }

    public double getGarantia() {
        return garantia;
    }

    public void setGarantia(double garantia) {
        this.garantia = garantia;
    }

    @Override
    public String mostrarDatos() {
       return nombre; 
    }
    
    
    
}
