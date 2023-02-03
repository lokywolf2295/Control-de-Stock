package com.alura.jdbc.controller;

import com.alura.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

    /**
     * Metodo que permite actualizar los datos de la DB con los modificados en
     * la tabla.
     *
     * @param nombre
     * @param descripcion
     * @param cantidad
     * @param id
     * @return updateCount
     * @throws SQLException
     */
    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
        //todos los metodos nececitan tener el throws por la coneccion, o en su defecto un try catch
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTO SET "
                + " NOMBRE = ?"
                + ", DESCRIPCION = ?"
                + ", CANTIDAD = ?"
                + " WHERE IDPRODUCTO = ?");//actualizamos todos los datos de la tabla
        statement.setString(1,nombre);
        statement.setString(2,descripcion);
        statement.setInt(3,cantidad);
        statement.setInt(4,id);

        statement.execute();

        int updateCount = statement.getUpdateCount(); //metodo que retorna el estado de actualización del statement

        con.close();

        return updateCount;
    }

    /**
     * Metodo que permite eliminar los datos de la base de datos
     *
     * @param id
     * @return updateCount
     * @throws SQLException
     */
    public int eliminar(Integer id) throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE IDPRODUCTO = ?");//script para eliminar un dato por su id
        statement.setInt(1,id);

        statement.execute();

        int updateCount = statement.getUpdateCount();//metodo que retorna el estado de actualización del statement

        con.close();

        return updateCount;
    }

    /**
     * Metodo que permite mostrar la informacion de la base de datos.
     *
     * @return
     * @throws SQLException
     */
    public List<Map<String, String>> listar() throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        PreparedStatement statement = con.prepareStatement("SELECT IDPRODUCTO, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO"); //para poder ejecutar comandos de sql  y evitar el ingreso de SQL Injection creamos un PreparedStatement
        statement.execute(); //enviamos las columnas que deseamos visualizar

        ResultSet resultSet = statement.getResultSet(); //obtenemos la información que proviene de la Base de datos

        List<Map<String, String>> resultado = new ArrayList<>();//en esta lista almacenamos la informacipon obtenida

        while (resultSet.next()) { //mediante el bucle mapeamos la información y la vamos almacenando en cada fila
            Map<String, String> fila = new HashMap<>();
            fila.put("IDPRODUCTO", String.valueOf(resultSet.getInt("IDPRODUCTO")));
            fila.put("NOMBRE", resultSet.getString("NOMBRE"));
            fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
            fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

            resultado.add(fila);
        }

        con.close();//cerramos la conexion

        return resultado;
    }

    /**
     * Metodo que permite guardar la informacion en la base de datos.
     *
     * @param producto
     * @throws SQLException
     */
    public void guardar(Map<String, String> producto) throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        /*ATENCIÓN IMPORTANTE
            usamos el PreparedStatement para:
            Revisar la información ingresada por los inputs
            evitar que en esa información se encuentren secuencias de SQL
            por lo tanto evitamos SQL INJECTION
         */
        PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO (nombre, descripcion, cantidad)" //valores que queremos agregar
                        + " VALUES (?,?,?)",
        Statement.RETURN_GENERATED_KEYS);//podemos tomar el id generado al insertar en la lista de la DB
        statement.setString(1,producto.get("NOMBRE"));
        statement.setString(2,producto.get("DESCRIPCION"));
        statement.setInt(3,Integer.valueOf(producto.get("CANTIDAD")));

        statement.execute(); //ejecutamos el execute

        ResultSet resultSet = statement.getGeneratedKeys();//obtenemos todas las keys (id) generadas

        while (resultSet.next()) {//recorremos la lista de id
            System.out.println(String.format(
                    "Fue insertado el producto de ID: %d",
                    resultSet.getInt(1)));
        }
    }

}
