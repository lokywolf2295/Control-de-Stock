package com.alura.jdbc.controller;

import com.alura.factory.ConnectionFactory;
import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

import java.util.List;

public class ProductoController {

    private ProductoDAO productoDAO; //creamos una variable de tipo ProductoDAO

    public ProductoController() {
        this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperaConexion());//creamos un objeto de la clase ProductoDAO
        // pasandole como parametro el metodo recuperarConexion de la clase ConnectionFactory
    }


    /**
     * Llama al metodo modificar de la clase productoDAO.
     *
     * @param nombre      del producto
     * @param descripcion del producto
     * @param cantidad    de stock
     * @param id          identificador
     * @return updateCount devuelve la actualización
     */
    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {
        return productoDAO.modificar(nombre, descripcion, cantidad, id);
    }

    /**
     * Llama al metodo eliminar de la clase productoDAO
     *
     * @param id recibe como parametro
     * @return el producto eliminado
     */
    public int eliminar(Integer id) {
        return productoDAO.eliminar(id);
    }

    /**
     * Metodo que llama al metodo listar y permite mostrar todos los productos.
     *
     * @return resultado devuelve la lista de los productos
     */
    public List<Producto> listar() {
        return productoDAO.listar();
    }

    /**
     * Metodo Sobrecargado que llama al metodo listar y permite mostrar todos los productos.
     * @param categoria recibiendo como parametro
     * @return productoDAO
     */
    public List<Producto> listar(Categoria categoria) {
        return productoDAO.listar(categoria.getId());
    }

    /**
     * Metodo que permite llamar al metodo guardar de la clase productoDAO.
     *
     * @param producto recibe por parametro un objeto de la clase Producto
     * @param categoriaId recibe el id para relacionarlo con la categoría
     */
    public void guardar(Producto producto, Integer categoriaId) {
        producto.setCategoriaId(categoriaId);
        productoDAO.guardar(producto);
    }


}
