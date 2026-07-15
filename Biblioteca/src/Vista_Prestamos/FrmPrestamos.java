package Vista_Prestamos;
//V1.5 Alpha...

import ClaseBase.Cliente;
import ClaseBase.Prestamo;
import Datos_Prestamos.DatosSQL;
import Modelo_Prestamos.Prestamos_Tabla;

import com.toedter.calendar.JCalendar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
public class FrmPrestamos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPrestamos.class.getName());
    
private Date fechaEntregaSeleccionada;
private DatosSQL prestamos_Datos = new DatosSQL();
private int idLibroSeleccionadoEdicion = -1;
private String dniAnterior = "";
private String usuarioAnterior = "";
private String libroAnterior = "";
private int filaHover = -1;
private int columnaHover = -1;
private int filaSeleccionEfecto = -1;
private int columnaSeleccionEfecto = -1;
private float alphaSeleccion = 0.0f;
private Timer timerEsperaSeleccion;
private Timer timerDesvanecerSeleccion;   
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
    configurarVentanaRedimensionable();
    configurarTabla();
    configurarEditorColumnaLibro();
    configurarColorEstado();
    cargarPrestamos();
    eventoCalendarioTabla();
    configurarTxtBuscarDni();
    configurarEditorColumnaDni();
    configurarTxtDniUsuario();
    configurarTxtCodigoLibro();
    
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
        btnDetalles = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
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
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "DNI", "USUARIO", "LIBRO ", "FECHA PRESTAMO", "FECHA ENTREGA", "ESTADO ", "MONTO", "MULTA "
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
                .addContainerGap(324, Short.MAX_VALUE))
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

        btnDetalles.setBackground(new java.awt.Color(0, 102, 102));
        btnDetalles.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDetalles.setForeground(new java.awt.Color(255, 255, 255));
        btnDetalles.setText("- DETALLES -");
        btnDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetallesActionPerformed(evt);
            }
        });

        jLabel6.setText("V 1.5_Alpha");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(142, 142, 142))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(2, 2, 2)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(btnDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(12, Short.MAX_VALUE))
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
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCalendario, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtDniUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    String codigoLibroTexto = txtCodigoLibro.getText().trim();

    if (codigoLibroTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese primero el código del libro.");
        txtCodigoLibro.requestFocus();
        return;
    }

    int idLibro;

    try {
        idLibro = Integer.parseInt(codigoLibroTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El código del libro debe ser numérico.");
        txtCodigoLibro.requestFocus();
        return;
    }

    int diasMaximos = prestamos_Datos.obtenerDiasMaximosPorLibro(idLibro);

    if (diasMaximos <= 0) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        txtCodigoLibro.requestFocus();
        return;
    }

    Date fechaMinima = new Date();

    java.util.Calendar calendarioMaximo = java.util.Calendar.getInstance();
    calendarioMaximo.setTime(fechaMinima);
    calendarioMaximo.add(java.util.Calendar.DAY_OF_MONTH, diasMaximos);

    Date fechaMaxima = calendarioMaximo.getTime();

    JDialog dialogoCalendario = new JDialog(this, "Seleccionar fecha de entrega", true);
    dialogoCalendario.setSize(400, 350);
    dialogoCalendario.setLocationRelativeTo(this);
    dialogoCalendario.setLayout(new java.awt.BorderLayout());

    JCalendar calendario = new JCalendar();

    calendario.setMinSelectableDate(fechaMinima);
    calendario.setMaxSelectableDate(fechaMaxima);

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
        txtDniUsuario.requestFocus();
        return;
    }

    if (dni.length() != 8) {
        JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos.");
        txtDniUsuario.requestFocus();
        return;
    }

    if (!prestamos_Datos.validarUsuarioPuedeRegistrarPrestamo(dni)) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        txtDniUsuario.setText("");
        txtDniUsuario.requestFocus();
        return;
    }

    if (codigoLibroTexto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Ingrese el código del libro.");
        txtCodigoLibro.requestFocus();
        return;
    }

    int idLibro;

    try {
        idLibro = Integer.parseInt(codigoLibroTexto);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El código del libro debe ser numérico.");
        txtCodigoLibro.requestFocus();
        return;
    }

    String tituloLibro = prestamos_Datos.obtenerTituloLibroDisponible(idLibro);

    if (tituloLibro == null) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        txtCodigoLibro.setText("");
        txtCodigoLibro.requestFocus();
        return;
    }

    if (fechaEntregaSeleccionada == null) {
        JOptionPane.showMessageDialog(this, "Seleccione una fecha de entrega.");
        return;
    }

    Cliente cliente = new Cliente();
    cliente.setDni(dni);

    LocalDate fechaEntrega = fechaEntregaSeleccionada
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

    Prestamo prestamo = new Prestamo();
    prestamo.setCliente(cliente);
    prestamo.setFechaVencimiento(fechaEntrega);

    boolean registrado = prestamos_Datos.registrarPrestamo(prestamo, idLibro);

    JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());

    if (registrado) {
        cargarPrestamos();
        limpiarCampos();
    }

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void TblPrestamosAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_TblPrestamosAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_TblPrestamosAncestorAdded

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed

 
    if (!modoEdicion) {
        JOptionPane.showMessageDialog(this, "Primero debe activar el modo modificación para eliminar.");
        return;
    }

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

    boolean eliminado = prestamos_Datos.eliminarPrestamo(idPrestamo);

    JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());

    if (eliminado) {
        modoEdicion = false;
        filaEditando = -1;
        cargarPrestamos();
    }


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

    modoEdicion = true;
    filaEditando = -1;

    TblPrestamos.clearSelection();

    JOptionPane.showMessageDialog(
        this,
        "Modo modificación activado.\n\n" +
        "Instrucciones:\n" +
        "- Haga clic en la fila que desea modificar.\n" +
        "- Puede editar DNI y LIBRO.\n" +
        "- En LIBRO escriba el código del libro, no el nombre.\n" +
        "- Para cambiar FECHA PRÉSTAMO, haga clic en esa columna.\n" +
        "- Para cambiar FECHA ENTREGA, haga clic en esa columna.\n" +
        "- Al presionar GUARDAR se saldrá del modo modificación."
    );

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

    if (!modoEdicion || filaEditando == -1) {
        JOptionPane.showMessageDialog(this, "Primero debe activar el modo modificación.");
        return;
    }

    if (TblPrestamos.isEditing()) {
        TblPrestamos.getCellEditor().stopCellEditing();
    }

    int fila = filaEditando;

    int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());
    String dniNuevo = TblPrestamos.getValueAt(fila, COL_DNI).toString().trim();
    Object valorLibro = TblPrestamos.getValueAt(fila, COL_LIBRO);
    Object valorFechaEntrega = TblPrestamos.getValueAt(fila, COL_FECHA_ENTREGA);
    Object valorFechaPrestamo = TblPrestamos.getValueAt(fila, COL_FECHA_PRESTAMO);

    if (dniNuevo.isEmpty()) {
        TblPrestamos.setValueAt(dniAnterior, fila, COL_DNI);
        TblPrestamos.setValueAt(usuarioAnterior, fila, COL_USUARIO);

        JOptionPane.showMessageDialog(
                this,
                "No se cambió el DNI. Se restauró el registro anterior."
        );
        return;
    }

    if (valorLibro == null || valorLibro.toString().trim().isEmpty()) {
        TblPrestamos.setValueAt(libroAnterior, fila, COL_LIBRO);

        JOptionPane.showMessageDialog(
                this,
                "No se cambió el libro. Se restauró el registro anterior."
        );
        return;
    }

    if (valorFechaPrestamo == null) {
        JOptionPane.showMessageDialog(this, "La fecha de préstamo no puede estar vacía.");
        return;
    }

    if (valorFechaEntrega == null) {
        JOptionPane.showMessageDialog(this, "La fecha de entrega no puede estar vacía.");
        return;
    }

    LocalDate fechaPrestamoNueva;

    if (valorFechaPrestamo instanceof LocalDate) {
        fechaPrestamoNueva = (LocalDate) valorFechaPrestamo;

    } else if (valorFechaPrestamo instanceof java.sql.Date) {
        fechaPrestamoNueva = ((java.sql.Date) valorFechaPrestamo).toLocalDate();

    } else if (valorFechaPrestamo instanceof java.util.Date) {
        fechaPrestamoNueva = ((java.util.Date) valorFechaPrestamo)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

    } else {
        try {
            fechaPrestamoNueva = java.sql.Date.valueOf(valorFechaPrestamo.toString()).toLocalDate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha de préstamo inválida. Use el calendario.");
            return;
        }
    }

    LocalDate fechaEntregaNueva;

    if (valorFechaEntrega instanceof LocalDate) {
        fechaEntregaNueva = (LocalDate) valorFechaEntrega;

    } else if (valorFechaEntrega instanceof java.sql.Date) {
        fechaEntregaNueva = ((java.sql.Date) valorFechaEntrega).toLocalDate();

    } else if (valorFechaEntrega instanceof java.util.Date) {
        fechaEntregaNueva = ((java.util.Date) valorFechaEntrega)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

    } else {
        try {
            fechaEntregaNueva = java.sql.Date.valueOf(valorFechaEntrega.toString()).toLocalDate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha de entrega inválida. Use el calendario.");
            return;
        }
    }

    if (fechaPrestamoNueva.isAfter(fechaEntregaNueva)) {
        JOptionPane.showMessageDialog(
                this,
                "La fecha de préstamo no puede ser mayor que la fecha de entrega."
        );
        return;
    }

    String valorLibroGuardar;

    if (idLibroSeleccionadoEdicion != -1) {
        valorLibroGuardar = String.valueOf(idLibroSeleccionadoEdicion);
    } else {
        valorLibroGuardar = valorLibro.toString().trim();
    }

    String usuarioValidado = prestamos_Datos.validarDniParaEdicion(dniNuevo, idPrestamo);

    if (usuarioValidado == null) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        return;
    }

    boolean guardado = prestamos_Datos.modificarPrestamo(
            idPrestamo,
            dniNuevo,
            valorLibroGuardar,
            fechaPrestamoNueva,
            fechaEntregaNueva
    );

    JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());

    if (guardado) {
        idLibroSeleccionadoEdicion = -1;

        dniAnterior = "";
        usuarioAnterior = "";
        libroAnterior = "";

        modoEdicion = false;
        filaEditando = -1;

        cargarPrestamos();
    }


    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnRegistrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar1ActionPerformed

    String dniBuscar = txtBuscarDni.getText().trim();

    if (dniBuscar.isEmpty() || dniBuscar.equals("BUSCAR POR DNI ...")) {
        JOptionPane.showMessageDialog(this, "Ingrese un DNI para buscar.");
        return;
    }

    cargarPrestamosPorDni(dniBuscar);

    }//GEN-LAST:event_btnRegistrar1ActionPerformed

    private void btnDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetallesActionPerformed

    if (modoEdicion) {
        JOptionPane.showMessageDialog(
                this,
                "No puede ver detalles mientras está en modo modificación.\nPrimero guarde o termine la edición."
        );
        return;
    }

    int fila = TblPrestamos.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione primero la fila.");
        return;
    }

    int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());

    String detalle = prestamos_Datos.obtenerDetallePrestamoHTML(idPrestamo);

    if (detalle == null) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        return;
    }

    JOptionPane.showMessageDialog(
            this,
            detalle,
            "Detalles de Préstamo",
            JOptionPane.INFORMATION_MESSAGE
    );

    }//GEN-LAST:event_btnDetallesActionPerformed

    //METODOSUSADOS//

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
            "ESTADO",
            "PAGO TOTAL"
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

