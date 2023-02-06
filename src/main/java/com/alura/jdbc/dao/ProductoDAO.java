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

    /**
     * Metodo que permite guardar la informacion en la base de datos.
     *
     * @param producto recibe por parametro un objeto de la clase Producto
     */
    public void guardar(Producto producto) {
        try (con) { //cerramos la conexión de manera automática
        /*ATENCIÓN IMPORTANTE
            usamos el PreparedStatement para:
            Revisar la información ingresada por los inputs
            evitar que en esa información se encuentren secuencias de SQL
            por lo tanto evitamos SQL INJECTION
         */
            final PreparedStatement statement = con.prepareStatement("INSERT INTO PRODUCTO " +
                            "(nombre, descripcion, cantidad, categoria_id)" //valores que queremos agregar
                            + " VALUES (?,?,?,?)",
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
     * @param producto  contiene el modelo de nombre, descripción, cantidad y categoriaId
     * @param statement prepara la ejecución de la query
     * @throws SQLException para evitar errores
     */
    private void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());
        statement.setInt(4, producto.getCategoriaId());//agregamos la categoria


        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();//obtenemos todas las keys (id) generadas

        try (resultSet) { //con el final y el try ejecutamos el autoclousable de la clase resulset que cierra las conexiones de manera automática
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto %s", producto));
            }
        }
    }

    /**
     * Metodo que permite mostrar la informacion de la base de datos.
     *
     * @return resultado devuelve la fila del producto agregado
     */
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

    /**
     * Metodo que permite eliminar los datos de la base de datos
     *
     * @param id identificador
     * @return updateCount devuelve la actualización
     */
    public int eliminar(Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement(
                    "DELETE FROM PRODUCTO WHERE IDPRODUCTO = ?");//script para eliminar un dato por su id

            try (statement) {
                statement.setInt(1, id);

                statement.execute();

                int updateCount = statement.getUpdateCount();//metodo que retorna el estado de actualización del statement

                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que permite actualizar los datos de la DB con los modificados en
     * la tabla.
     *
     * @param nombre      del producto
     * @param descripcion del producto
     * @param cantidad    de stock
     * @param id          identificador
     * @return updateCount devuelve la actualización
     */
    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {

        try { //cerramos automaticamente la conexion cuando finalice la operación
            final PreparedStatement statement = con.prepareStatement(
                    "UPDATE PRODUCTO SET "
                            + " NOMBRE = ?"
                            + ", DESCRIPCION = ?"
                            + ", CANTIDAD = ?"
                            + " WHERE IDPRODUCTO = ?");//actualizamos todos los datos de la tabla

            try (statement) { //cerramos el statement cuando finalice la instrucción.
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, cantidad);
                statement.setInt(4, id);

                statement.execute();

                int updateCount = statement.getUpdateCount();

                return updateCount;//metodo que retorna el estado de actualización del statement
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
