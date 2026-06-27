package ClaseBase;

public class Ejemplar implements Mostrar{
    private int idEjemplar;
    private Libro libro;
    private String estado;

    public Ejemplar(int idEjemplar, Libro libro, String estado) {
        this.idEjemplar = idEjemplar;
        this.libro = libro;
        this.estado = estado;
    }

    public int getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(int idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String mostrarDatos() {
        return "ID: "+idEjemplar+
                "\n"+"Libro: "+libro+
                "\n"+"Estado: "+estado;
    }
    
}
