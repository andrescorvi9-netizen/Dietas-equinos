/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author carvi
 */
public class ConexionBD {


    public static Connection getConexion() {
     
    String connectionUrl = "jdbc:sqlserver://localhost:1433;"
    + "databaseName=DIETAS_CABALLOS;"
    + "user=sa;"
    + "password=12345678;"
    + "loginTimeout=30;"
    + "TrustServerCertificate=True;";
    
    
    try{
    Connection con = DriverManager.getConnection(connectionUrl);
    return con;
    }catch(SQLException ex){
    return null;}
            
    
    }
}

