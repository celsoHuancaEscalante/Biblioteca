
package LogicaDaniel; //logica daniel

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;  
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class ReporteGrafico {

    public ReporteGrafico() {
    }
    
    /*
      Método que analiza el historial de préstamos y fabrica un gráfico de barras 
      Un objeto ChartPanel listo para ser inyectado directamente en el JPanel gris de la interfaz.
     */
    public ChartPanel obtenerGraficoBarras (ArrayList<ReporteFila>listaFilas) {
        // Instanciamiento de la estructura de datos que JFreeChart requiere para el gráfico de barras
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // Instanciamiento de un mapa asociativo en blanco para contar cuántas veces se repite cada género
        HashMap <String, Integer> conteoGeneros = new HashMap<>();
        
        //Bucle que recorre celda por celda sobre el listado de filas
        for (ReporteFila r : listaFilas) {
            //
            if (r.getDetalle() != null &&
                    r.getDetalle().getEjemplar() !=null &&
                    r.getDetalle().getEjemplar().getLibro() != null &&
                    r.getDetalle().getEjemplar().getLibro().getGenero() !=null) {
                
                // Extrae el nombre del género navegando por la jerarquía de POO
                String nombreGenero = r.getDetalle().getEjemplar().getLibro().getGenero().getNombre();
                //Evalúa que la cadena de texto recuperada contenga un nombre real y válido
              if (nombreGenero != null && !nombreGenero.isEmpty()) {
                  // Suma un préstamo al género actual
                    conteoGeneros.put(nombreGenero, conteoGeneros.getOrDefault(nombreGenero, 0) + 1);
                }  
            }
        }
        // Bucle que recorre el mapa asociativo 
        for (Map.Entry<String,Integer> entry: conteoGeneros.entrySet()) {
            // Inyecta los valores acumulados en el dataset de JFreeChart 
            dataset.setValue(entry.getValue(), "Cantidad de Prestamos", entry.getKey());
        }
        // Creacion de objeto con la fabrica de graficos
        JFreeChart chart = ChartFactory.createBarChart3D(
                "Generos Literarios", 
                "Generos", 
                "Canitdad de Prestamos", 
                dataset, // El origen  de datos 
                PlotOrientation.VERTICAL, 
                false, //Indica que no queremos mostrar la leyenda de series
                true, //Activa las herramientas de ayuda flotantes
                false); //Desactiva la generacion automatica de url
        
        // Recupera la zona de trazado del gráfico y la transforma a su subtipo CategoryPlot
        CategoryPlot plot = chart.getCategoryPlot();
        // Extrae el eje vertical (Y) y lo convierte a NumberAxis para poder modificar su comportamiento numérico
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        // Forza al eje vertical a usar números enteros 
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //Rotar numero a 45 grados
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        
        // Envia el grafico envuelto dentro de un contenedor swing
        return new ChartPanel(chart);
    }
    
    /*
      Método público avanzado que calcula la evolución temporal de ingresos y genera un gráfico de líneas dinámico
      Un ChartPanel con las líneas de tendencia de ganancias.
     */
    public ChartPanel obtenerGraficoLineas (ArrayList<ReporteFila> listaFilas, String mesSeleccionado, String anioSeleccionado) {
        // Instanciamiento de la estructura de datos que JFreechar requiere para el grafico
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        int anioTarget = Integer.parseInt(anioSeleccionado);
        
        // Arreglo de los meses
        String [] nombreMeses = {
            "Enero", 
            "Febrero", 
            "Marzo", 
            "Abril", 
            "Mayo", 
            "Junio", 
            "Julio", 
            "Agosto", 
            "Septiembre", 
            "Octubre", 
            "Noviembre", 
            "Diciembre"};
        
        //Evalúa mediante lógica inversa si el usuario quiere ver los días de un mes o los meses de un año
        boolean porDias = !mesSeleccionado.equals("Todos");
        
        // Si 'porDias' es verdadero, se activa el zoom analitico del panel
        if (porDias) {
            // Instancia de mapa en blanco
            HashMap <Integer, Double> ganaciasPorDia = new HashMap<>();
            // Llena el mapa desde eñ dia 1 al 31 inicializando su valor inicial de 0
            for (int d = 1; d <= 31; d++) ganaciasPorDia.put(d, 0.0);
            // inicializa el contador
            int mesTarget = 0;
            // Recorre el arreglo de nombres de meses para averiguar el mes seleccionado
            for (int i = 0; i < nombreMeses.length; i++) {
                //Evalua si el texto de la casilla coincide con el combobox
                if (nombreMeses[i].equals(mesSeleccionado)) {
                    mesTarget = i + 1;
                    break;
                }
            }
            
            // Recorre la coleccion de registros financieros
            for (ReporteFila r : listaFilas) {
                //Captura la fecha del prestamo
                LocalDate fecha = r.getPrestamo().getFechaPrestamo();
                // Validacion que la fecha exista, pertenezca al año y mes
                if (fecha != null && fecha.getYear() == anioTarget && fecha.getMonthValue() == mesTarget) {
                    // Extrae el numero del dia del mes que se hizo la transaccion 
                    int dia = fecha.getDayOfMonth();
                    // Suma el dinero total ganado en esa fila al acumulado
                    ganaciasPorDia.put(dia, ganaciasPorDia.get(dia) + r.getTotal());
                }
            }
            
            // Recorre los 31 dias ya procesados 
            for (int d = 1; d <= 31; d++) {
                // Carga los datos acumulados para el data set final
                dataset.addValue(ganaciasPorDia.get(d), "Ganacias (S/)", String.valueOf(d));
            }
            
        } else {
            //Instanciamiento de un mapa en blanco
            HashMap<Integer, Double> gananciasPorMes = new HashMap<>();
            // Llena el mapa con los 12 meses inicializando su valor incial en 0
            for (int m = 1; m <= 12; m++) gananciasPorMes.put(m, 0.0);
            
            // Recorre todas las filas 
            for (ReporteFila r : listaFilas) {
                // Extrae la fecha de la fila
                LocalDate fecha = r.getPrestamo().getFechaPrestamo();
                // Validacion que la fecha exista y pertenzca al año consultado
                if (fecha != null && fecha.getYear() == anioTarget) {
                    // Extrae el mes que se hizo la transaccion
                    int mes = fecha.getMonthValue();
                    //Suma del dinero total del mes correspondiente
                    gananciasPorMes.put(mes, gananciasPorMes.get(mes) + r.getTotal());
                }
            }
            
            //Recorre los 12 meses procesados en el mapa
            for (int m = 1; m <= 12; m++) {
                //Carga los datos acumulados
                dataset.addValue(gananciasPorMes.get(m), "Ganancias (S/)", nombreMeses[m - 1]);
            }
        }
        //Define el titulo horizontal
        String tituloEjeX = porDias ? "Dias de " + mesSeleccionado : "Meses del Año " + anioSeleccionado;
        
        //Creacion del objeto con la fabrica de graficos
        JFreeChart chart = ChartFactory.createLineChart("", 
                tituloEjeX, "Monto Acumulado (S/)", 
                dataset, // El origen  de datos 
                PlotOrientation.VERTICAL, 
                true,   // Muestra la pequeña leyenda de series de color abajo ("Ganancias (S/)")
                true, // Activa las etiquetas de ayuda flotantes para el usuario
                false); // Indica que no queremos mapear enlaces de internet interactivos
        
        // Recupera la zona de trazado del gráfico y la transforma a su subtipo CategoryPlot
        CategoryPlot plot = chart.getCategoryPlot();
        // Recupera el eje del rango de dinero
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        // Fuerza al eje a usar numeros enteros
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        //rotar los numeros a 45 grados
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        // Se envia lo envuelto dentro del panel swing de graficos
        return new ChartPanel(chart);
    }
    
}

