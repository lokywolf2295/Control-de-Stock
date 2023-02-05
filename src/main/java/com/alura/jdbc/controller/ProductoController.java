package com.alura.jdbc.controller;

import com.alura.factory.ConnectionFactory;
import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.List;

public class ProductoController {

    private ProductoDAO productoDAO; //creamos una variable de tipo ProductoDAO

    public ProductoController() {
        this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());//creamos un objeto de la clase ProductoDAO
        // pasandole como parametro el metodo recuperarConexion de la clase ConnectionFactory
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
     */
    public List<Producto> listar(){
        return productoDAO.listar();
    }

    /**
     * Metodo que permite guardar la informacion en la base de datos.
     * @param producto recibe por parametro un objeto de la clase Producto
     */
    public void guardar(Producto producto) {

        productoDAO.guardar(producto);
    }


}
