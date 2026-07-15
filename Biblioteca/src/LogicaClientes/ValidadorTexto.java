package LogicaClientes;
//importa librería para describir patrones de texto

import java.util.regex.Pattern;

//Clase con reglas de validación y formateo de texto para módulo Clientes
public class ValidadorTexto {

    private static final Pattern SOLO_LETRAS
            = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+( [A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$");
    private static final Pattern CORREO
            = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    //Método de validación de formato nombre y Apellido: solo letras, sin numeros/simbolos, sin espacio dobles/no vacío. */
    public static boolean esNombreValido(String texto) {
        if (texto == null) {
            return false;
        }
        return !texto.isEmpty()
                && texto.equals(texto.trim())
                && !texto.contains("  ")
                && SOLO_LETRAS.matcher(texto).matches();
    }

    //Método que capitaliza cada palabra
    public static String capitalizar(String texto) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }
        String[] palabras = texto.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String p : palabras) {
            if (p.isEmpty()) {
                continue;
            }
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    //Método que valida formato de correo tenga "@" y "." , sin espacios, no vacío
    public static boolean esCorreoValido(String correo) {
        return correo != null && !correo.isBlank()
                && !correo.contains(" ")
                && CORREO.matcher(correo.trim()).matches();
    }

    //Método para validar DNI(8 digitos) o CE(9 digitos), solo numérico, sin espacios
    public static boolean esDniValido(String dni, boolean esExtranjero) {
        if (dni == null) {
            return false;
        }
        String d = dni.trim();
        if (d.isEmpty() || d.contains(" ") || !d.equals(dni)) {
            return false;
        }
        if (!d.chars().allMatch(Character::isDigit)) {
            return false;
        }
        return esExtranjero ? d.length() == 9 : d.length() == 8;
    }
    
    //Detecta posibles errores de tipeo (dígitos repetidos o secuencias consecutivas). Confirma con usuario antes de guardar los datos.
    public static boolean pareceTypo(String dni) {
        if (dni == null || dni.length() < 4) {
            return false;
        }
        boolean todosIguales = dni.chars().distinct().count() == 1;
        boolean secuencial = true;
        for (int i = 1; i < dni.length(); i++) {
            if (dni.charAt(i) - dni.charAt(i - 1) != 1) {
                secuencial = false;
                break;
            }
        }
        return todosIguales || secuencial;
    }
    
    // Número local peruano: exactamente 9 dígitos y empieza en 9
    public static boolean esNumeroPeruValido(String numero) {
        return numero != null && numero.matches("9\\d{8}");
    }
 
    // Código de país extranjero: "+" seguido de al menos 1 dígito, sin espacios
    public static boolean esCodigoPaisExtranjeroValido(String codigo) {
        return codigo != null && codigo.matches("\\+\\d+");
    }
 
    // Número extranjero: solo dígitos, no vacío, sin espacios
    public static boolean esNumeroExtranjeroValido(String numero) {
        return numero != null && !numero.isBlank() && numero.matches("\\d+");
    }
 
}
