package com.alura.jdbc.dao;

import com.alura.jdbc.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection con;

    public CategoriaDAO(Connection con) {
        this.con = con;
    }

    /**
     * Permite obtener de la base de datos la lista de categorias
     *
     * @return resultado que es la lista de las categorias
     */
    public List<Categoria> listar() {
        List<Categoria> resultado = new ArrayList<>();

        try {
            final PreparedStatement statement = con.prepareStatement(
                    "SELECT ID, NOMBRE FROM CATEGORIA");//script para mostrar los nombres de las categorias

            try (statement) {
                final ResultSet resultSet = statement.executeQuery();

                try (resultSet) {
                    while (resultSet.next()) { //ciclo que agrega a la lista de categorias su nombre segun su id
                        var categoria = new Categoria(resultSet.getInt("ID"),
                                resultSet.getString("NOMBRE"));

                        resultado.add(categoria);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }

    public List<Categoria> listarConProductos() {
        List<Categoria> resultado = new ArrayList<>();

        try {
            var querySelect = "SELECT C.ID, C.NOMBRE, P.ID, P.NOMBRE, P.CANTIDAD "
                    + "FROM CATEGORIA C "
                    + "INNER JOIN PRODUCTO P ON C.ID = P.CATEGORIA_ID";
            final PreparedStatement statement = con.prepareStatement(querySelect);
            //script para mostrar los nombres de las categorias junto con sus productos

            try (statement) {
                final ResultSet resultSet = statement.executeQuery();

                try (resultSet) {
                    while (resultSet.next()) { //ciclo que agrega a la lista de categorias su nombre segun su id
                        //Extraigo los id y nombre en variable para usar el stream
                        Integer categoriaId = resultSet.getInt("ID");
                        String categoriaNombre = resultSet.getString("NOMBRE");

                        var categoria = resultado
                                .stream() //transformo en stream la lista
                                .filter(cat -> cat.getId().equals(categoriaId)) //nusco si en la lista hay una categoría con el id
                                .findAny().orElseGet(() -> { //si existe agregamos el resultado a la variable categoria

                                    //si no existe
                                    Categoria cat = new Categoria(categoriaId,
                                            categoriaNombre); //creamos el objeto de la categoría
                                    resultado.add(cat); //agregamos a la categoría
                                    return cat; //y la retornamos
                                });
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultado;
    }
}