private void configurarEditorColumnaDni() {

    JTextField txtEditorDni = new JTextField();

    ((AbstractDocument) txtEditorDni.getDocument()).setDocumentFilter(new DocumentFilter() {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            if (string == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + string + textoActual.substring(offset);

            if (string.matches("\\d+") && nuevoTexto.length() <= 8) {
                super.insertString(fb, offset, string, attr);
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Poner una ID Valida."
                    );
                });
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + text + textoActual.substring(offset + length);

            if (text.matches("\\d*") && nuevoTexto.length() <= 8) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Poner una ID Valida."
                    );
                });
            }
        }
    });

    Timer temporizadorDni = new Timer(2000, e -> {

        String dni = txtEditorDni.getText().trim();

        if (dni.isEmpty()) {
            return;
        }

        if (dni.length() != 8) {
            JOptionPane.showMessageDialog(this, "Poner una ID Valida.");
            return;
        }

        int fila = TblPrestamos.getEditingRow();

        if (fila == -1) {
            fila = filaEditando;
        }

        if (fila == -1) {
            return;
        }

        int idPrestamoActual = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());

        String usuario = prestamos_Datos.validarDniParaEdicion(dni, idPrestamoActual);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());

            txtEditorDni.setText("");

            TblPrestamos.setValueAt("", fila, COL_DNI);
            TblPrestamos.setValueAt("", fila, COL_USUARIO);

            return;
        }

        if (TblPrestamos.isEditing()) {
            TblPrestamos.getCellEditor().stopCellEditing();
        }

        TblPrestamos.setValueAt(dni, fila, COL_DNI);
        TblPrestamos.setValueAt(usuario, fila, COL_USUARIO);
    });

    temporizadorDni.setRepeats(false);

    txtEditorDni.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            temporizadorDni.restart();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            temporizadorDni.restart();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            temporizadorDni.restart();
        }
    });

    TblPrestamos.getColumnModel().getColumn(COL_DNI).setCellEditor(new DefaultCellEditor(txtEditorDni));
}

