/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Vista;

import ClaseBase.*;
import Datos.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Raul
 */
public class JDialog extends javax.swing.JDialog {
    
    private LibroAD libroAD = new LibroAD();
    private AutorAD autorAD = new AutorAD();
    private EditorialAD editorialAD = new EditorialAD();
    private GeneroAD generoAD = new GeneroAD();
    private EjemplarAD ejemplarAD = new EjemplarAD();

// Referencia al JFrame
private Jfrm jfrm;

// Modelo de tabla
private DefaultTableModel modeloTabla;

// Variable de control
private Libro libroActual = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(JDialog.class.getName());

    /**
     * Creates new form JDialog
     */
    public JDialog(Jfrm jfrm) {
        super(jfrm, "Registro de Libros", true);
    this.jfrm = jfrm;
    
    initComponents();
    
    // Inicializar tabla
    inicializarTabla();
    
    // Cargar tabla
    cargarTabla();
    
    // Agregar doble clic en tabla para eliminar
    tblLibros.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                eliminarLibroDesdeTabla();
            }
        }
    });
    
    // Configurar botones
    btnGuardarLibro.addActionListener(e -> guardarLibro());
    btnRegresar.addActionListener(e -> dispose());
    
    setLocationRelativeTo(jfrm);
    setVisible(true);
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
    List<Libro> libros = libroAD.obtenerTodos();
    
    for (Libro libro : libros) {
        modeloTabla.addRow(new Object[]{
            libro.getTitulo(),
            libro.getAutor().toString(),
            libro.getEditorial().getNombre(),
            libro.getAnioPublicacion(),
            libro.getGenero().getNombre(),
            libro.getStock()
        });
    }
}
    
    private void modificarDesdeTabla() {
    int filaSeleccionada = tblLibros.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona una fila para modificar");
        return;
    }
    
    String titulo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
    
    List<Libro> libros = libroAD.obtenerTodos();
    for (Libro libro : libros) {
        if (libro.getTitulo().equals(titulo)) {
            libroActual = libro;
            
            // AUTOCOMPLETADO
            txtTitulo.setText(libro.getTitulo());
            txtAutor.setText(libro.getAutor().toString());
            txtEditorial.setText(libro.getEditorial().getNombre());
            txtAñoPublicación.setText(String.valueOf(libro.getAnioPublicacion()));
            txtGenero.setText(libro.getGenero().getNombre());
            txtStock.setText(String.valueOf(libro.getStock()));
            
            JOptionPane.showMessageDialog(this, "Campos completados. Modifica y presiona Guardar.");
            break;
        }
    }
}
    
    private void guardarLibro() {
    if (txtTitulo.getText().isEmpty() || txtStock.getText().isEmpty()) {
        //JOptionPane.showMessageDialog(this, "Completa los campos obligatorios");
        return;
    }
    
    try {
        String titulo = txtTitulo.getText();
        String nombreAutor = txtAutor.getText();
        String nombreEditorial = txtEditorial.getText();
        int ano = Integer.parseInt(txtAñoPublicación.getText());
        String nombreGenero = txtGenero.getText();
        int stock = Integer.parseInt(txtStock.getText());
        
        // ============================================
        // VERIFICAR/CREAR AUTOR
        // ============================================
        Autor autor = null;
        List<Autor> autoresEnBD = autorAD.obtenerTodos();
        
        for (Autor a : autoresEnBD) {
            if (a.toString().equalsIgnoreCase(nombreAutor)) {
                autor = a;
                break;
            }
        }
        
        // Si no existe, crear uno nuevo
        if (autor == null) {
            // Aquí asumo que el usuario escribe: "Primer Segundo Primer Segundo"
            // Para simplificar, creo un autor con todos los datos en primerNombre
            String[] partes = nombreAutor.trim().split(" ");
    
            String primerNombre = partes.length > 0 ? partes[0] : "";
            String segundoNombre = partes.length > 1 ? partes[1] : "";
            String primerApellido = partes.length > 2 ? partes[2] : "";
            String segundoApellido = partes.length > 3 ? partes[3] : "";
            
            autor = new Autor(0, primerNombre, segundoNombre, primerApellido, segundoApellido);
            autorAD.insertar(autor);
            
            // Obtener el ID asignado
            autoresEnBD = autorAD.obtenerTodos();
            for (Autor a : autoresEnBD) {
                if (a.mostrarDatos().equals(nombreAutor)) {
                    autor = a;
                    break;
                }
            }
        }
        
        // ============================================
        // VERIFICAR/CREAR EDITORIAL
        // ============================================
        Editorial editorial = null;
        List<Editorial> editorialesEnBD = editorialAD.obtenerTodos();
        
        for (Editorial e : editorialesEnBD) {
            if (e.getNombre().equalsIgnoreCase(nombreEditorial)) {
                editorial = e;
                break;
            }
        }
        
        // Si no existe, crear una nueva
        if (editorial == null) {
            editorial = new Editorial(0, nombreEditorial);
            editorialAD.insertar(editorial);
            
            // Obtener el ID asignado
            editorialesEnBD = editorialAD.obtenerTodos();
            for (Editorial e : editorialesEnBD) {
                if (e.getNombre().equals(nombreEditorial)) {
                    editorial = e;
                    break;
                }
            }
        }
        
        // ============================================
        // VERIFICAR/CREAR GÉNERO
        // ============================================
        Genero genero = null;
        List<Genero> generosEnBD = generoAD.obtenerTodos();
        
        for (Genero g : generosEnBD) {
            if (g.getNombre().equalsIgnoreCase(nombreGenero)) {
                genero = g;
                break;
            }
        }
        
        // Si no existe, crear uno nuevo
        if (genero == null) {
            genero = new Genero(0, nombreGenero);
            generoAD.insertar(genero);
            
            // Obtener el ID asignado
            generosEnBD = generoAD.obtenerTodos();
            for (Genero g : generosEnBD) {
                if (g.getNombre().equals(nombreGenero)) {
                    genero = g;
                    break;
                }
            }
        }
        
        // ============================================
        // INSERTAR O ACTUALIZAR LIBRO
        // ============================================
        if (libroActual != null) {
            // MODIFICAR
            libroActual.setTitulo(titulo);
            libroActual.setAutor(autor);
            libroActual.setEditorial(editorial);
            libroActual.setAnioPublicacion(ano);
            libroActual.setGenero(genero);
            libroActual.setStock(stock);
            
            boolean actualizado = libroAD.actualizar(libroActual);
            
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Libro actualizado correctamente");
                limpiarFormulario();
                libroActual = null;
                cargarTabla();
                jfrm.actualizarTablaDesdeDialog();
            }
        } else {
            // INSERTAR NUEVO
            Libro nuevoLibro = new Libro(
                0, genero, autor, editorial,
                titulo, stock, ano
            );
            
            int idLibroInsertado = libroAD.insertar(nuevoLibro);
            
            if (idLibroInsertado > 0) {
                boolean ejemplaresCreados = ejemplarAD.insertarEjemplares(idLibroInsertado, stock);
                
                if (ejemplaresCreados) {
                    JOptionPane.showMessageDialog(this, "Libro registrado con " + stock + " ejemplares");
                    limpiarFormulario();
                    cargarTabla();
                    jfrm.actualizarTablaDesdeDialog();
                }
            }
        }
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
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        List<Libro> libros = libroAD.obtenerTodos();
        
        for (Libro libro : libros) {
            if (libro.getTitulo().equals(titulo)) {
                // Eliminar ejemplares primero
                ejemplarAD.eliminarPorLibro(libro.getIdLibro());
                
                // Eliminar libro
                boolean eliminado = libroAD.eliminar(libro.getIdLibro());
                
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Libro eliminado correctamente");
                    cargarTabla();
                    jfrm.actualizarTablaDesdeDialog();
                }
                break;
            }
        }
    }
}
    
    private void limpiarFormulario() {
    txtTitulo.setText("");
    txtAutor.setText("");
    txtEditorial.setText("");
    txtAñoPublicación.setText("");
    txtGenero.setText("");
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
        txtGenero = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();

        jLabel7.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 500, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
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
                                    .addComponent(txtTitulo)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(65, 65, 65)
                                        .addComponent(txtAñoPublicación))
                                    .addComponent(txtAutor)
                                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
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
                        .addGap(53, 53, 53)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegresar)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
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
                .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
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
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRegresar)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    //SE BORRO METODO

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardarLibro;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegresar;
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
    private javax.swing.JTextField txtGenero;
    private javax.swing.JTextField txtStock;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
