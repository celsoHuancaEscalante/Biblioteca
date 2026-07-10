package VistaJoshuar;

//Actualización 1.1...

import com.toedter.calendar.JCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Buscar//

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

//SQL//

import ConnectXampp.ConnectMySQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;


public class FrmPrestamos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPrestamos.class.getName());
    
    private Date fechaEntregaSeleccionada;
    
private boolean modoEdicion = false;
private int filaEditando = -1;
private final int COL_ID = 0;
private final int COL_DNI = 1;
private final int COL_USUARIO = 2;
private final int COL_LIBRO = 3;
private final int COL_FECHA_PRESTAMO = 4;
private final int COL_FECHA_ENTREGA = 5;
private final int COL_ESTADO = 6;
private final int COL_MULTA = 7;



    public FrmPrestamos() {
        initComponents();
        configurarTabla();
        cargarPrestamos();
        eventoCalendarioTabla();
        configurarTxtBuscarDni();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDniUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtCodigoLibro = new javax.swing.JTextField();
        btnCalendario = new javax.swing.JButton();
        btnRegistrar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblPrestamos = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        txtBuscarDni = new javax.swing.JTextField();
        btnRegistrar1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 204, 204));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("//- - -Apartado de Prestamos - - -//");

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(240, 240, 240))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("DNI - USUARIO :");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("CODIGO -LIBRO :");

        btnCalendario.setBackground(new java.awt.Color(0, 102, 102));
        btnCalendario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCalendario.setForeground(new java.awt.Color(255, 255, 255));
        btnCalendario.setText("- CALENDARIO -");
        btnCalendario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalendarioActionPerformed(evt);
            }
        });

        btnRegistrar.setBackground(new java.awt.Color(0, 102, 102));
        btnRegistrar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegistrar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar.setText("- REGISTRAR -");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 204, 204));

        TblPrestamos.setBackground(new java.awt.Color(204, 204, 204));
        TblPrestamos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "DNI", "USUARIO", "LIBRO ", "FECHA PRESTAMO", "FECHA ENTREGA", "ESTADO PRESTAMO", "MULTA ACUMULADA"
            }
        ));
        TblPrestamos.setColumnSelectionAllowed(true);
        TblPrestamos.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                TblPrestamosAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(TblPrestamos);
        TblPrestamos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        btnEliminar.setBackground(new java.awt.Color(0, 102, 102));
        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("- ELIMINAR -");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnModificar.setBackground(new java.awt.Color(0, 102, 102));
        btnModificar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnModificar.setForeground(new java.awt.Color(255, 255, 255));
        btnModificar.setText("- MÓDIFICAR -");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnGuardar.setBackground(new java.awt.Color(0, 102, 102));
        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(255, 255, 255));
        btnGuardar.setText("- GUARDAR -");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 28)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("/// - Tabla de Prestamos - ///");

        jPanel9.setBackground(new java.awt.Color(204, 255, 255));

        txtBuscarDni.setText("BUSCAR POR DNI ...");

        btnRegistrar1.setBackground(new java.awt.Color(0, 102, 102));
        btnRegistrar1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegistrar1.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrar1.setText("- Buscar -");
        btnRegistrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(305, 305, 305)
                .addComponent(btnRegistrar1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtBuscarDni, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarDni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar1))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(313, 313, 313))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("FECHA ENTREGA:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(287, 287, 287)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(61, 61, 61)
                        .addComponent(txtDniUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(57, 57, 57))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(42, 42, 42)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnCalendario)
                                .addGap(71, 71, 71)
                                .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCodigoLibro, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoLibro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtDniUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalendarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalendarioActionPerformed

    JDialog dialogoCalendario = new JDialog(this, "Seleccionar fecha de entrega", true);
    dialogoCalendario.setSize(400, 350);
    dialogoCalendario.setLocationRelativeTo(this);
    dialogoCalendario.setLayout(new java.awt.BorderLayout());

    JCalendar calendario = new JCalendar();

    JButton btnAceptar = new JButton("Aceptar");

    btnAceptar.addActionListener(e -> {
        fechaEntregaSeleccionada = calendario.getDate();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formato.format(fechaEntregaSeleccionada);

        btnCalendario.setText(fechaFormateada);

        dialogoCalendario.dispose();
    });

    dialogoCalendario.add(calendario, java.awt.BorderLayout.CENTER);
    dialogoCalendario.add(btnAceptar, java.awt.BorderLayout.SOUTH);

    dialogoCalendario.setVisible(true);

    }//GEN-LAST:event_btnCalendarioActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed

    String dni = txtDniUsuario.getText().trim();
    String codigoLibroTexto = txtCodigoLibro.getText().trim();

    if (dni.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese el DNI del usuario.");
        return;
    }

    if (codigoLibroTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese el código del libro.");
        return;
    }

    if (fechaEntregaSeleccionada == null) {
        JOptionPane.showMessageDialog(this, "Seleccione una fecha de entrega.");
        return;
    }

    int idLibro;

    try {
        idLibro = Integer.parseInt(codigoLibroTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El código del libro debe ser numérico.");
        return;
    }

    registrarPrestamoBD(dni, idLibro, fechaEntregaSeleccionada);

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void TblPrestamosAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_TblPrestamosAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_TblPrestamosAncestorAdded

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed


    int fila = TblPrestamos.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.");
        return;
    }

    int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());

    int confirmacion = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de eliminar este préstamo?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION
    );

    if (confirmacion != JOptionPane.YES_OPTION) {
        return;
    }

    eliminarPrestamoBD(idPrestamo);


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

    int fila = TblPrestamos.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una fila para modificar.");
        return;
    }

    modoEdicion = true;
    filaEditando = fila;

    JOptionPane.showMessageDialog(
        this,
        "Modo edición activado.\n\n" +
        "Puede editar DNI y LIBRO.\n" +
        "En LIBRO escriba el código del libro, no el nombre.\n" +
        "Para cambiar FECHA ENTREGA, haga clic en esa celda y seleccione una fecha."
    );

    TblPrestamos.editCellAt(fila, COL_DNI);
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
    if (!modoEdicion || filaEditando == -1) {
        JOptionPane.showMessageDialog(this, "No hay ninguna fila en edición.");
        return;
    }

    int fila = filaEditando;

    int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());
    String dniNuevo = TblPrestamos.getValueAt(fila, COL_DNI).toString().trim();
    Object valorLibro = TblPrestamos.getValueAt(fila, COL_LIBRO);
    Object valorFechaEntrega = TblPrestamos.getValueAt(fila, COL_FECHA_ENTREGA);

    if (dniNuevo.isEmpty()) {
        JOptionPane.showMessageDialog(this, "El DNI no puede estar vacío.");
        return;
    }

    if (valorFechaEntrega == null) {
        JOptionPane.showMessageDialog(this, "La fecha de entrega no puede estar vacía.");
        return;
    }

    java.sql.Date fechaEntregaSQL;

    if (valorFechaEntrega instanceof java.sql.Date) {
        fechaEntregaSQL = (java.sql.Date) valorFechaEntrega;
    } else if (valorFechaEntrega instanceof java.util.Date) {
        fechaEntregaSQL = new java.sql.Date(((java.util.Date) valorFechaEntrega).getTime());
    } else {
        try {
            fechaEntregaSQL = java.sql.Date.valueOf(valorFechaEntrega.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Use el calendario para modificarla.");
            return;
        }
    }

    guardarCambiosPrestamo(idPrestamo, dniNuevo, valorLibro.toString().trim(), fechaEntregaSQL);

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnRegistrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar1ActionPerformed

    String dniBuscar = txtBuscarDni.getText().trim();

    if (dniBuscar.isEmpty() || dniBuscar.equals("BUSCAR POR DNI ...")) {
        JOptionPane.showMessageDialog(this, "Ingrese un DNI para buscar.");
        return;
    }

    cargarPrestamosPorDni(dniBuscar);

    }//GEN-LAST:event_btnRegistrar1ActionPerformed