private void configurarColorEstado() {

    Color colorNormal = new Color(245, 252, 255);
    Color colorFilaHover = new Color(226, 242, 255);
    Color colorColumnaHover = new Color(234, 247, 255);
    Color colorCeldaHover = new Color(210, 232, 250);

    Color colorSeleccionBase = new Color(180, 215, 245);
    Color colorGrid = new Color(210, 225, 235);

    TblPrestamos.setBackground(colorNormal);
    TblPrestamos.setSelectionBackground(colorSeleccionBase);
    TblPrestamos.setSelectionForeground(Color.BLACK);
    TblPrestamos.setGridColor(colorGrid);

    TblPrestamos.setRowSelectionAllowed(true);
    TblPrestamos.setColumnSelectionAllowed(false);
    TblPrestamos.setCellSelectionEnabled(false);

    DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {

        @Override
        public Component getTableCellRendererComponent(
                javax.swing.JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            Component componente = super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            componente.setFont(componente.getFont().deriveFont(Font.PLAIN));
            componente.setForeground(Color.BLACK);

            Color fondo = colorNormal;

            if (row == filaHover && column == columnaHover) {
                fondo = colorCeldaHover;

            } else if (row == filaHover) {
                fondo = colorFilaHover;

            } else if (column == columnaHover) {
                fondo = colorColumnaHover;
            }

            if (filaSeleccionEfecto != -1 && columnaSeleccionEfecto != -1 && alphaSeleccion > 0) {

                if (row == filaSeleccionEfecto && column == columnaSeleccionEfecto) {
                    fondo = mezclarColor(fondo, colorSeleccionBase, alphaSeleccion);

                } else if (row == filaSeleccionEfecto) {
                    fondo = mezclarColor(fondo, new Color(200, 230, 250), alphaSeleccion);

                } else if (column == columnaSeleccionEfecto) {
                    fondo = mezclarColor(fondo, new Color(215, 238, 252), alphaSeleccion);
                }
            }

            componente.setBackground(fondo);

            if (column == COL_ESTADO && value != null) {

                String estado = value.toString().trim();

                if (estado.equalsIgnoreCase("Disponible") || estado.equalsIgnoreCase("Devuelto")) {
                    componente.setForeground(new Color(0, 140, 0));

                } else if (estado.equalsIgnoreCase("Prestado")) {
                    componente.setForeground(new Color(190, 140, 0));

                } else if (estado.equalsIgnoreCase("Atrasado")) {
                    componente.setForeground(Color.RED);

                } else if (estado.equalsIgnoreCase("Perdido")) {
                    componente.setForeground(Color.BLACK);
                    componente.setFont(componente.getFont().deriveFont(Font.BOLD));
                }
            }

            setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            return componente;
        }
    };

    for (int i = 0; i < TblPrestamos.getColumnCount(); i++) {
        TblPrestamos.getColumnModel().getColumn(i).setCellRenderer(renderizador);
    }

    TblPrestamos.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
        @Override
        public void mouseMoved(MouseEvent e) {

            int fila = TblPrestamos.rowAtPoint(e.getPoint());
            int columna = TblPrestamos.columnAtPoint(e.getPoint());

            if (fila != filaHover || columna != columnaHover) {
                filaHover = fila;
                columnaHover = columna;
                TblPrestamos.repaint();
            }
        }
    });

    TblPrestamos.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {

            int fila = TblPrestamos.rowAtPoint(e.getPoint());
            int columna = TblPrestamos.columnAtPoint(e.getPoint());

            if (fila == -1 || columna == -1) {
                return;
            }

            filaSeleccionEfecto = fila;
            columnaSeleccionEfecto = columna;
            alphaSeleccion = 1.0f;

            TblPrestamos.repaint();

            if (timerEsperaSeleccion != null && timerEsperaSeleccion.isRunning()) {
                timerEsperaSeleccion.stop();
            }

            if (timerDesvanecerSeleccion != null && timerDesvanecerSeleccion.isRunning()) {
                timerDesvanecerSeleccion.stop();
            }

            timerEsperaSeleccion = new Timer(10000, ev -> {

                timerDesvanecerSeleccion = new Timer(70, fade -> {

                    alphaSeleccion -= 0.05f;

                    if (alphaSeleccion <= 0) {
                        alphaSeleccion = 0.0f;
                        filaSeleccionEfecto = -1;
                        columnaSeleccionEfecto = -1;

                        timerDesvanecerSeleccion.stop();
                    }

                    TblPrestamos.repaint();
                });

                timerDesvanecerSeleccion.start();
            });

            timerEsperaSeleccion.setRepeats(false);
            timerEsperaSeleccion.start();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            filaHover = -1;
            columnaHover = -1;
            TblPrestamos.repaint();
        }
    });
}
    
