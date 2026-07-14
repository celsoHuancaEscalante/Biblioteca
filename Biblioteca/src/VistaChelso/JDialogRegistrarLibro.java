/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package VistaChelso;

import ClaseBase.*;
import LogicaChelso.LogicaRegistrar;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Raul
 */
public class JDialogRegistrarLibro extends javax.swing.JDialog {
    
    private LogicaRegistrar logica = new LogicaRegistrar();
    private DefaultTableModel modeloTabla;
    private JfrmBuscarLibro jfrm;
    private Libro libroActual = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JDialogRegistrarLibro.class.getName());

    /**
     * Creates new form JDialog
     */
    public JDialogRegistrarLibro(JfrmBuscarLibro jfrm) {
        super(jfrm, "Registro de Libros", true);
        this.jfrm = jfrm;
    
        initComponents();
        inicializarTabla();
        cargarGenerosEnComboBox();
        cargarTabla();
    
        // Agregar doble clic en tabla para eliminar
        tblLibros.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    eliminarLibroDesdeTabla();
                }
            }
        });
    
        
    
        setLocationRelativeTo(jfrm);
        setVisible(true);
        }
    
    private void cargarGenerosEnComboBox() {
        
        cboGenero.removeAllItems();
        for (String genero : logica.obtenerGenerosParaComboBox()) {
            cboGenero.addItem(genero);
        }
    }
    
    private void inicializarTabla() {
        modeloTabla = new DefaultTableModel(
        new String[]{"Título", "Autor", "Editorial", "Año", "Género", "Stock"},
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
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        
        for (Object[] fila : logica.obtenerLibrosParaTabla()) {
            modeloTabla.addRow(fila);
        }
    }
    
    private void modificarDesdeTabla() {
        int filaSeleccionada = tblLibros.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar");
            return;
        }
        
        String titulo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        
        libroActual = logica.buscarLibroPorTitulo(titulo);
        
        if (libroActual != null) {
            // Autocompletar campos
            txtTitulo.setText(libroActual.getTitulo());
            txtAutor.setText(libroActual.getAutor().toString());
            txtEditorial.setText(libroActual.getEditorial().getNombre());
            txtAñoPublicación.setText(String.valueOf(libroActual.getAnioPublicacion()));
            cboGenero.setSelectedItem(libroActual.getGenero().getNombre());
            txtStock.setText(String.valueOf(libroActual.getStock()));
            
            JOptionPane.showMessageDialog(this, "Campos completados. Modifica y presiona Guardar.");
        }
    }
    
    private void guardarLibro() {
        if (txtTitulo.getText().isEmpty() || txtStock.getText().isEmpty()) {
            return;
        }
        
        try {
            String titulo = txtTitulo.getText();
            String nombreAutor = txtAutor.getText();
            String nombreEditorial = txtEditorial.getText();
            int ano = Integer.parseInt(txtAñoPublicación.getText());
            String nombreGenero = (String) cboGenero.getSelectedItem();
            int stock = Integer.parseInt(txtStock.getText());
            
            
            String resultado = logica.guardarLibro(titulo, nombreAutor, nombreEditorial, 
                                                   ano, nombreGenero, stock, libroActual);
            
            JOptionPane.showMessageDialog(this, resultado);
            
            limpiarFormulario();
            cargarTabla();
            jfrm.actualizarTablaDesdeDialog();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Verifica que Año y Stock sean números");
        }
    }
    
    private void eliminarLibroDesdeTabla() {
        int filaSeleccionada = tblLibros.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            return;
        }
        
        String titulo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar el libro: " + titulo + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            
            String resultado = logica.eliminarLibro(titulo);
            
            JOptionPane.showMessageDialog(this, resultado);
            cargarTabla();
            jfrm.actualizarTablaDesdeDialog();
        }
    }
    
    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtAñoPublicación.setText("");
        cboGenero.setSelectedIndex(0);
        txtStock.setText("");
        libroActual = null;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        txtEditorial = new javax.swing.JTextField();
        txtAñoPublicación = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        btnRegresar = new javax.swing.JButton();
        btnGuardarLibro = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLibros = new javax.swing.JTable();
        txtAutor = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        cboGenero = new javax.swing.JComboBox<>();

        jLabel7.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setText("Información general");

        jLabel2.setText("Ejemplares");

        jLabel3.setText("Título:");

        jLabel5.setText("Editorial:");

        jLabel6.setText("Año de Publicación:");

        jLabel8.setText("Total de ejemplares: (Stock)");

        jLabel9.setText("Género:");

        txtEditorial.addActionListener(this::txtEditorialActionPerformed);

        btnRegresar.setText("Regresar");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        btnGuardarLibro.setText("Guardar libro");
        btnGuardarLibro.addActionListener(this::btnGuardarLibroActionPerformed);

        tblLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Título", "Autor", "Editorial", "Año publicación", "Género", "Stock"
            }
        ));
        jScrollPane1.setViewportView(tblLibros);

        jLabel10.setText("Autor:");

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 854, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 786, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegresar)
                .addGap(27, 27, 27))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(182, 182, 182)
                                .addComponent(jLabel6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboGenero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTitulo)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(65, 65, 65)
                                        .addComponent(txtAñoPublicación))
                                    .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnModificar)
                                .addGap(46, 46, 46)
                                .addComponent(btnLimpiar)
                                .addGap(42, 42, 42)
                                .addComponent(btnGuardarLibro)
                                .addGap(53, 53, 53))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel10)
                .addGap(1, 1, 1)
                .addComponent(txtAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAñoPublicación, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel9)
                .addGap(2, 2, 2)
                .addComponent(cboGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardarLibro)
                    .addComponent(btnModificar)
                    .addComponent(btnLimpiar))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRegresar)
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEditorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEditorialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEditorialActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        modificarDesdeTabla();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarLibroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarLibroActionPerformed
        guardarLibro();
    }//GEN-LAST:event_btnGuardarLibroActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarLibro;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cboGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLibros;
    private javax.swing.JTextField txtAutor;
    private javax.swing.JTextField txtAñoPublicación;
    private javax.swing.JTextField txtEditorial;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