private void configurarTabla() {

    DefaultTableModel modelo = new DefaultTableModel(
        new Object[][]{},
        new String[]{
            "ID",
            "DNI",
            "USUARIO",
            "LIBRO",
            "FECHA PRESTAMO",
            "FECHA ENTREGA",
            "ESTADO PRESTAMO",
            "MULTA ACUMULADA"
        }
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {

            if (!modoEdicion) {
                return false;
            }

            if (row != filaEditando) {
                return false;
            }

            // Solo permitimos editar DNI y LIBRO.
            // En LIBRO se debe escribir el código del libro.
            return column == COL_DNI || column == COL_LIBRO;
        }
    };

    TblPrestamos.setModel(modelo);
    TblPrestamos.setRowSelectionAllowed(true);
    TblPrestamos.setColumnSelectionAllowed(false);
    TblPrestamos.getTableHeader().setReorderingAllowed(false);
}
    
private void cargarPrestamos() {

    DefaultTableModel modelo = (DefaultTableModel) TblPrestamos.getModel();
    modelo.setRowCount(0);

    String sql =
        "SELECT " +
        "p.id_prestamo AS id, " +
        "c.dni AS dni, " +
        "CONCAT(c.nombres, ' ', c.apellidos) AS usuario, " +
        "l.titulo AS libro, " +
        "p.fecha_prestamo AS fecha_prestamo, " +
        "p.fecha_entrega AS fecha_entrega, " +
        "CASE " +
        "   WHEN p.fecha_devolucion IS NOT NULL THEN 'Devuelto' " +
        "   WHEN CURDATE() > p.fecha_entrega THEN 'Atrasado' " +
        "   ELSE 'Prestado' " +
        "END AS estado_prestamo, " +
        "CASE " +
        "   WHEN p.fecha_devolucion IS NULL AND CURDATE() > p.fecha_entrega " +
        "   THEN DATEDIFF(CURDATE(), p.fecha_entrega) * dp.multa_diaria " +
        "   WHEN p.fecha_devolucion IS NOT NULL AND p.fecha_devolucion > p.fecha_entrega " +
        "   THEN DATEDIFF(p.fecha_devolucion, p.fecha_entrega) * dp.multa_diaria " +
        "   ELSE 0 " +
        "END AS multa_acumulada " +
        "FROM prestamos p " +
        "INNER JOIN clientes c ON p.dni_cliente = c.dni " +
        "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
        "INNER JOIN ejemplares e ON dp.id_ejemplar = e.id_ejemplar " +
        "INNER JOIN libros l ON e.id_libro = l.id_libro " +
        "ORDER BY p.id_prestamo DESC";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql);
         ResultSet rs = pst.executeQuery()) {

        while (rs.next()) {
            modelo.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("dni"),
                rs.getString("usuario"),
                rs.getString("libro"),
                rs.getDate("fecha_prestamo"),
                rs.getDate("fecha_entrega"),
                rs.getString("estado_prestamo"),
                "S/ " + rs.getDouble("multa_acumulada")
            });
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage());
    }
}

