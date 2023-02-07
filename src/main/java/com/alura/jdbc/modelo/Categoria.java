package com.alura.jdbc.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que almacena los metodos y atributos de las categorías
 */
public class Categoria {
    private Integer id;
    private String nombre;

    private List<Producto> productos;

    /**
     * Constructor que recibe dos parametros
     *
     * @param id     de la categoría
     * @param nombre de la categoría
     */
    public Categoria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    /**
     * metodo que permite relacionar los productos con sus categorias
     *
     * @param producto que es recibido para la relación
     */
    public void agregar(Producto producto) {
        if (this.productos == null) {
            this.productos = new ArrayList<>();
        }
        this.productos.add(producto);
    }

    /**
     * Metodo que devuelve cada producto
     *
     * @return productos
     */
    public List<Producto> getProductos() {
        return this.productos;
    }

    /**
     * sobreescribimos el metodo
     *
     * @return para que retorne solo el nombre
     */
    @Override
    public String toString() {
        return this.nombre;
    }


}
