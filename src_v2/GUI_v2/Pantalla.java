package GUI;

import Logica.Gestion_cabalo;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel; 
import java.awt.*;
import java.sql.*;

public class Pantalla extends JFrame {

    // Colores basados en la maqueta de Login (Tierra/Oliva)
    private static final Color COLOR_FONDO = new Color(205, 133, 63); // Marrón claro/Tan
    private static final Color COLOR_PANEL = new Color(102, 51, 0); // Marrón oscuro/Saddle Brown
    private static final Color COLOR_BOTON = new Color(50, 100, 50); // Verde oliva oscuro
    private static final Color COLOR_TEXTO = Color.WHITE;
    private static final Font FONT_TITULO = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_ETIQUETA = new Font("SansSerif", Font.PLAIN, 14);

    // Componentes de la Interfaz
    private final JTextField txtNombreEquino;
    private final JTextField txtIdCaballo; // Código del Caballo
    private final JComboBox<String> cmbEdadEquino;
    private final JComboBox<String> cmbEstadoSalud;
    private final JTable tablaDietas;
    private final DefaultTableModel modeloTabla;
    private final JButton btnGuardarDieta;
    private final JButton btnMisCaballos;
    private final JButton btnCerrarSesion;
    
    // Etiqueta para el título dinámico de la dieta
    private final JLabel lblTituloDieta; 

    // Dependencias de Conexión y Lógica
    private final Connection conexion;
    private final int idDueno;
    private final Gestion_cabalo gestion; // Instancia de la lógica de negocio

    public Pantalla(Connection con, int idDueno) {
        this.conexion = con;
        this.idDueno = idDueno;
        this.gestion = new Gestion_cabalo(con); 

        // --- Configuración básica de la ventana ---
        setTitle("Gestión de Dietas para Equinos (Dueño ID: " + idDueno + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600); 
        setLayout(new BorderLayout(15, 15)); 
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_FONDO); 

        // --- Título de la Aplicación (Norte) ---
        JLabel lblTituloApp = new JLabel("Sistema de Gestión de Dietas Equinas", JLabel.CENTER);
        lblTituloApp.setFont(new Font("Serif", Font.BOLD, 28));
        lblTituloApp.setForeground(COLOR_PANEL);
        add(lblTituloApp, BorderLayout.NORTH);

        // --- 1. Panel Superior (Campos de Entrada) ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); 
        
        // Estilización de Etiquetas y Campos
        JLabel lblNombre = new JLabel("Nombre del Equino:");
        lblNombre.setFont(FONT_ETIQUETA);
        txtNombreEquino = new JTextField(15);
        txtNombreEquino.setFont(FONT_ETIQUETA);

        JLabel lblId = new JLabel("Código Caballo:");
        lblId.setFont(FONT_ETIQUETA);
        txtIdCaballo = new JTextField(10); 
        txtIdCaballo.setFont(FONT_ETIQUETA);

        panelSuperior.add(lblNombre);
        panelSuperior.add(txtNombreEquino);
        panelSuperior.add(lblId);
        panelSuperior.add(txtIdCaballo);

        add(panelSuperior, BorderLayout.NORTH);

        // --- 2. Panel Central (Tabla y Controles Laterales) ---
        JPanel panelCentral = new JPanel(new BorderLayout(15, 15));
        panelCentral.setBackground(COLOR_FONDO);
        
        // Título Dinámico
        lblTituloDieta = new JLabel("Dieta Sugerida (Aún sin caballo registrado)", JLabel.CENTER);
        lblTituloDieta.setHorizontalAlignment(SwingConstants.CENTER);
        lblTituloDieta.setFont(FONT_TITULO);
        lblTituloDieta.setForeground(COLOR_PANEL);
        
        // ** 2b. Tabla de Dietas **
        String[] columnas = {"Alimento", "Característica", "Cantidad"}; 

        modeloTabla = new DefaultTableModel(columnas, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
        };
        tablaDietas = new JTable(modeloTabla);
        
