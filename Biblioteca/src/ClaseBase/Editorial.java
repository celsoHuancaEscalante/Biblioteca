package ClaseBase;

public class Editorial implements Mostrar{
    private int idEditorial;
    private String nombre;

    public Editorial(int idEditorial, String nombre) {
        this.idEditorial = idEditorial;
        this.nombre = nombre;
    }

    public int getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(int idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String mostrarDatos() {
        return nombre;
    }
    
    
    
}