private void cargarPrestamos() {

    DefaultTableModel modelo = (DefaultTableModel) TblPrestamos.getModel();
    modelo.setRowCount(0);

    ArrayList<Prestamos_Tabla> lista = prestamos_Datos.listarPrestamos();

    for (Prestamos_Tabla p : lista) {
        modelo.addRow(new Object[]{
            p.getIdPrestamo(),
            p.getDni(),
            p.getUsuario(),
            p.getLibro(),
            p.getFechaPrestamo(),
            p.getFechaEntrega(),
            p.getEstadoPrestamo(),
            "S/ " + p.getPagoTotal()
        });
    }

    if (lista.isEmpty() && prestamos_Datos.getMensaje() != null) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
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

            if (fila == -1) {
                return;
            }

            if (filaEditando != -1 && filaEditando != fila) {
                restaurarDatosSiQuedaronVacios();
            }

            filaEditando = fila;

            if (columna == COL_ESTADO && e.getClickCount() == 2) {

                int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());
                String estadoActual = TblPrestamos.getValueAt(fila, COL_ESTADO).toString();

                int confirmacion = JOptionPane.showConfirmDialog(
                        FrmPrestamos.this,
                        "¿Desea cambiar el estado del préstamo?\n\n" +
                        "Estado actual: " + estadoActual,
                        "Confirmar cambio de estado",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }

                boolean cambiado = prestamos_Datos.cambiarEstadoPrestamoPorDobleClick(idPrestamo, estadoActual);

                JOptionPane.showMessageDialog(FrmPrestamos.this, prestamos_Datos.getMensaje());

                if (cambiado) {
                    cargarPrestamos();
                    modoEdicion = false;
                    filaEditando = -1;
                }

                return;
            }

            if (columna == COL_FECHA_ENTREGA) {
                restaurarDatosSiQuedaronVacios();
                abrirCalendarioParaTabla(fila);
                return;
            }

            if (columna == COL_FECHA_PRESTAMO) {
                restaurarDatosSiQuedaronVacios();
                abrirCalendarioFechaPrestamo(fila);
                return;
            }

            if (columna == COL_LIBRO) {

                Object libroActual = TblPrestamos.getValueAt(fila, COL_LIBRO);

                if (libroActual != null && !libroActual.toString().trim().isEmpty()) {
                    libroAnterior = libroActual.toString();
                }

                idLibroSeleccionadoEdicion = -1;

                TblPrestamos.setValueAt("", fila, COL_LIBRO);
                TblPrestamos.editCellAt(fila, COL_LIBRO);

                if (TblPrestamos.getEditorComponent() != null) {
                    TblPrestamos.getEditorComponent().requestFocus();
                }

                return;
            }

            if (columna == COL_DNI) {

                Object dniActual = TblPrestamos.getValueAt(fila, COL_DNI);
                Object usuarioActual = TblPrestamos.getValueAt(fila, COL_USUARIO);

                if (dniActual != null && !dniActual.toString().trim().isEmpty()) {
                    dniAnterior = dniActual.toString();
                }

                if (usuarioActual != null && !usuarioActual.toString().trim().isEmpty()) {
                    usuarioAnterior = usuarioActual.toString();
                }

                TblPrestamos.setValueAt("", fila, COL_DNI);
                TblPrestamos.setValueAt("", fila, COL_USUARIO);

                TblPrestamos.editCellAt(fila, COL_DNI);

                if (TblPrestamos.getEditorComponent() != null) {
                    TblPrestamos.getEditorComponent().requestFocus();
                }

                return;
            }
        }
    });
}

