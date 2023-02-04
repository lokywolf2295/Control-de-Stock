package com.alura.tests;

import com.alura.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * probamos las conexiones multiples para ver como funciona el pool de conexiones
 * con la limitación de solo 10 conexiones
 */
public class PruebaPoolDeConexiones {

    public static void main(String[] args) throws SQLException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        for (int i = 0; i < 20; i ++){
            Connection conexion = connectionFactory.recuperaConexion(); //iniciamos una conexion por cada vuelta del ciclo

            System.out.println("Abriendo la conexion número: "+ (i+1)); //mostramos la cantidad de conexiones
        }
    }
}
