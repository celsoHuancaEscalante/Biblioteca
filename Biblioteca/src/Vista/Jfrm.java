/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import Datos.*;
import ClaseBase.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class Jfrm extends javax.swing.JFrame {
    
    private LibroAD libroAD = new LibroAD();
    private GeneroAD generoAD = new GeneroAD();
    private AutorAD autorAD = new AutorAD();
    private EditorialAD editorialAD = new EditorialAD();
    private EjemplarAD ejemplarAD = new EjemplarAD();
    private DefaultTableModel modeloTabla;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Jfrm.class.getName());

    /**
     * Creates new form Jfrm
     */
    public Jfrm() {
        
        initComponents();
    
    // Inicializar tabla
    inicializarTabla();
    
    // Cargar ComboBoxes
    cargarComboBoxes();
    
    // Cargar tabla
    cargarTabla();
    
    // Agregar KeyListener al txtBuscarLibro (autocompletado)
    txtBuscarPorTitulo.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            buscarEnTiempo();
        }
    });
        
    }
    
    private void inicializarTabla() {
    modeloTabla = new DefaultTableModel(
        new String[]{"ID", "Título", "Autor", "Editorial", "Género", "Unidades", "Estado"},
        0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tblLibros.setModel(modeloTabla);
    tblLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}

    private void cargarComboBoxes() {
    // Limpiar ComboBoxes
    cboAutores.removeAllItems();
    cboGenero.removeAllItems();
    
    // Agregar opción "Todos"
    cboAutores.addItem("Todos");
    cboGenero.addItem("Todos");
    
    // Cargar Autores
    for (Autor a : autorAD.obtenerTodos()) {
        cboAutores.addItem(a.toString());
    }
    
    // Cargar Géneros
    for (Genero g : generoAD.obtenerTodos()) {
        cboGenero.addItem(g.getNombre());
    }
}
    
    private void cargarTabla() {
    modeloTabla.setRowCount(0);
    List<Libro> libros = libroAD.obtenerTodos();
    
    for (Libro libro : libros) {
        int disponibles = ejemplarAD.contarDisponibles(libro.getIdLibro());
        int noDisponibles = libro.getStock() - disponibles;
        
        String estado = disponibles > 0 ? "Disponible" : "No Disponible";
        
        modeloTabla.addRow(new Object[]{
            libro.getIdLibro(),
            libro.getTitulo(),
            libro.getAutor().toString(),
            libro.getEditorial().getNombre(),
            libro.getGenero().getNombre(),
            libro.getStock(),
            estado
        });
    }
    
    int totalEjemplares = calcularTotalEjemplares(libros);
    // Actualizar label de total
    lblTotalLibros.setText("" + totalEjemplares);
}
    
    private void buscarEnTiempo() {
    String titulo = txtBuscarPorTitulo.getText();
    
    if (titulo.isEmpty()) {
        cargarTabla();
        return;
    }
    
    modeloTabla.setRowCount(0);
    List<Libro> libros = libroAD.buscarPorTitulo(titulo);
    
    for (Libro libro : libros) {
        int disponibles = ejemplarAD.contarDisponibles(libro.getIdLibro());
        String estado = disponibles > 0 ? "Disponible" : "No Disponible";
        
        modeloTabla.addRow(new Object[]{
            libro.getIdLibro(),
            libro.getTitulo(),
            libro.getAutor().toString(),
            libro.getEditorial().getNombre(),
            libro.getGenero().getNombre(),
            libro.getStock(),
            estado
        });
    }
    
    lblTotalLibros.setText("" + libros.size());
}
    
    public void actualizarTablaDesdeDialog() {
    cargarComboBoxes();
    cargarTabla();
}
    
    private int calcularTotalEjemplares(List<Libro> libros) {
    int total = 0;
    for (Libro libro : libros) {
        total += libro.getStock();
    }
    return total;
}
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtBuscarPorTitulo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cboGenero = new javax.swing.JComboBox<>();
        cboAutores = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnAgregarLibro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLibros = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblTotalLibros = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Buscar:");

        cboGenero.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aventura", "Ciencia Ficción", "Dramático", "Fantasía", "Lírico", "Misterio", "Narrativo", "Poesía ", "Romántico", "Terror" }));
        cboGenero.addActionListener(this::cboGeneroActionPerformed);

        cboAutores.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Autores" }));

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        btnAgregarLibro.setText("Agregar libro");
        btnAgregarLibro.addActionListener(this::btnAgregarLibroActionPerformed);

        tblLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Título", "Autor", "Editorial", "Género", "Unidades", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tblLibros);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setText("Total de libros");

        lblTotalLibros.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(lblTotalLibros, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalLibros, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Género");

        jLabel5.setText("Autor");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtBuscarPorTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(165, 165, 165))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnBuscar)
                        .addGap(65, 65, 65)
                        .addComponent(btnLimpiar)
                        .addGap(113, 113, 113))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAgregarLibro)
                        .addGap(42, 42, 42))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(128, 128, 128)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cboGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(79, 79, 79)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(122, 122, 122)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(txtBuscarPorTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(cboAutores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnLimpiar)
                                    .addComponent(btnBuscar)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(85, 85, 85))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(51, 51, 51)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnAgregarLibro)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarLibroActionPerformed
        new JDialog(this);
    }//GEN-LAST:event_btnAgregarLibroActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        
        String generoSeleccionado = (String) cboGenero.getSelectedItem();
        String autorSeleccionado = (String) cboAutores.getSelectedItem();
    
        Genero genero = null;
        Autor autor = null;
    
    // Buscar Género
        if (!generoSeleccionado.equals("Todos")) {
            for (Genero g : generoAD.obtenerTodos()) {
                if (g.getNombre().equals(generoSeleccionado)) {
                    genero = g;
                    break;
                }
            }
        }
    
    // Buscar Autor
        if (!autorSeleccionado.equals("Todos")) {
            for (Autor a : autorAD.obtenerTodos()) {
                if (a.toString().equals(autorSeleccionado)) {
                    autor = a;
                    break;
                }
            }
        }
    
    // Filtrar libros
        modeloTabla.setRowCount(0);
        List<Libro> libros = libroAD.filtrar(genero, autor);
    
        for (Libro libro : libros) {
            int disponibles = ejemplarAD.contarDisponibles(libro.getIdLibro());
            String estado = disponibles > 0 ? "Disponible" : "No Disponible";
        
            modeloTabla.addRow(new Object[]{
                libro.getIdLibro(),
                libro.getTitulo(),
                libro.getAutor().toString(),
                libro.getEditorial().getNombre(),
                libro.getGenero().getNombre(),
                libro.getStock(),
                estado
            });
        }
    
        lblTotalLibros.setText("" + libros.size());
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        
        txtBuscarPorTitulo.setText("");
        cboAutores.setSelectedIndex(0);
        cboGenero.setSelectedIndex(0);
        cargarTabla();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void cboGeneroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGeneroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboGeneroActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Jfrm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarLibro;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cboAutores;
    private javax.swing.JComboBox<String> cboGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalLibros;
    private javax.swing.JTable tblLibros;
    private javax.swing.JTextField txtBuscarPorTitulo;
    // End of variables declaration//GEN-END:variables
}
