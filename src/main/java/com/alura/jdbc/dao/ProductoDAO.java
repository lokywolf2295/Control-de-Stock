package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alura.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

    final private Connection con;

    public ProductoDAO(Connection con) { //como atributo tiene la conexion
        this.con = con;
    }

    public void guardar(Producto producto) {
        try (con) { //cerramos la conexión de manera automática
        /*ATENCIÓN IMPORTANTE
            usamos el PreparedStatement para:
            Revisar la información ingresada por los inputs
            evitar que en esa información se encuentren secuencias de SQL
            por lo tanto evitamos SQL INJECTION
         */
            final PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO " +
                            "(nombre, descripcion, cantidad)" //valores que queremos agregar
                            + " VALUES (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);//podemos tomar el id generado al insertar en la lista de la DB

            try (statement) {//para tener un mejor control de la transacción cerramos el statement de manera automática
                ejecutaRegistro(producto, statement); //mientras sea menor se registra con normalidad sinó se divide en otra ejecución.
            }
        } catch (SQLException e) {
            throw new RuntimeException();
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

        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();//obtenemos todas las keys (id) generadas

        try (resultSet) { //con el final y el try ejecutamos el autoclousable de la clase resulset que cierra las conexiones de manera automática
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto %s", producto));
            }
        }
    }

    public List<Producto> listar() {
        List<Producto> resultado = new ArrayList<>();//en esta lista almacenamos la informacipon obtenida

        ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperaConexion();

        try (con) {
            final PreparedStatement statement = con
                    .prepareStatement("SELECT IDPRODUCTO, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");
            //para poder ejecutar comandos de sql  y evitar el ingreso de SQL Injection creamos un PreparedStatement

            try (statement) {
                statement.execute(); //enviamos las columnas que deseamos visualizar

                final ResultSet resultSet = statement.getResultSet(); //obtenemos la información que proviene de la Base de datos

                try (resultSet) {
                    while (resultSet.next()) { //mediante el bucle mapeamos la información y la vamos almacenando en cada fila
                        Producto fila = (new Producto(
                                resultSet.getInt("IDPRODUCTO"),
                                resultSet.getString("NOMBRE"),
                                resultSet.getString("DESCRIPCION"),
                                resultSet.getInt("CANTIDAD")));

                        resultado.add(fila);
                    }
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
