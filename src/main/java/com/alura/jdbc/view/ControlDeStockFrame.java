package com.alura.jdbc.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.alura.jdbc.controller.CategoriaController;
import com.alura.jdbc.controller.ProductoController;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

import java.util.Optional;

public class ControlDeStockFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel labelNombre, labelDescripcion, labelCantidad, labelCategoria;
    private JTextField textoNombre, textoDescripcion, textoCantidad;
    private JComboBox<Categoria> comboCategoria;
    private JButton botonGuardar, botonModificar, botonLimpiar, botonEliminar, botonReporte;
    private JTable tabla;
    private DefaultTableModel modelo;
    private ProductoController productoController;
    private CategoriaController categoriaController;

    public ControlDeStockFrame() {
        super("Productos");

        this.categoriaController = new CategoriaController();
        this.productoController = new ProductoController();

        Container container = getContentPane();
        setLayout(null);

        configurarCamposDelFormulario(container);

        configurarTablaDeContenido(container);

        configurarAccionesDelFormulario();
    }

    /**
     * Metodo que permite visualizar todo el contenido de lo almacenado en la DB
     *
     * @param container recibe el contenedor como parametro
     */
    private void configurarTablaDeContenido(Container container) {
        tabla = new JTable();

        modelo = (DefaultTableModel) tabla.getModel();
        modelo.addColumn("Identificador del Producto");
        modelo.addColumn("Nombre del Producto");
        modelo.addColumn("Descripci??n del Producto");
        modelo.addColumn("Cantidad");

        cargarTabla();

        tabla.setBounds(10, 205, 760, 280);

        botonEliminar = new JButton("Eliminar");
        botonModificar = new JButton("Modificar");
        botonReporte = new JButton("Ver Reporte");
        botonEliminar.setBounds(10, 500, 80, 20);
        botonModificar.setBounds(110, 500, 90, 20);
        botonReporte.setBounds(220, 500, 110, 20);

        container.add(tabla);
        container.add(botonEliminar);
        container.add(botonModificar);
        container.add(botonReporte);

        setSize(800, 600);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    /**
     * metodo que permite la visualizaci??n de los campos del formulario y sus
     * botones
     *
     * @param container recibe el contenedor como parametro
     */
    private void configurarCamposDelFormulario(Container container) {
        labelNombre = new JLabel("Nombre del Producto");
        labelDescripcion = new JLabel("Descripci??n del Producto");
        labelCantidad = new JLabel("Cantidad");
        labelCategoria = new JLabel("Categor??a del Producto");

        labelNombre.setBounds(10, 10, 240, 15);
        labelDescripcion.setBounds(10, 50, 240, 15);
        labelCantidad.setBounds(10, 90, 240, 15);
        labelCategoria.setBounds(10, 130, 240, 15);

        labelNombre.setForeground(Color.BLACK);
        labelDescripcion.setForeground(Color.BLACK);
        labelCategoria.setForeground(Color.BLACK);

        textoNombre = new JTextField();
        textoDescripcion = new JTextField();
        textoCantidad = new JTextField();
        comboCategoria = new JComboBox<>();
        comboCategoria.addItem(new Categoria(0,"Elige una Categor??a"));

        var categorias = this.categoriaController.listar();//llamamos al metodo listar
        categorias.forEach(categoria -> comboCategoria.addItem(categoria)); //Recorre la lista y va agregando la categor??a

        textoNombre.setBounds(10, 25, 265, 20);
        textoDescripcion.setBounds(10, 65, 265, 20);
        textoCantidad.setBounds(10, 105, 265, 20);
        comboCategoria.setBounds(10, 145, 265, 20);

        botonGuardar = new JButton("Guardar");
        botonLimpiar = new JButton("Limpiar");
        botonGuardar.setBounds(10, 175, 80, 20);
        botonLimpiar.setBounds(110, 175, 80, 20);

        container.add(labelNombre);
        container.add(labelDescripcion);
        container.add(labelCantidad);
        container.add(labelCategoria);
        container.add(textoNombre);
        container.add(textoDescripcion);
        container.add(textoCantidad);
        container.add(comboCategoria);
        container.add(botonGuardar);
        container.add(botonLimpiar);
    }

    /**
     * Al precionar el boton guardar llamamos a los metodos ...
     */
    private void configurarAccionesDelFormulario() {
        botonGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //lista de metodos a llamar
                guardar();
                limpiarTabla();
                cargarTabla();
            }
        });

        botonLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        botonEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminar();
                limpiarTabla();
                cargarTabla();
            }
        });

        botonModificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modificar();
                limpiarTabla();
                cargarTabla();
            }
        });

        botonReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirReporte();
            }
        });
    }

    private void abrirReporte() {
        new ReporteFrame(this);
    }

    private void limpiarTabla() {
        modelo.getDataVector().clear();
    }

    private boolean tieneFilaElegida() {
        return tabla.getSelectedRowCount() == 0 || tabla.getSelectedColumnCount() == 0;
    }

    /**
     * Mediante este metodo logramos seleccionar cada campo de la lista
     * modificar los datos, enviar esa informaci??n a la base de datos y
     * actualizarla
     */
    private void modificar() {
        if (tieneFilaElegida()) {
            JOptionPane.showMessageDialog(this, "Por favor, elije un item");
            return;
        }

        Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()))
                .ifPresentOrElse(fila -> {
                    Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString()); //modificamos para que el in se conviierta en String
                    String nombre = (String) modelo.getValueAt(tabla.getSelectedRow(), 1);
                    String descripcion = (String) modelo.getValueAt(tabla.getSelectedRow(), 2);
                    Integer cantidad = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 3).toString()); //agregamos el que obtiene la informaci??n de la cantidad

                    var filasModificadas = this.productoController.modificar(nombre, descripcion, cantidad, id);

                    JOptionPane.showMessageDialog(this, String.format("%d item modificado con ??xito!", filasModificadas));
                }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
    }

    /**
     * metodo que permite eliminar los datos de la lista
     */
    private void eliminar() {
        if (tieneFilaElegida()) {//revisamos que una fila est?? seleccionada para ser modificada
            JOptionPane.showMessageDialog(this, "Por favor, elije un item");
            return;
        }

        Optional.ofNullable(modelo.getValueAt(tabla.getSelectedRow(), tabla.getSelectedColumn()))
                .ifPresentOrElse(fila -> {
                    Integer id = Integer.valueOf(modelo.getValueAt(tabla.getSelectedRow(), 0).toString());
                    var filasModificadas = this.productoController.eliminar(id);

                    modelo.removeRow(tabla.getSelectedRow());

                    JOptionPane.showMessageDialog(this, String.format("%d item eliminado con ??xito!", filasModificadas));
                }, () -> JOptionPane.showMessageDialog(this, "Por favor, elije un item"));
    }

    /**
     * Metodo que permite mostrar la tabla, con los datos obtenidos de la DB
     * dentro del panel de visualizaci??n
     */
    private void cargarTabla() {
        var productos = this.productoController.listar();

        productos.forEach(producto -> modelo.addRow(//recorremos la lista obteniendo cada campo
                new Object[]{
                        producto.getId(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getCantidad()}));
    }

    /**
     * metodo que permite guardar el nombre, descripci??n y cantidad en la base
     * de datos segun la informaci??n obteniada de los campos del formulario.
     * ademas corrobora que no se incerten datos erroneos o campos bac??os
     */
    private void guardar() {
        if (textoNombre.getText().isBlank() || textoDescripcion.getText().isBlank()) { //corrobora que ambos campos no est??n vac??os
            JOptionPane.showMessageDialog(this, "Los campos Nombre y Descripci??n son requeridos.");
            return;
        }

        Integer cantidadInt;

        try {
            cantidadInt = Integer.parseInt(textoCantidad.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, String
                    .format("El campo cantidad debe ser num??rico dentro del rango %d y %d.", 0, Integer.MAX_VALUE)); //corrobora que no se incerten Strings
            return;
        }

        var producto = new Producto(textoNombre.getText(),
                textoDescripcion.getText(),
                cantidadInt);//almacena la informaci??n en un hashmap

        var categoria = (Categoria) comboCategoria.getSelectedItem(); //casteo la categoria para que sea de tipo Categor??a

        this.productoController.guardar(producto, categoria.getId());

        JOptionPane.showMessageDialog(this, "Registrado con ??xito!");

        this.limpiarFormulario();
    }

    private void limpiarFormulario() {
        this.textoNombre.setText("");
        this.textoDescripcion.setText("");
        this.textoCantidad.setText("");
        this.comboCategoria.setSelectedIndex(0);
    }

}
