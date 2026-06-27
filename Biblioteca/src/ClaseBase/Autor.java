
package ClaseBase;

public class Autor implements Mostrar{
    
    private int idAutor;
    private String nombreApellido;

    public Autor(int idAutor, String nombreApellido) {
        this.idAutor = idAutor;
        this.nombreApellido = nombreApellido;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public String getNombreApellido() {
        return nombreApellido;
    }

    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    @Override
    public String mostrarDatos() {
        return nombreApellido;
    }
    
    
    
}
