
package LogicaReporteIngresos;

import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;


public class ReporteGrafico {

    public ReporteGrafico() {
    }
    
    public ChartPanel obtenerGraficoBarras (DefaultTableModel modeloTabla) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        HashMap <String, Double> ingresosPorLibro = new HashMap<>();
        
        int filas = modeloTabla.getRowCount();
        for (int i = 0; i < filas; i++) {
            String libro = (String) modeloTabla.getValueAt(i, 5); //Columna libro
            double totalFila = (double) modeloTabla.getValueAt(i, 8); //Columna Total
            ingresosPorLibro.put(libro, ingresosPorLibro.getOrDefault(libro, 0.0) + totalFila);
        }
        
        for (Map.Entry<String, Double> entry : ingresosPorLibro.entrySet()) {
            dataset.setValue(entry.getValue(), "Ingresos (S/)", entry.getKey());
        }
        
        JFreeChart chart = ChartFactory.createBarChart3D(
                "Ingresos Totales por Libro", 
                "Libros", 
                "Monto Acumulado (S/)", 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, 
                true, 
                false);
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
