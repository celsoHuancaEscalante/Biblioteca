package ClaseBase;

public class Libro implements Mostrar{
   private int idLibro;
   private Genero genero;
   private Autor autor;
   private Editorial editorial;
   private String titulo;
   private int stock;
   private int anioPublicacion;

    public Libro(int idLibro, Genero genero, Autor autor, Editorial editorial, String titulo, int stock, int anioPublicacion) {
        this.idLibro = idLibro;
        this.genero = genero;
        this.autor = autor;
        this.editorial = editorial;
        this.titulo = titulo;
        this.stock = stock;
        this.anioPublicacion = anioPublicacion;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    @Override
    public String mostrarDatos() {
        return titulo;
    }
   
   
   
}