private void abrirCalendarioFechaPrestamo(int fila) {

    Object valorFechaEntrega = TblPrestamos.getValueAt(fila, COL_FECHA_ENTREGA);

    if (valorFechaEntrega == null) {
        JOptionPane.showMessageDialog(this, "Primero debe existir una fecha de entrega.");
        return;
    }

    Date fechaEntregaDate;

    if (valorFechaEntrega instanceof java.sql.Date) {
        fechaEntregaDate = (java.sql.Date) valorFechaEntrega;

    } else if (valorFechaEntrega instanceof java.util.Date) {
        fechaEntregaDate = (java.util.Date) valorFechaEntrega;

    } else {
        try {
            java.sql.Date fechaConvertida = java.sql.Date.valueOf(valorFechaEntrega.toString());
            fechaEntregaDate = fechaConvertida;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "La fecha de entrega no es válida.");
            return;
        }
    }

    JDialog dialogo = new JDialog(this, "Modificar fecha de préstamo", true);
    dialogo.setSize(400, 350);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new java.awt.BorderLayout());

    JCalendar calendario = new JCalendar();

    calendario.setMaxSelectableDate(fechaEntregaDate);

    Object fechaActualTabla = TblPrestamos.getValueAt(fila, COL_FECHA_PRESTAMO);

    if (fechaActualTabla instanceof java.sql.Date) {
        calendario.setDate((java.sql.Date) fechaActualTabla);

    } else if (fechaActualTabla instanceof java.util.Date) {
        calendario.setDate((java.util.Date) fechaActualTabla);
    }

    JButton btnAceptar = new JButton("Aceptar");

    btnAceptar.addActionListener(e -> {
        Date fechaNueva = calendario.getDate();

        if (fechaNueva.after(fechaEntregaDate)) {
            JOptionPane.showMessageDialog(
                    this,
                    "La fecha de préstamo no puede ser mayor que la fecha de entrega."
            );
            return;
        }

        java.sql.Date fechaSQL = new java.sql.Date(fechaNueva.getTime());

TblPrestamos.setValueAt(fechaSQL, fila, COL_FECHA_PRESTAMO);

actualizarEstadoYPagoFila(fila);

dialogo.dispose();
    });

    dialogo.add(calendario, java.awt.BorderLayout.CENTER);
    dialogo.add(btnAceptar, java.awt.BorderLayout.SOUTH);
    dialogo.setVisible(true);
}

