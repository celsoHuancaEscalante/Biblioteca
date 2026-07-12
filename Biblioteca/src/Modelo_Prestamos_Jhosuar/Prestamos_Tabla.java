package Modelo_Prestamos_Jhosuar;

import java.time.LocalDate;

public class Prestamos_Tabla {

    private int idPrestamo;
    private String dni;
    private String usuario;
    private String libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaEntrega;
    private String estadoPrestamo;
    private double multaAcumulada;

    public Prestamos_Tabla() {}

    public Prestamos_Tabla(int idPrestamo, String dni, String usuario, String libro,
                         LocalDate fechaPrestamo, LocalDate fechaEntrega,
                         String estadoPrestamo, double multaAcumulada) {
        this.idPrestamo = idPrestamo;
        this.dni = dni;
        this.usuario = usuario;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaEntrega = fechaEntrega;
        this.estadoPrestamo = estadoPrestamo;
        this.multaAcumulada = multaAcumulada; }

    
    
    
    public int getIdPrestamo() { return idPrestamo; }

    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public String getDni() { return dni; }

    public void setDni(String dni) { this.dni = dni; }

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getLibro() { return libro; }

    public void setLibro(String libro) { this.libro = libro; }

    public LocalDate getFechaPrestamo() { return fechaPrestamo; }

    public void setFechaPrestamo(LocalDate fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }

    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstadoPrestamo() { return estadoPrestamo; }

    public void setEstadoPrestamo(String estadoPrestamo) { this.estadoPrestamo = estadoPrestamo; }

    public double getMultaAcumulada() { return multaAcumulada; }

    public void setMultaAcumulada(double multaAcumulada) { this.multaAcumulada = multaAcumulada; }}

