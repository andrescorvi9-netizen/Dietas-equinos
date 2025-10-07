package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static Connection conexion = null;

    public static Connection getConexion() {
        String connectionUrl = "jdbc:sqlserver://databaseproyecto.database.windows.net:1433;"
                + "database=dietasparaequinos;"
                + "user=adminsql@databaseproyecto;"
                + "password=hola1234.;"
                + "encrypt=true;"
                + "trustServerCertificate=false;"
                + "hostNameInCertificate=*.database.windows.net;"
                + "loginTimeout=30;";

        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(connectionUrl);
                System.out.println("✅ Conexión abierta a Azure SQL Database");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al conectar: " + ex.getMessage());
            conexion = null;
        }
        return conexion;
    }

    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada.");
            }
        } catch (SQLException ex) {
            System.err.println("❌ Error al cerrar conexión: " + ex.getMessage());
        }
    }
}