private void abrirCalendarioParaTabla(int fila) {

    int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());

    int diasMaximos = prestamos_Datos.obtenerDiasMaximosPorPrestamo(idPrestamo);

    if (diasMaximos <= 0) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        return;
    }

    Date fechaMinima = new Date();

    java.util.Calendar calendarioMaximo = java.util.Calendar.getInstance();
    calendarioMaximo.setTime(fechaMinima);
    calendarioMaximo.add(java.util.Calendar.DAY_OF_MONTH, diasMaximos);

    Date fechaMaxima = calendarioMaximo.getTime();

    JDialog dialogo = new JDialog(this, "Modificar fecha de entrega", true);
    dialogo.setSize(400, 350);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new java.awt.BorderLayout());

    JCalendar calendario = new JCalendar();

    calendario.setMinSelectableDate(fechaMinima);
    calendario.setMaxSelectableDate(fechaMaxima);

    Object fechaActualTabla = TblPrestamos.getValueAt(fila, COL_FECHA_ENTREGA);

    if (fechaActualTabla instanceof java.sql.Date) {
        calendario.setDate((java.sql.Date) fechaActualTabla);
    } else if (fechaActualTabla instanceof java.util.Date) {
        calendario.setDate((java.util.Date) fechaActualTabla);
    }

    JButton btnAceptar = new JButton("Aceptar");

    btnAceptar.addActionListener(e -> {
        Date fechaNueva = calendario.getDate();

        java.sql.Date fechaSQL = new java.sql.Date(fechaNueva.getTime());


TblPrestamos.setValueAt(fechaSQL, fila, COL_FECHA_ENTREGA);

actualizarEstadoYPagoFila(fila);

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
                cargarPrestamos();
            }
        }
    });

    ((AbstractDocument) txtBuscarDni.getDocument()).setDocumentFilter(new DocumentFilter() {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            if (string == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + string + textoActual.substring(offset);

            if (!string.matches("\\d+")) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Solo se permiten números."
                    );
                });
                return;
            }

            if (nuevoTexto.length() > 8) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "El DNI debe tener máximo 8 dígitos."
                    );
                });
                return;
            }

            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + text + textoActual.substring(offset + length);

            if (!text.matches("\\d*")) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Solo se permiten números."
                    );
                });
                return;
            }

            if (nuevoTexto.length() > 8) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "El DNI debe tener máximo 8 dígitos."
                    );
                });
                return;
            }

            super.replace(fb, offset, length, text, attrs);
        }
    });

    txtBuscarDni.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filtrarPrestamosMientrasEscribe();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filtrarPrestamosMientrasEscribe();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filtrarPrestamosMientrasEscribe();
        }
    });
}

private void filtrarPrestamosMientrasEscribe() {

    String texto = txtBuscarDni.getText().trim();

    if (texto.equals("BUSCAR POR DNI ...")) {
        return;
    }

    if (texto.isEmpty()) {
        cargarPrestamos();
        return;
    }

    DefaultTableModel modelo = (DefaultTableModel) TblPrestamos.getModel();
    modelo.setRowCount(0);

    ArrayList<Prestamos_Tabla> lista = prestamos_Datos.buscarPrestamosPorDni(texto);

    for (Prestamos_Tabla p : lista) {
        modelo.addRow(new Object[]{
            p.getIdPrestamo(),
            p.getDni(),
            p.getUsuario(),
            p.getLibro(),
            p.getFechaPrestamo(),
            p.getFechaEntrega(),
            p.getEstadoPrestamo(),
            "S/ " + p.getPagoTotal()
        });
    }
}

private void cargarPrestamosPorDni(String dniBuscar) {

    DefaultTableModel modelo = (DefaultTableModel) TblPrestamos.getModel();
    modelo.setRowCount(0);

    ArrayList<Prestamos_Tabla> lista = prestamos_Datos.buscarPrestamosPorDni(dniBuscar);

    for (Prestamos_Tabla p : lista) {
        modelo.addRow(new Object[]{
            p.getIdPrestamo(),
            p.getDni(),
            p.getUsuario(),
            p.getLibro(),
            p.getFechaPrestamo(),
            p.getFechaEntrega(),
            p.getEstadoPrestamo(),
            "S/ " + p.getPagoTotal()
        });
    }

    if (lista.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No se encontraron préstamos para el DNI: " + dniBuscar);
    }
}

private void actualizarEstadoYPagoFila(int fila) {

    int idPrestamo = Integer.parseInt(TblPrestamos.getValueAt(fila, COL_ID).toString());

    Object valorFechaPrestamo = TblPrestamos.getValueAt(fila, COL_FECHA_PRESTAMO);
    Object valorFechaEntrega = TblPrestamos.getValueAt(fila, COL_FECHA_ENTREGA);

    if (valorFechaPrestamo == null || valorFechaEntrega == null) {
        return;
    }

    LocalDate fechaPrestamo;
    LocalDate fechaEntrega;

    try {
        if (valorFechaPrestamo instanceof LocalDate) {
            fechaPrestamo = (LocalDate) valorFechaPrestamo;
        } else if (valorFechaPrestamo instanceof java.sql.Date) {
            fechaPrestamo = ((java.sql.Date) valorFechaPrestamo).toLocalDate();
        } else if (valorFechaPrestamo instanceof java.util.Date) {
            fechaPrestamo = ((java.util.Date) valorFechaPrestamo)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } else {
            fechaPrestamo = java.sql.Date.valueOf(valorFechaPrestamo.toString()).toLocalDate();
        }

        if (valorFechaEntrega instanceof LocalDate) {
            fechaEntrega = (LocalDate) valorFechaEntrega;
        } else if (valorFechaEntrega instanceof java.sql.Date) {
            fechaEntrega = ((java.sql.Date) valorFechaEntrega).toLocalDate();
        } else if (valorFechaEntrega instanceof java.util.Date) {
            fechaEntrega = ((java.util.Date) valorFechaEntrega)
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } else {
            fechaEntrega = java.sql.Date.valueOf(valorFechaEntrega.toString()).toLocalDate();
        }

    } catch (Exception e) {
        return;
    }

    int diasMaximos = prestamos_Datos.obtenerDiasMaximosPorPrestamo(idPrestamo);

    if (diasMaximos <= 0) {
        JOptionPane.showMessageDialog(this, prestamos_Datos.getMensaje());
        return;
    }

    long diasEntrePrestamoYEntrega = java.time.temporal.ChronoUnit.DAYS.between(fechaPrestamo, fechaEntrega);

    long diasAtraso = diasEntrePrestamoYEntrega - diasMaximos;

    if (diasAtraso < 0) {
        diasAtraso = 0;
    }

    String nuevoEstado;

    if (diasAtraso >= 100) {
        nuevoEstado = "Perdido";
    } else if (diasAtraso > 0) {
        nuevoEstado = "Atrasado";
    } else {
        nuevoEstado = "Prestado";
    }

    double[] datosPago = prestamos_Datos.obtenerDatosPagoPorPrestamo(idPrestamo);

    if (datosPago == null) {
        return;
    }

    double precioPrestamo = datosPago[0];
    double garantia = datosPago[1];
    double multaPorDia = datosPago[2];

double multa = diasAtraso * multaPorDia;
double pagoTotal;

if (nuevoEstado.equalsIgnoreCase("Devuelto")) {
    pagoTotal = 0.00;

} else if (nuevoEstado.equalsIgnoreCase("Perdido")) {
    pagoTotal = precioPrestamo + garantia + multa + 100.00;

} else {
    pagoTotal = precioPrestamo + garantia + multa;
}

TblPrestamos.setValueAt(nuevoEstado, fila, COL_ESTADO);
TblPrestamos.setValueAt("S/ " + String.format("%.2f", pagoTotal), fila, COL_MULTA);
}

