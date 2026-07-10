
package LogicaDaniel;

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
    
    public ChartPanel obtenerGraficoBarras (ArrayList<ReporteFila>listaFilas) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        HashMap <String, Integer> conteoGeneros = new HashMap<>();
        
        for (ReporteFila r : listaFilas) {
            if (r.getDetalle() != null &&
                    r.getDetalle().getEjemplar() !=null &&
                    r.getDetalle().getEjemplar().getLibro() != null &&
                    r.getDetalle().getEjemplar().getLibro().getGenero() !=null) {
                
                String nombreGenero = r.getDetalle().getEjemplar().getLibro().getGenero().getNombre();
                
                if (nombreGenero != null && !nombreGenero.isEmpty()) {
                    conteoGeneros.put(nombreGenero, conteoGeneros.getOrDefault(nombreGenero, 0) + 1);
                }
            }
        }
        
        for (Map.Entry<String,Integer> entry: conteoGeneros.entrySet()) {
            dataset.setValue(entry.getValue(), "Cantidad de Prestamos", entry.getKey());
        }
        JFreeChart chart = ChartFactory.createBarChart3D(
                "Generos Literarios", 
                "Generos", 
                "Canitdad de Prestamos", 
                dataset, 
                PlotOrientation.VERTICAL, 
                false, 
                true, 
                false);
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return new ChartPanel(chart);
    }
    
    public ChartPanel obtenerGraficoLineas (ArrayList<ReporteFila> listaFilas, String mesSeleccionado, String anioSeleccionado) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int anioTarget = Integer.parseInt(anioSeleccionado);
        
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
        
        boolean porDias = !mesSeleccionado.equals("Todos");
        if (porDias) {
            //Logica por dias: Inicializamos los 31 dias en 0
            HashMap <Integer, Double> ganaciasPorDia = new HashMap<>();
            for (int d = 1; d <= 31; d++) ganaciasPorDia.put(d, 0.0);
            int mesTarget = 0;
            for (int i = 0; i < nombreMeses.length; i++) {
                if (nombreMeses[i].equals(mesSeleccionado)) {
                    mesTarget = i + 1;
                    break;
                }
            }
            
            // Acumular dinero de la base de datos por día
            for (ReporteFila r : listaFilas) {
                LocalDate fecha = r.getPrestamo().getFechaPrestamo();
                if (fecha != null && fecha.getYear() == anioTarget && fecha.getMonthValue() == mesTarget) {
                    int dia = fecha.getDayOfMonth();
                    ganaciasPorDia.put(dia, ganaciasPorDia.get(dia) + r.getTotal());
                }
            }
            
            //Alineamos los data set al grafico
            for (int d = 1; d <= 31; d++) {
                dataset.addValue(ganaciasPorDia.get(d), "Ganacias (S/)", String.valueOf(d));
            }
            
        } else {
            //Logica por Meses: Inicializamos los 12 meses en 0
            HashMap<Integer, Double> gananciasPorMes = new HashMap<>();
            for (int m = 1; m <= 12; m++) gananciasPorMes.put(m, 0.0);
            
            // Acumular dinero de la base de datos por día
            for (ReporteFila r : listaFilas) {
                LocalDate fecha = r.getPrestamo().getFechaPrestamo();
                if (fecha != null && fecha.getYear() == anioTarget) {
                    int mes = fecha.getMonthValue();
                    gananciasPorMes.put(mes, gananciasPorMes.get(mes) + r.getTotal());
                }
            }
            
            //Alineamos los dataset para el grafico
            for (int m = 1; m <= 12; m++) {
                dataset.addValue(gananciasPorMes.get(m), "Ganancias (S/)", nombreMeses[m - 1]);
            }
        }
        String tituloEjeX = porDias ? "Dias de " + mesSeleccionado : "Meses del Año " + anioSeleccionado;
        
        JFreeChart chart = ChartFactory.createLineChart("", 
                tituloEjeX, "Monto Acumulado (S/)", 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false);
        
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        //rotar los numeros a 45 grados
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        
        
        return new ChartPanel(chart);
    }
    
}
