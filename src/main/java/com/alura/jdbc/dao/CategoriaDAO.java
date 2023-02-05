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
}
