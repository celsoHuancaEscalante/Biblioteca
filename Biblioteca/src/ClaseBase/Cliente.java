package ClaseBase;

import java.time.LocalDate;

public class Cliente {
    private String dni;
    private String primerNombre;
    private String primerApellido;
    private String telefono;
    private String correo;
    private LocalDate fechaRegistro;

    public Cliente(String dni, String primerNombre, String primerApellido, String telefono, String correo) {
        this.dni = dni;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = LocalDate.now();
    }

    public String getDni() {
        return dni;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
}
