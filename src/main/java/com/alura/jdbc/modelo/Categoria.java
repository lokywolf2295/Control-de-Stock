package com.alura.jdbc.modelo;

public class Categoria {
    private  Integer id;
    private  String nombre;

    /**
     * Constructor que recibe dos parametros
     * @param id de la categoría
     * @param nombre de la categoría
     */
    public Categoria(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * sobreescribimos el metodo
     * @return para que retorne solo el nombre
     */
    @Override
    public String toString() {
        return this.nombre;
    }
}
