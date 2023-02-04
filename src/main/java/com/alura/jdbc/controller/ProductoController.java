package com.alura.jdbc.controller;

import com.alura.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

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
     * @param nombre      del producto
     * @param descripcion del producto
     * @param cantidad    de stock
     * @param id          identificador
     * @return updateCount devuelve la actualización
     * @throws SQLException para evitar errores
     */
    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
        //todos los metodos nececitan tener el throws por la coneccion, o en su defecto un try catch
        ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperaConexion();

        try (con) { //cerramos automaticamente la conexion cuando finalice la operación
            final PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTO SET " + " NOMBRE = ?" + ", DESCRIPCION = ?" + ", CANTIDAD = ?" + " WHERE IDPRODUCTO = ?");//actualizamos todos los datos de la tabla

            try (statement) { //cerramos el statement cuando finalice la instrucción.
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, cantidad);
                statement.setInt(4, id);

                statement.execute();

                int updateCount = statement.getUpdateCount();

                return updateCount;//metodo que retorna el estado de actualización del statement
            }
        }
    }

    /**
     * Metodo que permite eliminar los datos de la base de datos
     *
     * @param id identificador
     * @return updateCount devuelve la actualización
     * @throws SQLException para evitar errores
     */
    public int eliminar(Integer id) throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperaConexion();

        try (con) {
            final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE IDPRODUCTO = ?");//script para eliminar un dato por su id

            try (statement) {
                statement.setInt(1, id);

                statement.execute();

                int updateCount = statement.getUpdateCount();//metodo que retorna el estado de actualización del statement

                return updateCount;
            }
        }
    }

    /**
     * Metodo que permite mostrar la informacion de la base de datos.
     *
     * @return resultado devuelve la lista de los productos
     * @throws SQLException para evitar errores
     */
    public List<Map<String, String>> listar() throws SQLException {
        ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperaConexion();

        try (con) {
            final PreparedStatement statement = con.prepareStatement("SELECT IDPRODUCTO, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO"); //para poder ejecutar comandos de sql  y evitar el ingreso de SQL Injection creamos un PreparedStatement

            try (statement) {
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
                return resultado;
            }
        }
    }

    /**
     * Metodo que permite guardar la informacion en la base de datos.
     *
     * @throws SQLException para evitar errores
     */
    public void guardar(Producto producto) throws SQLException {

        ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperaConexion();
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

        /*if (cantidad < 50) { //probando un error para luego manejarlo
            throw new RuntimeException("Ocurrió un error");
        }*/

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
