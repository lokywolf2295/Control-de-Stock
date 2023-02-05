package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

    final private Connection con;

    public ProductoDAO(Connection con) { //como atributo tiene la conexion
        this.con = con;
    }

    public void guardar(Producto producto) throws SQLException {
        try (con) { //cerramos la conexión de manera automática
            con.setAutoCommit(false); //configuramos para que la transacción no tenga el control de la transacción
            //si surge un error la transacción no se ejecuta directamente, antes ejecutaba una y las siguientes no

        /*ATENCIÓN IMPORTANTE
            usamos el PreparedStatement para:
            Revisar la información ingresada por los inputs
            evitar que en esa información se encuentren secuencias de SQL
            por lo tanto evitamos SQL INJECTION
         */
            final PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO (nombre, descripcion, cantidad)" //valores que queremos agregar
                    + " VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);//podemos tomar el id generado al insertar en la lista de la DB

            try (statement) {//para tener un mejor control de la transacción cerramos el statement de manera automática
                ejecutaRegistro(producto, statement); //mientras sea menor se registra con normalidad sinó se divide en otra ejecución.

                con.commit(); //de esta manera nosotros decidimos cuando insertaremos la información
            }
        } catch (Exception e) {
            con.rollback(); //si ocurre un error entonces la transacción se retrae al inicio.
        }
    }

    /**
     * metodo que ejecutala instrucción SQL
     *
     * @param producto  contiene el modelo de nombre, descripción y cantidad
     * @param statement prepara la ejecución de la query
     * @throws SQLException para evitar errores
     */
    private void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());

        /*
        if (cantidad < 50) { //probando un error para luego manejarlo
            throw new RuntimeException("Ocurrió un error");
        }
        */

        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();//obtenemos todas las keys (id) generadas

        try (resultSet) { //con el final y el try ejecutamos el autoclousable de la clase resulset que cierra las conexiones de manera automática
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto %s", producto));
            }
        }
    }

}
