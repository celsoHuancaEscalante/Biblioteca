package ClaseBase;

public abstract class Usuario {

    String usuario;
    String contrasena;

    public Usuario(String usuario, String contrasena) {
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public boolean validarSesion(String user, String contra) {
        if (usuario.equals(user) && contrasena.equals(contra)) {
            return true;
        } else {
            return false;
        }
    }
}
