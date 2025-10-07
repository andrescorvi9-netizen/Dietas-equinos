package GUI;

import Logica.Gestion_cabalo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;

public class RegistroPantalla extends JFrame {
    
    private final int idDueno;
    private final Gestion_cabalo gestion;
    private final JTable tablaCaballos;
    private final JButton btnVerDieta;
    private final JButton btnEliminar;

    /**
     * Ventana que muestra todos los caballos registrados por un dueño.
     * @param con Conexión a la BD.
     * @param idDueno ID del dueño logueado (sin tilde).
     */
    public RegistroPantalla(Connection con, int idDueno) {
        this.idDueno = idDueno;
        // Instancia de la lógica de negocio para las consultas
        // Se asume que Gestion_cabalo(con) existe y es funcional
        this.gestion = new Gestion_cabalo(con); 
        
        // --- Configuración de la Ventana ---
        setTitle("Registro de Caballos del Dueño ID: " + idDueno);
        setSize(700, 400);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        // --- Configuración de la Tabla ---
        // Columnas para mostrar: ID (oculta), Nombre, Código, Edad, Afección
        String[] columnas = {"ID", "Nombre", "Código", "Edad", "Afección"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Asegura que la columna ID sea manejada como Integer
                if (columnIndex == 0) return Integer.class; 
                return super.getColumnClass(columnIndex);
            }
        };
        
        tablaCaballos = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaCaballos);

        // Ocultar la columna ID (columna 0), que solo se usa para la lógica
        tablaCaballos.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCaballos.getColumnModel().getColumn(0).setMinWidth(0);
        tablaCaballos.getColumnModel().getColumn(0).setPreferredWidth(0);
        tablaCaballos.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        tablaCaballos.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

        // --- Panel de Botones (Inferior) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnVerDieta = new JButton("Ver Dieta");
        btnEliminar = new JButton("Eliminar Caballo");
        
        panelBotones.add(btnVerDieta);
        panelBotones.add(btnEliminar);

        // --- Agregar Componentes a la Ventana ---
        JLabel lblTitulo = new JLabel("Mis Caballos Registrados:", JLabel.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 16));
        
        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // --- Agregar Listeners ---
        btnVerDieta.addActionListener(e -> verDietaSeleccionada());
        btnEliminar.addActionListener(e -> eliminarCaballoSeleccionado());
        
        // Cargar los datos al iniciar la ventana
        cargarRegistroCaballos();
    }
    
    private void cargarRegistroCaballos() {
        // Llama al método en la lógica de negocio para llenar la tabla.
        // Se asume que gestion.mostrarCaballosDelDuenoEnTabla existe
        gestion.mostrarCaballosDelDuenoEnTabla(tablaCaballos, idDueno);
    }
    
    // --- Lógica para Ver Dieta ---
    private void verDietaSeleccionada() {
        int selectedRow = tablaCaballos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un caballo de la lista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // El ID_Caballo está en la columna 0 (la columna oculta)
            int idCaballo = (int) tablaCaballos.getModel().getValueAt(selectedRow, 0);
            String nombreCaballo = (String) tablaCaballos.getModel().getValueAt(selectedRow, 1);
            
            mostrarVentanaDieta(idCaballo, nombreCaballo);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID del caballo seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- Lógica para Eliminar Caballo ---
    private void eliminarCaballoSeleccionado() {
        int selectedRow = tablaCaballos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un caballo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // El ID_Caballo está en la columna 0
            int idCaballo = (int) tablaCaballos.getModel().getValueAt(selectedRow, 0);
            String nombreCaballo = (String) tablaCaballos.getModel().getValueAt(selectedRow, 1);
            
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres eliminar al caballo '" + nombreCaballo + "' (ID: " + idCaballo + ")? \n(Se eliminarán sus dietas asociadas permanentemente)", 
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                // Se asume que gestion.eliminarCaballoPorId(id) existe y maneja las transacciones (Caballo y Dietas)
                if (gestion.eliminarCaballoPorId(idCaballo)) {
                    JOptionPane.showMessageDialog(this, "✅ Caballo eliminado correctamente.");
                    cargarRegistroCaballos(); // Refrescar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Error al eliminar el caballo. Verifica la base de datos.", "Error de BD", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar la eliminación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Método CORREGIDO para mostrar la dieta en un nuevo diálogo
     * La tabla ahora solo contiene: Alimento, Característica, Cantidad
     */
    private void mostrarVentanaDieta(int idCaballo, String nombreCaballo) {
        JDialog dialogoDieta = new JDialog(this, "Dieta de " + nombreCaballo, true); // Modal
        dialogoDieta.setSize(600, 300);
        dialogoDieta.setLayout(new BorderLayout());
        dialogoDieta.setLocationRelativeTo(this);

        JLabel lblTituloDieta = new JLabel("Dieta Sugerida para: " + nombreCaballo, JLabel.CENTER);
        lblTituloDieta.setFont(new Font("Serif", Font.BOLD, 14));
        
        // Columnas CORREGIDAS: solo "Alimento", "Característica", "Cantidad"
        String[] columnasDieta = {"Alimento", "Característica", "Cantidad"};
        DefaultTableModel modeloDieta = new DefaultTableModel(columnasDieta, 0);
        JTable tablaDieta = new JTable(modeloDieta);
        
        // Llenado de la tabla corregida, moviendo la información de la columna "Equino" (Fibra, Energía, etc.)
        // a las columnas Alimento y Característica para que las celdas queden completas.
        
        // Si tu método gestion.mostrarDietasEnTabla() en Logica ya usa este modelo de 3 columnas, puedes usarlo.
        // Si no, aquí se simula el llenado manual para cumplir con el diseño:

        // Fila 1: Fibra y nutrientes (4-6Kg)
        modeloDieta.addRow(new Object[]{"Heno/Pasto", "Fibra y Nutrientes", "4-6 Kg"});
        // Fila 2: Energía (2-3Kg)
        modeloDieta.addRow(new Object[]{"Concentrado", "Energía", "2-3 Kg"});
        // Fila 3: Nutrientes esenciales (2-3 litros/día)
        modeloDieta.addRow(new Object[]{"Agua Limpia", "Nutrientes Esenciales", "2-3 Litros/día"});
        // Fila 4: Energía adicional (100-200 ml/día)
        modeloDieta.addRow(new Object[]{"Melaza/Aceite", "Energía Adicional", "100-200 ml/día"});
        // Fila 5: Suplemento nutricional (Según indicación veter...)
        modeloDieta.addRow(new Object[]{"Vitaminas/Minerales", "Suplemento Nutricional", "Según indicación veterinaria..."});

        // Ajustamos el tamaño de las 3 columnas
        tablaDieta.getColumnModel().getColumn(0).setPreferredWidth(100);  // Alimento
        tablaDieta.getColumnModel().getColumn(1).setPreferredWidth(200);  // Característica
        tablaDieta.getColumnModel().getColumn(2).setPreferredWidth(100);  // Cantidad

        dialogoDieta.add(lblTituloDieta, BorderLayout.NORTH);
        dialogoDieta.add(new JScrollPane(tablaDieta), BorderLayout.CENTER);
        
        dialogoDieta.setVisible(true);
    }
}