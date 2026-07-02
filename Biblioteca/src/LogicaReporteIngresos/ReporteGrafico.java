
package LogicaReporteIngresos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


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
    
    public ChartPanel obtenerGraficoPie (DefaultTableModel modeloTabla) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        HashMap <String, Integer> conteoLibros = new HashMap<>();
        
        int filas = modeloTabla.getRowCount();
        for (int i = 0; i <filas; i++) {
            String libro = (String) modeloTabla.getValueAt(i, 5); //Columna libro
            conteoLibros.put(libro, conteoLibros.getOrDefault(libro, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : conteoLibros.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
                "Porcentaje de Demanda de Libros ", 
                dataset, 
                true, 
                true, 
                false);
        
        return new ChartPanel(chart);
    }
    
}
