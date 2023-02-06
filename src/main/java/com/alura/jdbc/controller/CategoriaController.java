package com.alura.jdbc.controller;

import com.alura.factory.ConnectionFactory;
import com.alura.jdbc.dao.CategoriaDAO;
import com.alura.jdbc.modelo.Categoria;

import java.util.List;

public class CategoriaController {
    private CategoriaDAO categoriaDAO;

    /**
     * Metodo que permite crear la conexión con la base de datos
     */
    public  CategoriaController(){
        var factory = new ConnectionFactory();
        this.categoriaDAO = new CategoriaDAO(factory.recuperaConexion());
    }

    /**
     * llama al metodo listar de la clase CategoriaDAO
     * @return categoriaDAO
     */
	public List<Categoria> listar() {
		return categoriaDAO.listar();
	}

    /**
     * Metodo que permite cargar el reporte de los productos ordenados por su categoría
     * @return la información obtenida del metodo listarConProductos
     */
    public List<Categoria> cargaReporte() {
        return this.categoriaDAO.listarConProductos();
    }

}