        // Estilo de la tabla
        tablaDietas.getTableHeader().setBackground(COLOR_PANEL.darker());
        tablaDietas.getTableHeader().setForeground(COLOR_TEXTO);
        tablaDietas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tablaDietas.setGridColor(COLOR_PANEL.brighter());
        tablaDietas.setRowHeight(25);

        JScrollPane scrollTabla = new JScrollPane(tablaDietas);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder()); 
        
        // Creamos un panel para el título dinámico y la tabla
        JPanel panelDieta = new JPanel(new BorderLayout());
        panelDieta.setBackground(COLOR_FONDO);
        panelDieta.setBorder(BorderFactory.createLineBorder(COLOR_PANEL.brighter(), 1)); 
        panelDieta.add(lblTituloDieta, BorderLayout.NORTH); 
        panelDieta.add(scrollTabla, BorderLayout.CENTER); 

        panelCentral.add(panelDieta, BorderLayout.CENTER);

        // ** 2c. Panel Lateral Derecho (Edad y Salud) **
        JPanel panelLateralDerecho = new JPanel();
        panelLateralDerecho.setLayout(new BoxLayout(panelLateralDerecho, BoxLayout.Y_AXIS));
        panelLateralDerecho.setBackground(COLOR_FONDO);
        panelLateralDerecho.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Edad del equino
        JLabel lblEdad = new JLabel("Edad del equino");
        lblEdad.setFont(FONT_ETIQUETA);
        panelLateralDerecho.add(lblEdad);
        String[] edades = {"Potro joven: 1 - 2 años", "Adolescente: 2-4 años", "Mayor: 4-15 años"};
        cmbEdadEquino = new JComboBox<>(edades);
        cmbEdadEquino.setFont(FONT_ETIQUETA);
        cmbEdadEquino.setMaximumSize(new Dimension(220, 30));
        panelLateralDerecho.add(cmbEdadEquino);
        
        panelLateralDerecho.add(Box.createVerticalStrut(30));
        
        // Estado de salud (Afeccion)
        JLabel lblSalud = new JLabel("Estado de salud (Afección)");
        lblSalud.setFont(FONT_ETIQUETA);
        panelLateralDerecho.add(lblSalud);
        String[] estados = {"Salud optima", "Deshidratacion", "Desnutricion", "Parasitos"};
        cmbEstadoSalud = new JComboBox<>(estados);
        cmbEstadoSalud.setFont(FONT_ETIQUETA);
        cmbEstadoSalud.setMaximumSize(new Dimension(220, 30));
        panelLateralDerecho.add(cmbEstadoSalud);
        
        // --- INICIO DE LA MODIFICACIÓN: Añadir la imagen ---
        panelLateralDerecho.add(Box.createVerticalStrut(20));
        
        // Carga y escala la imagen
        try {
            // ❗ RUTA CORREGIDA: Se agregaron las comillas dobles y se usaron barras diagonales.
            String rutaAbsoluta = "C:/Users/ACER/Downloads/png-transparent-arabian-horse-stallion-euclidean-illustration-white-horse-and-black-horse-horse-animals-fictional-character.png"; 
            
            ImageIcon originalIcon = new ImageIcon(rutaAbsoluta); 
            Image originalImage = originalIcon.getImage();
            
            // Redimensionar la imagen a un tamaño apropiado (ej: 200x150)
            Image scaledImage = originalImage.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            
            JLabel lblImagen = new JLabel(scaledIcon);
            lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT); 
            lblImagen.setBorder(BorderFactory.createLineBorder(COLOR_PANEL, 3)); 
            
            panelLateralDerecho.add(lblImagen);
        } catch (Exception e) {
            // Mensaje de error si la imagen no se encuentra 
            JLabel lblError = new JLabel("Error al cargar imagen 🐴");
            lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelLateralDerecho.add(lblError);
        }
        
        // --- FIN DE LA MODIFICACIÓN ---
        
        panelLateralDerecho.add(Box.createVerticalGlue()); // El 'glue' empuja todo lo anterior hacia arriba
        
        panelCentral.add(panelLateralDerecho, BorderLayout.EAST);
        
        add(panelCentral, BorderLayout.CENTER);

        // --- 3. Panel Inferior (Botones) ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelInferior.setBackground(COLOR_FONDO);
        
        btnGuardarDieta = new JButton("Guardar y Mostrar Dieta 💾");
        btnMisCaballos = new JButton("Mis Caballos 🐴");
        btnCerrarSesion = new JButton("Cerrar Sesión 🚪");

        // Estilo de botones
        styleButton(btnGuardarDieta);
        styleButton(btnMisCaballos);
        styleButton(btnCerrarSesion);
        
        panelInferior.add(btnGuardarDieta);
        panelInferior.add(btnMisCaballos);
        panelInferior.add(btnCerrarSesion);
        
        add(panelInferior, BorderLayout.SOUTH);

        // --- Agregar Listeners ---
        btnGuardarDieta.addActionListener(e -> guardarCaballo());
        btnMisCaballos.addActionListener(e -> mostrarRegistro()); 
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
    }

    // Método de utilidad para aplicar estilos a los botones
    private void styleButton(JButton button) {
        button.setBackground(COLOR_BOTON);
        button.setForeground(COLOR_TEXTO);
        button.setFont(FONT_ETIQUETA);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(COLOR_PANEL, 2));
        button.setPreferredSize(new Dimension(200, 40));
    }

    // --- LÓGICA DE NEGOCIO INTEGRADA (Botón Guardar) ---
    private void guardarCaballo() {
        String nombre = txtNombreEquino.getText().trim();
        String codigo = txtIdCaballo.getText().trim();
        String edad = (String) cmbEdadEquino.getSelectedItem();
        String afeccion = (String) cmbEstadoSalud.getSelectedItem();

        if (nombre.isEmpty() || codigo.isEmpty() || edad == null || afeccion == null) {
            JOptionPane.showMessageDialog(this, "Por favor llena todos los campos.");
            return;
        }

        // 1. Llamar a la lógica de negocio para guardar (Caballo y Dieta)
        gestion.guardarCaballoYDietas(idDueno, nombre, codigo, edad, afeccion); 
        
        // 2. Buscar el ID del caballo recién creado (para mostrar su dieta)
        int idCaballoGuardado = gestion.obtenerUltimoCaballoPorCodigo(codigo); 
        
        // 3. Mostrar la dieta en la tabla
        if (idCaballoGuardado != -1) {
            gestion.mostrarDietasEnTabla(tablaDietas, idCaballoGuardado); 
            
            // ACTUALIZAR EL TÍTULO DINÁMICO
            lblTituloDieta.setText("Dieta Sugerida para: " + nombre + " (ID: " + idCaballoGuardado + ")");

            // Limpiar campos de entrada
            txtNombreEquino.setText("");
            txtIdCaballo.setText("");
            cmbEdadEquino.setSelectedIndex(0);
            cmbEstadoSalud.setSelectedIndex(0);
        }
    }
    
    // --- LÓGICA DE NEGOCIO INTEGRADA (Botón Registro/Mis Caballos) ---
    private void mostrarRegistro() {
        // Se asume que RegistroPantalla existe
        // Aquí se llama a la clase que debe implementar para ver los registros 
        // Si no existe, este código generará un error de compilación/ejecución
        // Nota: Asegúrate de que las clases 'RegistroPantalla' y 'Login' existan en tu proyecto.
        RegistroPantalla registro = new RegistroPantalla(conexion, idDueno); 
        registro.setVisible(true);
    }
    
    // --- LÓGICA (Botón Cerrar Sesión) ---
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Estás seguro que deseas cerrar la sesión?", 
            "Cerrar Sesión", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            this.dispose();

            try {
                // Se asume que la clase Login existe
                Login loginWindow = new Login();
                loginWindow.setVisible(true); 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al intentar volver al Login: " + e.getMessage());
            }
        }
    }
}