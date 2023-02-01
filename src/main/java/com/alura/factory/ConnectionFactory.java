package com.alura.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Por convención: la clase debe llevar este nombre y su paquete: factory en referencia al metodo de dideño factory.
 * @author matias
 */
public class ConnectionFactory {

    public Connection recuperaConexion() throws SQLException {
        return DriverManager.getConnection( /*Abrimos la Conexion y la devolvemos*/
                "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
                "root",
                "");
    }
}
