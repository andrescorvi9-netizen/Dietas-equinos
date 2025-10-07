/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Apharius
 */
public class Gestion_cabalo {
    
     private final Connection conexion;

    public Gestion_cabalo(Connection conexion) {
        this.conexion = conexion;
    }
//////////////////Btn gurardar////////////////
     public void guardarCaballoYDietas(String nombreDueño, String cedulaDueño, String nombreCaballo, String codigoCaballo, String edad, String afeccion) {
try {
    if (conexion == null) {
        JOptionPane.showMessageDialog(null, "Error: No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int idDueño = obtenerOInsertarDueño(nombreDueño, cedulaDueño);
    int idCaballo = insertarCaballo(nombreCaballo, codigoCaballo, edad, afeccion, idDueño);
    insertarDietaSegunCondiciones(idCaballo, edad, afeccion);

    JOptionPane.showMessageDialog(null, "Datos guardados correctamente");

    } catch (SQLException ex) {
    JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + ex.getMessage());
    }
}
////////////////////////////////////////////////////////////////
private int obtenerOInsertarDueño(String nombre, String cedula) throws SQLException {
String query = "SELECT ID_Dueño FROM Dueños WHERE Cedula_Dueño = ?";
PreparedStatement ps = conexion.prepareStatement(query);
ps.setString(1, cedula);
ResultSet rs = ps.executeQuery();
if (rs.next()) return rs.getInt("ID_Dueño");

String insert = "INSERT INTO Dueños (Nombre_Dueño, Cedula_Dueño) VALUES (?, ?)";
PreparedStatement insertPs = conexion.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
insertPs.setString(1, nombre);
insertPs.setString(2, cedula);
insertPs.executeUpdate();
ResultSet keys = insertPs.getGeneratedKeys();
keys.next();
return keys.getInt(1);
}
////////////////////////////////////////////////////////////////////////////////        

 private int insertarCaballo(String nombre, String codigo, String edad, String afeccion, int idDueño) throws SQLException {
String insert = "INSERT INTO Caballos (Nombre_Caballo, Codigo_Caballo, Edad, Afeccion, ID_Dueño) VALUES (?, ?, ?, ?, ?)";
PreparedStatement ps = conexion.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
ps.setString(1, nombre);
ps.setString(2, codigo);
ps.setString(3, edad);
ps.setString(4, afeccion);
ps.setInt(5, idDueño);
ps.executeUpdate();
ResultSet keys = ps.getGeneratedKeys();
keys.next();
return keys.getInt(1);
    }
 //////////////////////////////////////////////////////////////
  public void insertarDietaSegunCondiciones(int idCaballo, String edad, String afeccion) throws SQLException {
        PreparedStatement ps = conexion.prepareStatement("INSERT INTO Dietas (ID_Caballo, ID_Alimento, Cantidad) VALUES (?, ?, ?)");

        switch (edad.toLowerCase()) {
            case "potro joven: 1 - 2 años":
                switch (afeccion.toLowerCase()) {
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
            case "adolecente: 2-4 años":
            case "adolescente: 2-4 años":
                switch (afeccion.toLowerCase()) {
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
                switch (afeccion.toLowerCase()) {
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
  
  ///////////////////////////////////////////////////////////

  public void mostrarDietasEnTabla(JTable tabla, int idCaballo) {
try {
String sql = """
    SELECT d.Cedula_Dueño, d.Nombre_Dueño, c.ID_Caballo, c.Nombre_Caballo,
           a.Nombre_Alimento, a.Caracteristica, dt.Cantidad
    FROM Dietas dt
    JOIN Caballos c ON dt.ID_Caballo = c.ID_Caballo
    JOIN Dueños d ON c.ID_Dueño = d.ID_Dueño
    JOIN Alimentos a ON dt.ID_Alimento = a.ID_Alimento
    WHERE c.ID_Caballo = ?
""";
PreparedStatement ps = conexion.prepareStatement(sql);
ps.setInt(1, idCaballo);
ResultSet rs = ps.executeQuery();

DefaultTableModel model = (DefaultTableModel) tabla.getModel();
model.setRowCount(0);

while (rs.next()) {
    Object[] row = {
        rs.getString("Cedula_Dueño"),
        rs.getString("Nombre_Dueño"),
        rs.getInt("ID_Caballo"),
        rs.getString("Nombre_Caballo"),
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
  ///////////////////////////Registros//////////////////////////////////////////
 public String[] obtenerListaDueños() {
try {
    String sql = "SELECT Cedula_Dueño, Nombre_Dueño FROM Dueños";
    PreparedStatement ps = conexion.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    java.util.List<String> lista = new java.util.ArrayList<>();
    while (rs.next()) {
        lista.add(rs.getString("Cedula_Dueño") + " - " + rs.getString("Nombre_Dueño"));
    }
    return lista.toArray(new String[0]);
} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Error al obtener dueños: " + e.getMessage());
    return new String[0];
}
}
//////////////////////////////////////////////////////
public String[] obtenerListaCaballosPorCedula(String cedulaDueño) {
try {
    String sql = """
    SELECT c.ID_Caballo, c.Nombre_Caballo
    FROM Caballos c
    JOIN Dueños d ON c.ID_Dueño = d.ID_Dueño
    WHERE d.Cedula_Dueño = ?
    """;
    PreparedStatement ps = conexion.prepareStatement(sql);
    ps.setString(1, cedulaDueño);
    ResultSet rs = ps.executeQuery();

    java.util.List<String> lista = new java.util.ArrayList<>();
    while (rs.next()) {
        lista.add(rs.getInt("ID_Caballo") + " - " + rs.getString("Nombre_Caballo"));
    }
    return lista.toArray(new String[0]);
} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Error al obtener caballos: " + e.getMessage());
    return new String[0];
}
}
////////////////////////////////////////////////////////////////
 public ResultSet obtenerDatosCaballo(int idCaballo) {
try {
    String sql = """
        SELECT d.Nombre_Dueño, d.Cedula_Dueño, c.Nombre_Caballo, c.Codigo_Caballo, c.Edad, c.Afeccion
        FROM Caballos c
        JOIN Dueños d ON c.ID_Dueño = d.ID_Dueño
        WHERE c.ID_Caballo = ?
    """;
    PreparedStatement ps = conexion.prepareStatement(sql);
    ps.setInt(1, idCaballo);
    return ps.executeQuery();
} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Error al obtener datos del caballo: " + e.getMessage());
    return null;
}
}
 
 /////////////////////////////////////////////////////////////////
  public boolean eliminarCaballoPorId(int idCaballo) {

try {

    if (this.conexion == null) {
        JOptionPane.showMessageDialog(null, "Error: No se pudo conectar a la base de datos.", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    this.conexion.setAutoCommit(false); // Start transaction

    // 1
    String sqlDietas = "DELETE FROM Dietas WHERE ID_Caballo = ?";
    try (PreparedStatement psDietas = this.conexion.prepareStatement(sqlDietas)) {
        psDietas.setInt(1, idCaballo);
        psDietas.executeUpdate();
    }

    // 2
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
        if (this.conexion != null) {
            this.conexion.rollback(); 
        }
    } catch (SQLException se) {

        JOptionPane.showMessageDialog(null, "Error al hacer rollback: " + se.getMessage());
    }
    return false; 
} finally {
    try {
        if (this.conexion != null) {
            this.conexion.setAutoCommit(true); 
        }
    } catch (SQLException e) {
         JOptionPane.showMessageDialog(null, "Error al restaurar auto-commit: " + e.getMessage());
    }
}
}
}
