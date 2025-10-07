/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;

/**
 * Pantalla de inicio para el rol de Administrador.
 * Muestra un mensaje de bienvenida y actúa como placeholder para futuras funcionalidades.
 */
public class AdministradorPantalla extends JFrame {
    
    // Colores basados en la maqueta de Login (Tierra/Oliva)
    private static final Color COLOR_FONDO_ADMIN = new Color(139, 69, 19); // Marrón Oscuro (Saddle Brown)
    private static final Color COLOR_PANEL_CONTENIDO = new Color(210, 180, 140); // Marrón Claro (Tan)
    private static final Color COLOR_TEXTO_TITULO = Color.WHITE;
    private static final Color COLOR_TEXTO_SEGUNDO = new Color(85, 107, 47); // Verde Oliva Oscuro

    public AdministradorPantalla(Connection con, int idAdmin) {
        // Configuración básica de la ventana
        setTitle("Administración del Sistema - DIETAS EQUINAS (ID: " + idAdmin + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450); 
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO_ADMIN); 

        // 1. Panel principal con layout para el contenido
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBackground(COLOR_FONDO_ADMIN);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 2. Panel de Contenido Central
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_PANEL_CONTENIDO);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // --- Componentes ---
        JLabel lblMensaje = new JLabel("BIENVENIDO, ADMINISTRADOR");
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblMensaje.setForeground(COLOR_TEXTO_SEGUNDO);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblID = new JLabel("ID de Usuario: " + idAdmin);
        lblID.setFont(new Font("SansSerif", Font.ITALIC, 18));
        lblID.setForeground(COLOR_FONDO_ADMIN.darker());
        lblID.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea txtFunciones = new JTextArea(
            "\nFuncionalidades de Administración (Pendientes de implementar):\n" +
            "1. Gestión de usuarios (Alta/Baja de Duenos/Cuidadores).\n" +
            "2. Modificación de parámetros y cantidades de Alimentos.\n" +
            "3. Visualización y exportación de reportes generales del sistema."
        );
        txtFunciones.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtFunciones.setForeground(COLOR_FONDO_ADMIN);
        txtFunciones.setBackground(COLOR_PANEL_CONTENIDO);
        txtFunciones.setEditable(false);
        txtFunciones.setWrapStyleWord(true);
        txtFunciones.setLineWrap(true);
        
        JButton btnCerrarSesion = new JButton("Cerrar Sesión 🚪");
        btnCerrarSesion.setBackground(COLOR_TEXTO_SEGUNDO);
        btnCerrarSesion.setForeground(COLOR_TEXTO_TITULO);
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(200, 40)); 
        
        // --- Adición de componentes al panel central ---
        panelContenido.add(lblMensaje);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(lblID);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));
        panelContenido.add(txtFunciones);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 30)));
        panelContenido.add(btnCerrarSesion);
        
        // Añadir el panel central al marco
        gbc.gridx = 0; gbc.gridy = 0; 
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panelPrincipal.add(panelContenido, gbc);
        
        add(panelPrincipal);

        // --- Listener para cerrar sesión ---
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Estás seguro que deseas cerrar la sesión de Administrador?", 
            "Cerrar Sesión", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            this.dispose();

            try {
                // Regresar a la ventana de Login
                Login loginWindow = new Login();
                loginWindow.setVisible(true); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al intentar volver al Login: " + e.getMessage());
            }
        }
    }
}