private void configurarEditorColumnaLibro() {

    JTextField txtEditorLibro = new JTextField();

    ((AbstractDocument) txtEditorLibro.getDocument()).setDocumentFilter(new DocumentFilter() {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            if (string == null) {
                return;
            }

            if (string.matches("\\d+")) {
                super.insertString(fb, offset, string, attr);
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Coloca una ID válida."
                    );
                });
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null) {
                return;
            }

            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Coloca una ID válida."
                    );
                });
            }
        }
    });

    Timer temporizadorLibro = new Timer(2000, e -> {

        String texto = txtEditorLibro.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        int idLibro = Integer.parseInt(texto);

        String titulo = prestamos_Datos.obtenerTituloLibroDisponible(idLibro);

        int fila = TblPrestamos.getEditingRow();

        if (fila == -1) {
            fila = filaEditando;
        }

        if (titulo == null) {

            String mensaje = prestamos_Datos.getMensaje();

            if (mensaje != null && mensaje.toLowerCase().contains("disponible")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Prueba otro libro, no hay ejemplares disponibles."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "El libro no está registrado."
                );
            }

            idLibroSeleccionadoEdicion = -1;

            txtEditorLibro.setText("");

            if (fila != -1) {
                TblPrestamos.setValueAt("", fila, COL_LIBRO);
            }

            return;
        }

        idLibroSeleccionadoEdicion = idLibro;

        if (TblPrestamos.isEditing()) {
            TblPrestamos.getCellEditor().stopCellEditing();
        }

        TblPrestamos.setValueAt(titulo, fila, COL_LIBRO);
    });

    temporizadorLibro.setRepeats(false);

    txtEditorLibro.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            temporizadorLibro.restart();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            temporizadorLibro.restart();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            temporizadorLibro.restart();
        }
    });

    TblPrestamos.getColumnModel().getColumn(COL_LIBRO).setCellEditor(new DefaultCellEditor(txtEditorLibro));
}

private void configurarTxtDniUsuario() {

    Timer temporizadorValidarDni = new Timer(2000, e -> {

        String dni = txtDniUsuario.getText().trim();

        if (dni.length() != 8) {
            return;
        }

        boolean existe = prestamos_Datos.existeClientePorDni(dni);

        if (!existe) {
            JOptionPane.showMessageDialog(
                    this,
                    "Poner un usuario ID válido."
            );

            txtDniUsuario.setText("");
            txtDniUsuario.requestFocus();
        }
    });

    temporizadorValidarDni.setRepeats(false);

    ((AbstractDocument) txtDniUsuario.getDocument()).setDocumentFilter(new DocumentFilter() {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            if (string == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + string + textoActual.substring(offset);

            if (!string.matches("\\d+")) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Carácter no válido."
                    );
                });
                return;
            }

            if (nuevoTexto.length() > 8) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "El DNI debe tener máximo 8 dígitos."
                    );
                });
                return;
            }

            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + text + textoActual.substring(offset + length);

            if (!text.matches("\\d*")) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Carácter no válido."
                    );
                });
                return;
            }

            if (nuevoTexto.length() > 8) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "El DNI debe tener máximo 8 dígitos."
                    );
                });
                return;
            }

            super.replace(fb, offset, length, text, attrs);
        }
    });

    txtDniUsuario.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validarCuandoLlegueAOchoDigitos(temporizadorValidarDni);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validarCuandoLlegueAOchoDigitos(temporizadorValidarDni);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validarCuandoLlegueAOchoDigitos(temporizadorValidarDni);
        }
    });
}

