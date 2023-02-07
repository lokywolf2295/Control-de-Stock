package com.alura.jdbc.view;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.alura.jdbc.controller.CategoriaController;

public class ReporteFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTable tablaReporte;
    private DefaultTableModel modelo;

    private CategoriaController categoriaController;

    public ReporteFrame(ControlDeStockFrame controlDeStockFrame) {
        super("Reporte de produtos del stock");

        //inicializamos la categoría y producto controller
        this.categoriaController = new CategoriaController();

        Container container = getContentPane();
        setLayout(null);

        tablaReporte = new JTable();
        tablaReporte.setBounds(0, 0, 600, 400);
        container.add(tablaReporte);

        modelo = (DefaultTableModel) tablaReporte.getModel();
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");
        modelo.addColumn("");

        cargaReporte();

        setSize(600, 400);
        setVisible(true);
        setLocationRelativeTo(controlDeStockFrame);
    }

    /**
     * metodo que permite mostrar la lista de categorias y sus productos
     */
    private void cargaReporte() {
        var contenido = categoriaController.cargaReporte();

        contenido.forEach(categoria -> {
            modelo.addRow(new Object[]{categoria});

            var productos = categoria.getProductos();

            //Recorre la lista de productos devolviendo una columna vacía
            // y luego muestra su nombre y cantidad
            productos.forEach(producto ->
                    modelo.addRow(new Object[]{
                    "",
                    producto.getNombre(),
                    producto.getCantidad()
            }));
        });
    }
}
