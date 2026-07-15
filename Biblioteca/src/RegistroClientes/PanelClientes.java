package RegistroClientes;

import VistaDeProyecto.frmMenu;
import ClaseBase.Cliente;
import LogicaClientes.GestionCliente;
import LogicaClientes.ValidadorTexto;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DocumentFilter;
import java.util.function.IntSupplier;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class PanelClientes extends javax.swing.JPanel {

    private frmMenu frame;//comunicacion entre panel clientes y frmprincipal
    DefaultTableModel modelo;
    GestionCliente gestion = new GestionCliente();
    //Estado de los toggles (booleanos) de validación
    private boolean esExtranjero = false; // controlado por chkExtranjero (DNI 8 o CE 9 díg)
    private boolean telefonoModoPeru = true; // controlado por btnTipoTelefono ( +51 fijo o código libre )

    public PanelClientes(frmMenu frame) {
        initComponents();
        //Cambiar fondo de scrollpane
        tblClientes.getViewport().setBackground(new java.awt.Color(153, 255, 255));
        tblClientes.setBackground(new java.awt.Color(153, 255, 255));
        this.frame = frame;

        //Configuración del modelo de la tabla
        modelo = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("DNI");
        modelo.addColumn("Nombre");
        modelo.addColumn("Apellido");
        modelo.addColumn("Correo");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Multa");
        modelo.addColumn("Estado");
        tblResultCliente.setModel(modelo);
        //Cargar datos a la tabla
        cargarTabla();

        //Configurar la búsqueda 
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });

        //Detectar selección de fila para llenar el formulario con datos
        tblResultCliente.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    cargarCamposDesdeFila();
                }
            }
        });

        //Explicitar con qué campos realizar búsqueda
        txtBuscar.setToolTipText("Busca por DNI, nombre o apellido");
        //DNI: permitir solo dígitos, longitud max 8 (DNI) o 9 (CE)
        ((javax.swing.text.AbstractDocument) txtDNI.getDocument()).setDocumentFilter(new SoloDigitosFilter(() -> esExtranjero ? 9 : 8));
        //Nombre y apellidos: permitir solo letras y espacio simple, capitaliza al salir del campo.
        ((javax.swing.text.AbstractDocument) txtNombre.getDocument()).setDocumentFilter(new SoloLetrasFilter());
        ((javax.swing.text.AbstractDocument) txtApellido.getDocument()).setDocumentFilter(new SoloLetrasFilter());
        txtNombre.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                txtNombre.setText(ValidadorTexto.capitalizar(txtNombre.getText()));
            }
        });
        txtApellido.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                txtApellido.setText(ValidadorTexto.capitalizar(txtApellido.getText()));
            }
        });
        // --- Teléfono: por defecto modo Perú (+51 fijo, no editable) ---
        aplicarModoTelefonoPeru();
        btnTipoTelefono.addActionListener(evt -> {
            telefonoModoPeru = !telefonoModoPeru;
            if (telefonoModoPeru) {
                aplicarModoTelefonoPeru();
            } else {
                aplicarModoTelefonoExtranjero();
            }
        });
    }

//Método formato telefono Perú
    private void aplicarModoTelefonoPeru() {
        btnTipoTelefono.setText("PE"); //PERÚ
        btnTipoTelefono.setToolTipText("Modo Perú (+51). Click para cambiar a Extranjero.");
        txtCodigoPais.setText("+51");
        txtCodigoPais.setEditable(false);
        txtNumero.setText("");
    }
//Método formato telefono internacional

    private void aplicarModoTelefonoExtranjero() {
        btnTipoTelefono.setText("INTL"); //INTERNACIONAL
        btnTipoTelefono.setToolTipText("Modo Extranjero (código de país libre). Click para volver a Perú.");
        txtCodigoPais.setText("+");
        txtCodigoPais.setEditable(true);
        txtNumero.setText("");
    }