private void validarCuandoLlegueAOchoDigitos(Timer temporizadorValidarDni) {

    String dni = txtDniUsuario.getText().trim();

    if (dni.length() == 8) {
        temporizadorValidarDni.restart();
    } else {
        temporizadorValidarDni.stop();
    }
}

private void configurarVentanaRedimensionable() {
    setResizable(true);
    setMinimumSize(new java.awt.Dimension(1000, 650));
    setLocationRelativeTo(null);

    TblPrestamos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
}

private void configurarTxtCodigoLibro() {

    Timer temporizadorValidarLibro = new Timer(2000, e -> {

        String codigoTexto = txtCodigoLibro.getText().trim();

        if (codigoTexto.length() != 2) {
            return;
        }

        int idLibro;

        try {
            idLibro = Integer.parseInt(codigoTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Poner un código de libro válido.");
            txtCodigoLibro.setText("");
            txtCodigoLibro.requestFocus();
            return;
        }

        String titulo = prestamos_Datos.obtenerTituloLibroDisponible(idLibro);

        if (titulo == null) {

            String mensaje = prestamos_Datos.getMensaje();

            if (mensaje != null && mensaje.toLowerCase().contains("disponible")) {
                JOptionPane.showMessageDialog(
                        this,
                        "Prueba otro libro, no hay ejemplares disponibles."
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Poner un código de libro válido."
                );
            }

            txtCodigoLibro.setText("");
            txtCodigoLibro.requestFocus();
        }
    });

    temporizadorValidarLibro.setRepeats(false);

    ((AbstractDocument) txtCodigoLibro.getDocument()).setDocumentFilter(new DocumentFilter() {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            if (string == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + string + textoActual.substring(offset);

            if (!string.matches("\\d+")) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Carácter no válido."
                    );
                });
                return;
            }

            if (nuevoTexto.length() > 2) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "El código del libro debe tener máximo 2 dígitos."
                    );
                });
                return;
            }

            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            if (text == null) {
                return;
            }

            String textoActual = fb.getDocument().getText(0, fb.getDocument().getLength());
            String nuevoTexto = textoActual.substring(0, offset) + text + textoActual.substring(offset + length);

            if (!text.matches("\\d*")) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "Carácter no válido."
                    );
                });
                return;
            }

            if (nuevoTexto.length() > 2) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            FrmPrestamos.this,
                            "El código del libro debe tener máximo 2 dígitos."
                    );
                });
                return;
            }

            super.replace(fb, offset, length, text, attrs);
        }
    });

    txtCodigoLibro.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validarCuandoLlegueADosDigitos(temporizadorValidarLibro);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validarCuandoLlegueADosDigitos(temporizadorValidarLibro);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validarCuandoLlegueADosDigitos(temporizadorValidarLibro);
        }
    });
}

private void validarCuandoLlegueADosDigitos(Timer temporizadorValidarLibro) {

    String codigoTexto = txtCodigoLibro.getText().trim();

    if (codigoTexto.length() == 2) {
        temporizadorValidarLibro.restart();
    } else {
        temporizadorValidarLibro.stop();
    }
}

private void restaurarDatosSiQuedaronVacios() {

    if (filaEditando == -1) {
        return;
    }

    Object valorDni = TblPrestamos.getValueAt(filaEditando, COL_DNI);
    Object valorUsuario = TblPrestamos.getValueAt(filaEditando, COL_USUARIO);
    Object valorLibro = TblPrestamos.getValueAt(filaEditando, COL_LIBRO);

    if ((valorDni == null || valorDni.toString().trim().isEmpty()) && !dniAnterior.isEmpty()) {
        TblPrestamos.setValueAt(dniAnterior, filaEditando, COL_DNI);
        TblPrestamos.setValueAt(usuarioAnterior, filaEditando, COL_USUARIO);
    }

    if ((valorLibro == null || valorLibro.toString().trim().isEmpty()) && !libroAnterior.isEmpty()) {
        TblPrestamos.setValueAt(libroAnterior, filaEditando, COL_LIBRO);
    }
}

private void configurarEfectoClickVentana() {

    Color colorOriginal = jPanel1.getBackground();
    Color colorClick = new Color(180, 255, 255);

    jPanel1.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {

            TblPrestamos.clearSelection();

            if (TblPrestamos.isEditing()) {
                TblPrestamos.getCellEditor().stopCellEditing();
            }

            jPanel1.setBackground(colorClick);

            Timer timer = new Timer(180, ev -> {
                jPanel1.setBackground(colorOriginal);
            });

            timer.setRepeats(false);
            timer.start();
        }
    });
}

private Color mezclarColor(Color base, Color efecto, float alpha) {

    int r = (int) (base.getRed() * (1 - alpha) + efecto.getRed() * alpha);
    int g = (int) (base.getGreen() * (1 - alpha) + efecto.getGreen() * alpha);
    int b = (int) (base.getBlue() * (1 - alpha) + efecto.getBlue() * alpha);

    return new Color(r, g, b);
}

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new FrmPrestamos().setVisible(true));
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TblPrestamos;
    private javax.swing.JButton btnCalendario;
    private javax.swing.JButton btnDetalles;
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
    private javax.swing.JLabel jLabel6;
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
