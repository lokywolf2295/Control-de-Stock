
package com.alura.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) throws SQLException {
        
        /*Creamos la conexion mediante el DriverConection*/
        Connection con = DriverManager.getConnection(/*Abrimos la Conexion*/
                "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
                "root",
                "");

        System.out.println("Cerrando la conexión");

        /*Al finalizar se debe cerrar la conexion Obligatoriamente*/
        con.close();
    }
}