private void eventoCalendarioTabla() {

    TblPrestamos.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            if (!modoEdicion) {
                return;
            }

            int fila = TblPrestamos.getSelectedRow();
            int columna = TblPrestamos.getSelectedColumn();

            if (fila == filaEditando && columna == COL_FECHA_ENTREGA) {
                abrirCalendarioParaTabla(fila);
            }
        }
    });
}

private void abrirCalendarioParaTabla(int fila) {

    JDialog dialogo = new JDialog(this, "Modificar fecha de entrega", true);
    dialogo.setSize(400, 350);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new java.awt.BorderLayout());

    JCalendar calendario = new JCalendar();
    JButton btnAceptar = new JButton("Aceptar");

    btnAceptar.addActionListener(e -> {
        Date fechaNueva = calendario.getDate();

        java.sql.Date fechaSQL = new java.sql.Date(fechaNueva.getTime());

        TblPrestamos.setValueAt(fechaSQL, fila, COL_FECHA_ENTREGA);

        dialogo.dispose();
    });

    dialogo.add(calendario, java.awt.BorderLayout.CENTER);
    dialogo.add(btnAceptar, java.awt.BorderLayout.SOUTH);
    dialogo.setVisible(true);
}

private void limpiarCampos() {
    txtDniUsuario.setText("");
    txtCodigoLibro.setText("");
    btnCalendario.setText("- CALENDARIO -");
    fechaEntregaSeleccionada = null;
}

