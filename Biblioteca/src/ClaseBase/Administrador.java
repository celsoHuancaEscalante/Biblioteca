
package ClaseBase;

public class Administrador extends Usuario{

    public Administrador(String usuario, String contrasena) {
        super(usuario, contrasena);
    }
  
    public String mostrarMensaje(){
        return "Bienvenido Bibliotecario";
    }
}
