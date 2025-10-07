package Logica;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Clase de lógica de negocio para la gestión de caballos y dietas.
 * Usa nombres de columna sin tilde ('ID_Dueno', 'Afecto') para compatibilidad con JDBC/SQL Server.
 */
public class Gestion_cabalo {
    
    private final Connection conexion;

    public Gestion_cabalo(Connection conexion) {
        this.conexion = conexion;
    }

//////////////////Btn guardar Caballo y Dietas////////////////
    // Recibe el ID_Dueno (sin tilde) del Dueño autenticado
    public void guardarCaballoYDietas(int idDueno, String nombreCaballo, String codigoCaballo, String edad, String afeccion) {
        try {
            if (conexion == null || conexion.isClosed()) {
                JOptionPane.showMessageDialog(null, "Error: No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            conexion.setAutoCommit(false); 

            // 1. Insertar Caballo (usa 'Afecto' y 'ID_Dueno')
            int idCaballo = insertarCaballo(nombreCaballo, codigoCaballo, edad, afeccion, idDueno);
            
            // 2. Insertar Dieta
            insertarDietaSegunCondiciones(idCaballo, edad, afeccion);

            conexion.commit(); 
            JOptionPane.showMessageDialog(null, "✅ Caballo y dieta guardados correctamente.");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "❌ Error al guardar los datos: " + ex.getMessage());
            try {
                if (conexion != null) conexion.rollback(); 
            } catch (SQLException rollbackEx) {
                 JOptionPane.showMessageDialog(null, "Error al hacer rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                if (conexion != null) conexion.setAutoCommit(true); 
            } catch (SQLException autoCommitEx) {
                 JOptionPane.showMessageDialog(null, "Error al restaurar auto-commit: " + autoCommitEx.getMessage());
            }
        }
    }
////////////////////////////////////////////////////////////////

private int insertarCaballo(String nombre, String codigo, String edad, String afeccion, int idDueno) throws SQLException {
    // Columnas 'Afecto' e 'ID_Dueno' (sin tilde)
    String insert = "INSERT INTO Caballos (Nombre_Caballo, Codigo_Caballo, Edad, Afecto, ID_Dueno) VALUES (?, ?, ?, ?, ?)";
    
    // Usamos Statement.RETURN_GENERATED_KEYS para obtener el ID_Caballo autogenerado
    PreparedStatement ps = conexion.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
    
    ps.setString(1, nombre);
    ps.setString(2, codigo);
    ps.setString(3, edad);
    ps.setString(4, afeccion); // Se inserta el valor de la afección/afecto
    ps.setInt(5, idDueno);     // Se inserta el ID del Dueño
    
    ps.executeUpdate();
    
    ResultSet keys = ps.getGeneratedKeys();
    keys.next();
    return keys.getInt(1); // Devuelve el ID_Caballo recién creado
}

//////////////////////////////////////////////////////////////
public void insertarDietaSegunCondiciones(int idCaballo, String edad, String afeccion) throws SQLException {
    PreparedStatement ps = conexion.prepareStatement("INSERT INTO Dietas (ID_Caballo, ID_Alimento, Cantidad) VALUES (?, ?, ?)");

    String edadKey = edad.toLowerCase().trim();
    String afeccionKey = afeccion.toLowerCase().trim();

    switch (edadKey) {
        case "potro joven: 1 - 2 años":
            switch (afeccionKey) {
                case "salud optima":
                    insertarAlimento(ps, idCaballo, 1, "3-5Kg");
                    insertarAlimento(ps, idCaballo, 2, "15-20L");
                    insertarAlimento(ps, idCaballo, 3, "1-2Kg");
                    insertarAlimento(ps, idCaballo, 5, "2-3Kg");
                    insertarAlimento(ps, idCaballo, 4, "10-20g");
                    break;
                case "deshidratacion":
                    insertarAlimento(ps, idCaballo, 2, "30-40L");
                    insertarAlimento(ps, idCaballo, 1, "2-3Kg");
                    insertarAlimento(ps, idCaballo, 5, "1-2Kg");
                    break;
                case "desnutricion":
                    insertarAlimento(ps, idCaballo, 1, "4-6Kg");
                    insertarAlimento(ps, idCaballo, 3, "2-3Kg");
                    insertarAlimento(ps, idCaballo, 9, "2-3 litros/día");
                    insertarAlimento(ps, idCaballo, 10, "100-200 ml/día");
                    insertarAlimento(ps, idCaballo, 11, "Según indicación veterinaria");
                    break;
                case "parasitos":
                    insertarAlimento(ps, idCaballo, 1, "3Kg");
                    insertarAlimento(ps, idCaballo, 2, "20L");
                    insertarAlimento(ps, idCaballo, 11, "Desparasitación + vitaminas");
                    break;
            }
            break;
        case "adolescente: 2-4 años": 
            switch (afeccionKey) {
                case "salud optima":
                    insertarAlimento(ps, idCaballo, 1, "7-9Kg");
                    insertarAlimento(ps, idCaballo, 2, "25-30L");
                    insertarAlimento(ps, idCaballo, 3, "3-4Kg");
                    insertarAlimento(ps, idCaballo, 5, "4-6Kg");
                    insertarAlimento(ps, idCaballo, 4, "20-30g");
                    insertarAlimento(ps, idCaballo, 6, "1-2Kg");
                    break;
                case "deshidratacion":
                    insertarAlimento(ps, idCaballo, 2, "60-80L");
                    insertarAlimento(ps, idCaballo, 1, "4-5Kg");
                    insertarAlimento(ps, idCaballo, 5, "2-3Kg");
                    insertarAlimento(ps, idCaballo, 6, "1Kg");
                    break;
                case "desnutricion":
                    insertarAlimento(ps, idCaballo, 1, "8-10Kg");
                    insertarAlimento(ps, idCaballo, 3, "4-5Kg");
                    insertarAlimento(ps, idCaballo, 9, "3-4 litros/día");
                    insertarAlimento(ps, idCaballo, 10, "200-300 ml/día");
                    insertarAlimento(ps, idCaballo, 11, "Según indicación veterinaria");
                    break;
                case "parasitos":
                    insertarAlimento(ps, idCaballo, 1, "6Kg");
                    insertarAlimento(ps, idCaballo, 11, "Desparasitación + vitaminas");
                    break;
            }
            break;
        case "mayor: 4-15 años": 
            switch (afeccionKey) {
                case "salud optima":
                    insertarAlimento(ps, idCaballo, 1, "10-12Kg");
                    insertarAlimento(ps, idCaballo, 2, "30-40L");
                    insertarAlimento(ps, idCaballo, 3, "4-5Kg");
                    insertarAlimento(ps, idCaballo, 5, "6-8Kg");
                    insertarAlimento(ps, idCaballo, 4, "30-40g");
                    insertarAlimento(ps, idCaballo, 6, "2-3Kg");
                    break;
                case "deshidratacion":
                    insertarAlimento(ps, idCaballo, 2, "80-100L");
                    insertarAlimento(ps, idCaballo, 1, "5-6Kg");
                    insertarAlimento(ps, idCaballo, 5, "3-4Kg");
                    insertarAlimento(ps, idCaballo, 6, "2Kg");
                    break;
                case "desnutricion":
                    insertarAlimento(ps, idCaballo, 1, "12-14Kg");
                    insertarAlimento(ps, idCaballo, 3, "5-6Kg");
                    insertarAlimento(ps, idCaballo, 9, "4-5 litros/día");
                    insertarAlimento(ps, idCaballo, 10, "300-400 ml/día");
                    insertarAlimento(ps, idCaballo, 11, "Según indicación veterinaria");
                    break;
                case "parasitos":
                    insertarAlimento(ps, idCaballo, 1, "8Kg");
                    insertarAlimento(ps, idCaballo, 11, "Desparasitación + vitaminas");
                    break;
            }
            break;
    }
}

private void insertarAlimento(PreparedStatement ps, int idCaballo, int idAlimento, String cantidad) throws SQLException {
    ps.setInt(1, idCaballo);
    ps.setInt(2, idAlimento);
    ps.setString(3, cantidad);
    ps.executeUpdate();
}
    
/////////////////////////////////////////////////////////////////////////////////////////////
public int obtenerUltimoCaballoPorCodigo(String codigoCaballo) {
    try {
        // Busca el caballo más reciente con ese código (por si hay duplicados históricos)
        String sql = "SELECT ID_Caballo FROM Caballos WHERE Codigo_Caballo = ? ORDER BY ID_Caballo DESC";
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setString(1, codigoCaballo);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("ID_Caballo");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al obtener el ID del caballo: " + ex.getMessage());
    }
    return -1;
}
    
//-----------------------------------------------------------
// Método para mostrar la Dieta
//-----------------------------------------------------------
public void mostrarDietasEnTabla(JTable tabla, int idCaballo) {
    try {
        // ✅ CORRECCIÓN 1: Se modificó la consulta SQL para solo seleccionar 3 columnas
        String sql = "SELECT a.Nombre_Alimento, a.Caracteristica, dt.Cantidad " + 
                      "FROM Dietas dt " + 
                      "JOIN Caballos c ON dt.ID_Caballo = c.ID_Caballo " + 
                      "JOIN Alimentos a ON dt.ID_Alimento = a.ID_Alimento " + 
                      "WHERE c.ID_Caballo = ?";
        
        PreparedStatement ps = conexion.prepareStatement(sql);
        ps.setInt(1, idCaballo);
        ResultSet rs = ps.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
        model.setRowCount(0);

        while (rs.next()) {
            // ✅ CORRECCIÓN 2: Se modificó la inserción de datos para que coincida con las 3 columnas de la tabla (Alimento, Característica, Cantidad)
            Object[] row = {
                rs.getString("Nombre_Alimento"),
                rs.getString("Caracteristica"),
                rs.getString("Cantidad")
            };
        model.addRow(row);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al cargar la dieta: " + ex.getMessage()); 
    }
}
    
/////////////////////////////////////////////////////////////////
    public boolean eliminarCaballoPorId(int idCaballo) {
        try {
            this.conexion.setAutoCommit(false); 
            
            // 1. Eliminar Dietas asociadas
            String sqlDietas = "DELETE FROM Dietas WHERE ID_Caballo = ?";
            try (PreparedStatement psDietas = this.conexion.prepareStatement(sqlDietas)) {
                psDietas.setInt(1, idCaballo);
                psDietas.executeUpdate();
            }

            // 2. Eliminar Caballo
            String sqlCaballos = "DELETE FROM Caballos WHERE ID_Caballo = ?"; 
            boolean eliminado = false;
            try (PreparedStatement psCaballos = this.conexion.prepareStatement(sqlCaballos)) {
                psCaballos.setInt(1, idCaballo);
                eliminado = psCaballos.executeUpdate() > 0; 
            }

            this.conexion.commit(); 
            return eliminado;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el caballo: " + e.getMessage()); 
            try {
                if (this.conexion != null) this.conexion.rollback(); 
            } catch (SQLException se) {
                JOptionPane.showMessageDialog(null, "Error al hacer rollback: " + se.getMessage());
            }
            return false; 
        } finally {
            try {
                if (this.conexion != null) this.conexion.setAutoCommit(true); 
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al restaurar auto-commit: " + e.getMessage());
            }
        }
    }

    // --- Método para el Registro/Mis Caballos ---
    public void mostrarCaballosDelDuenoEnTabla(JTable tabla, int idDueno) {
        try {
            // CORREGIDO: Usa ID_Dueno y Afecto
            String sql = "SELECT ID_Caballo, Nombre_Caballo, Codigo_Caballo, Edad, Afecto FROM Caballos WHERE ID_Dueno = ?";
            
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idDueno);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("ID_Caballo"),
                    rs.getString("Nombre_Caballo"),
                    rs.getString("Codigo_Caballo"),
                    rs.getString("Edad"),
                    rs.getString("Afecto") 
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el registro de caballos: " + ex.getMessage()); 
        }
    }
}