private void guardarCambiosPrestamo(int idPrestamo, String dniNuevo, String valorLibro, java.sql.Date fechaEntregaNueva) {

    Connection conn = ConnectMySQL.conn();

    if (conn == null) {
        JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.");
        return;
    }

    try {
        conn.setAutoCommit(false);

        // 1. Validar DNI
        String sqlCliente = "SELECT CONCAT(nombres, ' ', apellidos) AS usuario FROM clientes WHERE dni = ?";
        PreparedStatement pstCliente = conn.prepareStatement(sqlCliente);
        pstCliente.setString(1, dniNuevo);
        ResultSet rsCliente = pstCliente.executeQuery();

        if (!rsCliente.next()) {
            JOptionPane.showMessageDialog(this, "El DNI ingresado no existe.");
            conn.rollback();
            return;
        }

        // 2. Obtener ejemplar actual del préstamo
        String sqlActual =
            "SELECT dp.id_ejemplar, e.id_libro " +
            "FROM detalle_prestamo dp " +
            "INNER JOIN ejemplares e ON dp.id_ejemplar = e.id_ejemplar " +
            "WHERE dp.id_prestamo = ?";

        PreparedStatement pstActual = conn.prepareStatement(sqlActual);
        pstActual.setInt(1, idPrestamo);
        ResultSet rsActual = pstActual.executeQuery();

        if (!rsActual.next()) {
            JOptionPane.showMessageDialog(this, "No se encontró el detalle del préstamo.");
            conn.rollback();
            return;
        }

        int idEjemplarActual = rsActual.getInt("id_ejemplar");
        int idLibroActual = rsActual.getInt("id_libro");

        int idLibroNuevo = idLibroActual;
        boolean cambiarLibro = false;

        // Si el valor en la columna LIBRO es numérico, se interpreta como código de libro.
        try {
            idLibroNuevo = Integer.parseInt(valorLibro);
            cambiarLibro = idLibroNuevo != idLibroActual;
        } catch (NumberFormatException e) {
            cambiarLibro = false;
        }

        // 3. Actualizar préstamo: DNI y fecha de entrega
        String sqlUpdatePrestamo =
            "UPDATE prestamos SET dni_cliente = ?, fecha_entrega = ? WHERE id_prestamo = ?";

        PreparedStatement pstUpdatePrestamo = conn.prepareStatement(sqlUpdatePrestamo);
        pstUpdatePrestamo.setString(1, dniNuevo);
        pstUpdatePrestamo.setDate(2, fechaEntregaNueva);
        pstUpdatePrestamo.setInt(3, idPrestamo);
        pstUpdatePrestamo.executeUpdate();

        // 4. Si cambió el libro, buscar ejemplar disponible
        if (cambiarLibro) {

            String sqlDisponible =
                "SELECT e.id_ejemplar, c.dias_prestamo, c.costo_reposicion, c.multa_diaria " +
                "FROM ejemplares e " +
                "INNER JOIN libros l ON e.id_libro = l.id_libro " +
                "INNER JOIN categorias c ON l.id_categoria = c.id_categoria " +
                "WHERE e.id_libro = ? AND e.estado = 'Disponible' " +
                "LIMIT 1";

            PreparedStatement pstDisponible = conn.prepareStatement(sqlDisponible);
            pstDisponible.setInt(1, idLibroNuevo);
            ResultSet rsDisponible = pstDisponible.executeQuery();

            if (!rsDisponible.next()) {

                String estado = obtenerEstadoLibro(conn, idLibroNuevo);

                JOptionPane.showMessageDialog(this, "El libro está en estado: " + estado);

                conn.rollback();
                return;
            }

            int idEjemplarNuevo = rsDisponible.getInt("id_ejemplar");
            int diasPrestamo = rsDisponible.getInt("dias_prestamo");
            double costoReposicion = rsDisponible.getDouble("costo_reposicion");
            double multaDiaria = rsDisponible.getDouble("multa_diaria");

            // Liberar ejemplar anterior
            String sqlLiberar =
                "UPDATE ejemplares SET estado = 'Disponible' WHERE id_ejemplar = ?";

            PreparedStatement pstLiberar = conn.prepareStatement(sqlLiberar);
            pstLiberar.setInt(1, idEjemplarActual);
            pstLiberar.executeUpdate();

            // Ocupar nuevo ejemplar
            String sqlOcupar =
                "UPDATE ejemplares SET estado = 'Prestado' WHERE id_ejemplar = ?";

            PreparedStatement pstOcupar = conn.prepareStatement(sqlOcupar);
            pstOcupar.setInt(1, idEjemplarNuevo);
            pstOcupar.executeUpdate();

            // Actualizar detalle del préstamo
            String sqlUpdateDetalle =
                "UPDATE detalle_prestamo " +
                "SET id_ejemplar = ?, dias_prestamo = ?, costo_reposicion = ?, multa_diaria = ? " +
                "WHERE id_prestamo = ?";

            PreparedStatement pstUpdateDetalle = conn.prepareStatement(sqlUpdateDetalle);
            pstUpdateDetalle.setInt(1, idEjemplarNuevo);
            pstUpdateDetalle.setInt(2, diasPrestamo);
            pstUpdateDetalle.setDouble(3, costoReposicion);
            pstUpdateDetalle.setDouble(4, multaDiaria);
            pstUpdateDetalle.setInt(5, idPrestamo);
            pstUpdateDetalle.executeUpdate();
        }

        conn.commit();

        JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");

        modoEdicion = false;
        filaEditando = -1;

        cargarPrestamos();

    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Error al guardar cambios: " + e.getMessage());

    } finally {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private void registrarPrestamoBD(String dni, int idLibro, Date fechaEntrega) {

    Connection conn = ConnectMySQL.conn();

    if (conn == null) {
        JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.");
        return;
    }

    try {
        conn.setAutoCommit(false);

        String sqlCliente = "SELECT dni FROM clientes WHERE dni = ?";
        PreparedStatement pstCliente = conn.prepareStatement(sqlCliente);
        pstCliente.setString(1, dni);
        ResultSet rsCliente = pstCliente.executeQuery();

        if (!rsCliente.next()) {
            JOptionPane.showMessageDialog(this, "El DNI ingresado no existe.");
            conn.rollback();
            return;
        }

        String sqlEjemplar =
            "SELECT e.id_ejemplar, c.dias_prestamo, c.costo_reposicion, c.multa_diaria " +
            "FROM ejemplares e " +
            "INNER JOIN libros l ON e.id_libro = l.id_libro " +
            "INNER JOIN categorias c ON l.id_categoria = c.id_categoria " +
            "WHERE l.id_libro = ? AND e.estado = 'Disponible' " +
            "LIMIT 1";

        PreparedStatement pstEjemplar = conn.prepareStatement(sqlEjemplar);
        pstEjemplar.setInt(1, idLibro);
        ResultSet rsEjemplar = pstEjemplar.executeQuery();

        if (!rsEjemplar.next()) {
            JOptionPane.showMessageDialog(this, "No hay ejemplares disponibles para este libro.");
            conn.rollback();
            return;
        }

        int idEjemplar = rsEjemplar.getInt("id_ejemplar");
        int diasPrestamo = rsEjemplar.getInt("dias_prestamo");
        double costoReposicion = rsEjemplar.getDouble("costo_reposicion");
        double multaDiaria = rsEjemplar.getDouble("multa_diaria");

        String sqlPrestamo =
            "INSERT INTO prestamos " +
            "(dni_cliente, fecha_prestamo, fecha_devolucion, fecha_entrega) " +
            "VALUES (?, CURDATE(), NULL, ?)";

        PreparedStatement pstPrestamo = conn.prepareStatement(sqlPrestamo, Statement.RETURN_GENERATED_KEYS);
        pstPrestamo.setString(1, dni);
        pstPrestamo.setDate(2, new java.sql.Date(fechaEntrega.getTime()));
        pstPrestamo.executeUpdate();

        ResultSet rsKey = pstPrestamo.getGeneratedKeys();

        int idPrestamo;

        if (rsKey.next()) {
            idPrestamo = rsKey.getInt(1);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo generar el ID del préstamo.");
            conn.rollback();
            return;
        }

        String sqlDetalle =
            "INSERT INTO detalle_prestamo " +
            "(id_ejemplar, id_prestamo, dias_prestamo, costo_reposicion, multa_diaria) " +
            "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle);
        pstDetalle.setInt(1, idEjemplar);
        pstDetalle.setInt(2, idPrestamo);
        pstDetalle.setInt(3, diasPrestamo);
        pstDetalle.setDouble(4, costoReposicion);
        pstDetalle.setDouble(5, multaDiaria);
        pstDetalle.executeUpdate();

        String sqlActualizarEjemplar =
            "UPDATE ejemplares SET estado = 'Prestado' WHERE id_ejemplar = ?";

        PreparedStatement pstActualizar = conn.prepareStatement(sqlActualizarEjemplar);
        pstActualizar.setInt(1, idEjemplar);
        pstActualizar.executeUpdate();

        conn.commit();

        JOptionPane.showMessageDialog(this, "Préstamo registrado correctamente. ID: " + idPrestamo);

        cargarPrestamos();
        limpiarCampos();

    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + e.getMessage());

    } finally {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private void eliminarPrestamoBD(int idPrestamo) {

    Connection conn = ConnectMySQL.conn();

    if (conn == null) {
        JOptionPane.showMessageDialog(this, "No se pudo conectar a la base de datos.");
        return;
    }

    try {
        conn.setAutoCommit(false);

        String sqlEjemplar =
            "SELECT id_ejemplar FROM detalle_prestamo WHERE id_prestamo = ?";

        PreparedStatement pstEjemplar = conn.prepareStatement(sqlEjemplar);
        pstEjemplar.setInt(1, idPrestamo);
        ResultSet rsEjemplar = pstEjemplar.executeQuery();

        int idEjemplar = -1;

        if (rsEjemplar.next()) {
            idEjemplar = rsEjemplar.getInt("id_ejemplar");
        }

        String sqlDetalle =
            "DELETE FROM detalle_prestamo WHERE id_prestamo = ?";

        PreparedStatement pstDetalle = conn.prepareStatement(sqlDetalle);
        pstDetalle.setInt(1, idPrestamo);
        pstDetalle.executeUpdate();

        String sqlPrestamo =
            "DELETE FROM prestamos WHERE id_prestamo = ?";

        PreparedStatement pstPrestamo = conn.prepareStatement(sqlPrestamo);
        pstPrestamo.setInt(1, idPrestamo);
        pstPrestamo.executeUpdate();

        if (idEjemplar != -1) {
            String sqlLiberar =
                "UPDATE ejemplares SET estado = 'Disponible' WHERE id_ejemplar = ?";

            PreparedStatement pstLiberar = conn.prepareStatement(sqlLiberar);
            pstLiberar.setInt(1, idEjemplar);
            pstLiberar.executeUpdate();
        }

        conn.commit();

        JOptionPane.showMessageDialog(this, "Préstamo eliminado correctamente.");

        modoEdicion = false;
        filaEditando = -1;

        cargarPrestamos();

    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, "Error al eliminar préstamo: " + e.getMessage());

    } finally {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private String obtenerEstadoLibro(Connection conn, int idLibro) throws SQLException {

    String sql =
        "SELECT estado FROM ejemplares WHERE id_libro = ? LIMIT 1";

    PreparedStatement pst = conn.prepareStatement(sql);
    pst.setInt(1, idLibro);
    ResultSet rs = pst.executeQuery();

    if (rs.next()) {
        return rs.getString("estado");
    }

    return "No existe";
}

private void configurarTxtBuscarDni() {

    txtBuscarDni.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            if (txtBuscarDni.getText().equals("BUSCAR POR DNI ...")) {
                txtBuscarDni.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (txtBuscarDni.getText().trim().isEmpty()) {
                txtBuscarDni.setText("BUSCAR POR DNI ...");
            }
        }
    });
}

private void cargarPrestamosPorDni(String dniBuscar) {

    DefaultTableModel modelo = (DefaultTableModel) TblPrestamos.getModel();
    modelo.setRowCount(0);

    String sql =
        "SELECT " +
        "p.id_prestamo AS id, " +
        "c.dni AS dni, " +
        "CONCAT(c.nombres, ' ', c.apellidos) AS usuario, " +
        "l.titulo AS libro, " +
        "p.fecha_prestamo AS fecha_prestamo, " +
        "p.fecha_entrega AS fecha_entrega, " +
        "CASE " +
        "   WHEN p.fecha_devolucion IS NOT NULL THEN 'Devuelto' " +
        "   WHEN CURDATE() > p.fecha_entrega THEN 'Atrasado' " +
        "   ELSE 'Prestado' " +
        "END AS estado_prestamo, " +
        "CASE " +
        "   WHEN p.fecha_devolucion IS NULL AND CURDATE() > p.fecha_entrega " +
        "   THEN DATEDIFF(CURDATE(), p.fecha_entrega) * dp.multa_diaria " +
        "   WHEN p.fecha_devolucion IS NOT NULL AND p.fecha_devolucion > p.fecha_entrega " +
        "   THEN DATEDIFF(p.fecha_devolucion, p.fecha_entrega) * dp.multa_diaria " +
        "   ELSE 0 " +
        "END AS multa_acumulada " +
        "FROM prestamos p " +
        "INNER JOIN clientes c ON p.dni_cliente = c.dni " +
        "INNER JOIN detalle_prestamo dp ON p.id_prestamo = dp.id_prestamo " +
        "INNER JOIN ejemplares e ON dp.id_ejemplar = e.id_ejemplar " +
        "INNER JOIN libros l ON e.id_libro = l.id_libro " +
        "WHERE c.dni = ? " +
        "ORDER BY p.id_prestamo DESC";

    try (Connection conn = ConnectMySQL.conn();
         PreparedStatement pst = conn.prepareStatement(sql)) {

        pst.setString(1, dniBuscar);

        ResultSet rs = pst.executeQuery();

        boolean encontro = false;

        while (rs.next()) {
            encontro = true;

            modelo.addRow(new Object[]{
                rs.getInt("id"),
                rs.getString("dni"),
                rs.getString("usuario"),
                rs.getString("libro"),
                rs.getDate("fecha_prestamo"),
                rs.getDate("fecha_entrega"),
                rs.getString("estado_prestamo"),
                "S/ " + rs.getDouble("multa_acumulada")
            });
        }

        if (!encontro) {
            JOptionPane.showMessageDialog(this, "No se encontraron préstamos para el DNI: " + dniBuscar);
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al buscar préstamos: " + e.getMessage());
    }
}

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new FrmPrestamos().setVisible(true));
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TblPrestamos;
    private javax.swing.JButton btnCalendario;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscarDni;
    private javax.swing.JTextField txtCodigoLibro;
    private javax.swing.JTextField txtDniUsuario;
    // End of variables declaration//GEN-END:variables
}
