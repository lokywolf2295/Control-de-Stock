
package com.alura.tests;

import com.alura.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) throws SQLException {
        
        /*Creamos la conexion mediante el DriverConection*/
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        System.out.println("Cerrando la conexión");

        /*Al finalizar se debe cerrar la conexion Obligatoriamente*/
        con.close();
    }
}