//DocumentFilter que solo permite digitos, hasta un máximo
    private static class SoloDigitosFilter extends DocumentFilter {

        private final IntSupplier maxLength;

        public SoloDigitosFilter(IntSupplier maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            String limpio = text.replaceAll("[^0-9]", "");
            int totalFinal = fb.getDocument().getLength() - length + limpio.length();
            if (totalFinal > maxLength.getAsInt()) {
                limpio = limpio.substring(0, Math.max(0, limpio.length() - (totalFinal - maxLength.getAsInt())));
            }
            super.replace(fb, offset, length, limpio, attrs);
        }
    }
    //DocumentFilter que solo permite letras(con tildes/ñ) y espacio simple

    private static class SoloLetrasFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text == null) {
                return;
            }
            String limpio = text.replaceAll("[^A-Za-zÁÉÍÓÚáéíóúÑñÜü ]", "");
            super.replace(fb, offset, length, limpio, attrs);
        }
    }

    private void cargarTabla() {
        filtrar();
    }

    //Método que se ejecuta al escribir en campo búsqueda o al marcar chkbox deudores
    private void filtrar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        boolean soloDeudores = chkSoloDeudores.isSelected();
        modelo.setRowCount(0);
        try {
            for (Cliente c : gestion.listarTodos()) {
                String dni = c.getDni().toLowerCase();
                String nombreCompleto = (c.getPrimerNombre() + " " + c.getPrimerApellido()).toLowerCase();
                if (!dni.contains(texto) && !nombreCompleto.contains(texto)) {
                    continue;
                }
                String estado = gestion.calcularEstado(c.getDni());
                if (soloDeudores && !"DEUDOR".equals(estado)) {
                    continue;
                }
                double multa = gestion.calcularMulta(c.getDni());
                modelo.addRow(new Object[]{
                    c.getDni(), c.getPrimerNombre(), c.getPrimerApellido(),
                    c.getCorreo(), c.getTelefono(), "S/ " + multa, estado
                });
            }
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al filtrar: " + e.getMessage());
        }
    }

    //Metodo que llena formulario al seleccionar una fila de tabla
    private void cargarCamposDesdeFila() {
        int fila = tblResultCliente.getSelectedRow();
        if (fila == -1) {
            return;
        }
        String dni = modelo.getValueAt(fila, 0).toString();
        txtDNI.setText(dni);
        txtNombre.setText(modelo.getValueAt(fila, 1).toString());
        txtApellido.setText(modelo.getValueAt(fila, 2).toString());
        txtCorreo.setText(modelo.getValueAt(fila, 3).toString());

        chkExtranjero.setSelected(dni.length() == 9);
        esExtranjero = chkExtranjero.isSelected();

        String telefono = modelo.getValueAt(fila, 4).toString();
        if (telefono.startsWith("+51")) {
            telefonoModoPeru = true;
            aplicarModoTelefonoPeru();
            txtNumero.setText(telefono.replaceFirst("^\\+51", ""));
        } else {
            telefonoModoPeru = false;
            aplicarModoTelefonoExtranjero();
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(\\+\\d+)(\\d*)$").matcher(telefono);
            if (m.matches()) {
                txtCodigoPais.setText(m.group(1));
                txtNumero.setText(m.group(2));
            } else {
                txtNumero.setText(telefono);
            }
        }
        txtDNI.setEditable(false); // ya existe, no se cambia el DNI
    }

    public void enviarClienteActualAPrestamos() {
        String dni = txtDNI.getText().trim();
        if (!dni.isEmpty()) {
            frame.enviarDniAPrestamos(dni);
            frame.mostrarPanel("prestamos"); // regresa automático a Préstamos
        }
    }

    private void limpiarCampos() {
        txtDNI.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtCorreo.setText("");
        chkExtranjero.setSelected(false);
        esExtranjero = false;
        telefonoModoPeru = true;
        aplicarModoTelefonoPeru();
        txtDNI.setEditable(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar1 = new javax.swing.JButton();
        chkSoloDeudores = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        txtCorreo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDNI = new javax.swing.JTextField();
        txtNumero = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        chkExtranjero = new javax.swing.JCheckBox();
        btnTipoTelefono = new javax.swing.JButton();
        txtCodigoPais = new javax.swing.JTextField();
        tblClientes = new javax.swing.JScrollPane();
        tblResultCliente = new javax.swing.JTable();

        setBackground(new java.awt.Color(153, 255, 255));
        setPreferredSize(new java.awt.Dimension(1100, 550));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(153, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("Gestión de Clientes");
        jLabel1.setPreferredSize(new java.awt.Dimension(121, 35));
        jPanel1.add(jLabel1, java.awt.BorderLayout.NORTH);

        jPanel2.setBackground(new java.awt.Color(153, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(153, 255, 255));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        txtBuscar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtBuscar.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtBuscar.setPreferredSize(new java.awt.Dimension(200, 30));
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        jPanel3.add(txtBuscar);

        btnBuscar1.setBackground(new java.awt.Color(153, 255, 255));
        btnBuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/RecursosDaniel/magnifying-glass (1).png"))); // NOI18N
        btnBuscar1.setBorderPainted(false);
        btnBuscar1.setPreferredSize(new java.awt.Dimension(35, 35));
        btnBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscar1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnBuscar1);

        chkSoloDeudores.setText("Solo deudores");
        chkSoloDeudores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSoloDeudoresActionPerformed(evt);
            }
        });
        jPanel3.add(chkSoloDeudores);

        jPanel2.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        jPanel4.setBackground(new java.awt.Color(153, 255, 255));
        jPanel4.setForeground(new java.awt.Color(153, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 50));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jToolBar1.setFloatable(true);
        jToolBar1.setRollover(true);
        jToolBar1.setPreferredSize(new java.awt.Dimension(300, 30));

        btnNuevo.setText("Nuevo");
        btnNuevo.setFocusable(false);
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setPreferredSize(new java.awt.Dimension(100, 40));
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNuevo);

        btnGuardar.setText("Guardar");
        btnGuardar.setFocusable(false);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setPreferredSize(new java.awt.Dimension(100, 40));
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGuardar);

        btnEliminar.setText("Eliminar");
        btnEliminar.setFocusable(false);
        btnEliminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEliminar.setPreferredSize(new java.awt.Dimension(100, 40));
        btnEliminar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEliminar);

        jPanel4.add(jToolBar1);

        jPanel2.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setBackground(new java.awt.Color(153, 255, 255));
        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(1220, 60));
        jPanel5.setRequestFocusEnabled(false);

        txtCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreoActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jLabel4.setText("Teléfono:");

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jLabel7.setText("Correo:");

        jLabel8.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jLabel8.setText("Primer Nombre:");

        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jLabel9.setText("DNI:");

        jLabel10.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jLabel10.setText("Primer Apellido:");

        chkExtranjero.setText("Es extranjero (CE)");
        chkExtranjero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkExtranjeroActionPerformed(evt);
            }
        });

        btnTipoTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoTelefonoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTipoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCodigoPais, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkExtranjero)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkExtranjero))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtCodigoPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnTipoTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);
        jPanel2.getAccessibleContext().setAccessibleName("");

        add(jPanel1, java.awt.BorderLayout.NORTH);

        tblClientes.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        tblResultCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblResultCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblResultClienteMouseClicked(evt);
            }
        });
        tblClientes.setViewportView(tblResultCliente);

        add(tblClientes, java.awt.BorderLayout.CENTER);
        tblClientes.getAccessibleContext().setAccessibleName("");
        tblClientes.getAccessibleContext().setAccessibleDescription("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiarCampos();
        tblResultCliente.clearSelection();
        txtDNI.requestFocus();
        txtBuscar.setText("");
        chkSoloDeudores.setSelected(false);
        filtrar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        filtrar();
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tblResultCliente.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente primero");
            return;
        }
        String dni = modelo.getValueAt(fila, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar cliente con DNI " + dni + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean eliminado = gestion.eliminar(dni);
                if (eliminado) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se puede eliminar. El cliente tiene historial de préstamos.");
                }
            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscar1ActionPerformed
        filtrar();
    }//GEN-LAST:event_btnBuscar1ActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        String dni = txtDNI.getText().trim();
        String nombre = ValidadorTexto.capitalizar(txtNombre.getText().trim());
        String apellido = ValidadorTexto.capitalizar(txtApellido.getText().trim());
        String correo = txtCorreo.getText().trim();
        String codigoPais = txtCodigoPais.getText().trim();
        String numero = txtNumero.getText().trim();
        //Validar DNI o CE
        if (!ValidadorTexto.esDniValido(dni, esExtranjero)) {
            JOptionPane.showMessageDialog(this,
                    esExtranjero
                            ? "El Carné de Extranjería debe tener exactamente 9 dígitos numéricos, sin espacios."
                            : "El DNI debe tener exactamente 8 dígitos numéricos, sin espacios.",
                    "DNI inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ValidadorTexto.pareceTypo(dni)) {
            int resp = JOptionPane.showConfirmDialog(this,
                    "El número \"" + dni + "\" parece un posible error de tipeo (dígitos repetidos o "
                    + "consecutivos).\n¿Confirma que realmente desea ingresar " + (esExtranjero ? "un extranjero" : "este DNI") + "?",
                    "Confirmar dato", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (resp != JOptionPane.YES_OPTION) {
                return;
            }
        }
        //Validar Nombre y Apellido 
        if (!ValidadorTexto.esNombreValido(nombre)) {
            JOptionPane.showMessageDialog(this,
                    "El nombre solo debe contener letras, sin números ni espacios extra.",
                    "Nombre inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!ValidadorTexto.esNombreValido(apellido)) {
            JOptionPane.showMessageDialog(this,
                    "El apellido solo debe contener letras, sin números ni espacios extra.",
                    "Apellido inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar Correo
        if (!ValidadorTexto.esCorreoValido(correo)) {
            JOptionPane.showMessageDialog(this,
                    "El correo debe contener \"@\" y \".\", sin espacios (ej: nombre@correo.com).",
                    "Correo inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Teléfono (Perú: (+51) + 9 dígitos empezando en 9 o Extranjero: +código + número) 
        if (telefonoModoPeru) {
            if (!ValidadorTexto.esNumeroPeruValido(numero)) {
                JOptionPane.showMessageDialog(this,
                        "El número peruano debe tener 9 dígitos y empezar en 9 (ej: 987654321).",
                        "Teléfono inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (!ValidadorTexto.esCodigoPaisExtranjeroValido(codigoPais)) {
                JOptionPane.showMessageDialog(this,
                        "El código de país debe ser \"+\" seguido de al menos un número (ej: +1).",
                        "Código de país inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!ValidadorTexto.esNumeroExtranjeroValido(numero)) {
                JOptionPane.showMessageDialog(this,
                        "El número de teléfono no puede estar vacío ni tener espacios.",
                        "Teléfono inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String telefono = codigoPais + numero;

        //Creamos objeto cliente
        Cliente c = new Cliente(dni, nombre, apellido, telefono, correo);

        try {
            if (txtDNI.isEditable()) {
                //Para cliente nuevo
                boolean agregado = gestion.agregar(c);
                JOptionPane.showMessageDialog(this,
                        agregado ? "Cliente registrado" : "No se pudo registrar el cliente");
            } else {
                // Para modificar cliente
                boolean actualizado = gestion.actualizar(c);
                JOptionPane.showMessageDialog(this,
                        actualizado ? "Cliente actualizado" : "No se pudo actualizar el cliente");
            }
            cargarTabla();
            limpiarCampos();
        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void tblResultClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblResultClienteMouseClicked
        if (evt.getClickCount() == 2) {
            enviarClienteActualAPrestamos();
        }
    }//GEN-LAST:event_tblResultClienteMouseClicked

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

    private void txtCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoActionPerformed

    private void chkSoloDeudoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSoloDeudoresActionPerformed
        chkSoloDeudores.setToolTipText("Seleccione opción y haga click en lupa para visualizar solo deudores");
    }//GEN-LAST:event_chkSoloDeudoresActionPerformed

    private void chkExtranjeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkExtranjeroActionPerformed

        esExtranjero = chkExtranjero.isSelected();
        txtDNI.setText("");
        txtDNI.setToolTipText(esExtranjero
                ? "Ingresa 9 dígitos (CE)"
                : "Ingresa 8 dígitos (DNI)");
    }//GEN-LAST:event_chkExtranjeroActionPerformed

    private void btnTipoTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTipoTelefonoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar1;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnTipoTelefono;
    private javax.swing.JCheckBox chkExtranjero;
    private javax.swing.JCheckBox chkSoloDeudores;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JScrollPane tblClientes;
    private javax.swing.JTable tblResultCliente;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigoPais;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumero;
    // End of variables declaration//GEN-END:variables
}
