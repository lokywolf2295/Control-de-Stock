package com.alura.jdbc.controller;

import com.alura.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

    public void modificar(String nombre, String descripcion, Integer id) {
        // TODO
    }

    /**
     * Metodo que permite eliminar los datos de la base de datos
     * @param id
     * @return
     * @throws SQLException 
     */
    public int eliminar(Integer id) throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        Statement statement = con.createStatement();
        
        statement.execute("DELETE FROM PRODUCTO WHERE ID = " + id);
        
        int updateCount = statement.getUpdateCount();
        
        con.close();
        
        return updateCount;
    }

    /**
     * Metodo que permite mostrar la informacion de la base de datos.
     * @return
     * @throws SQLException 
     */
    public List<Map<String, String>> listar() throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        Statement statement = con.createStatement(); //para poder ejecutar comandos de sql creamos un Statement
        statement.execute("SELECT IDPRODUCTO, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO"); //enviamos las columnas que deseamos visualizar

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
     * @param producto
     * @throws SQLException 
     */
    public void guardar(Map<String, String> producto) throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        Connection con = factory.recuperaConexion();

        Statement statement = con.createStatement();
        statement.execute(//para poder ejecutar comandos de sql creamos un Statement
                "INSERT INTO PRODUCTO (nombre, descripcion, cantidad)" //valores que queremos agregar
                        + " VALUES ('" + producto.get("NOMBRE") + "', '"
                        + producto.get("DESCRIPCION") + "', '" + producto.get("CANTIDAD") + "')",
                        Statement.RETURN_GENERATED_KEYS); //podemos tomar el id generado al insertar en la lista de la DB
        
        ResultSet resultSet = statement.getGeneratedKeys();//obtenemos todas las keys (id) generadas
        
        while(resultSet.next()) {//recorremos la lista de id
            System.out.println(String.format(
                    "Fue insertado el producto de ID: %d",
                    resultSet.getInt(1)));
        }
    }

}
