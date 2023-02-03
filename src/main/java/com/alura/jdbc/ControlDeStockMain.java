package com.alura.jdbc;

import javax.swing.JFrame;

import com.alura.jdbc.view.ControlDeStockFrame;

public class ControlDeStockMain {

	/**
	 * Metodo de inicialización del proyecto
	 * llama al frame de ControlDeStock
	 * y permite que al presionar el botón se detenga el programa
	 * @param args
	 */
	public static void main(String[] args) {
		ControlDeStockFrame produtoCategoriaFrame = new ControlDeStockFrame();
		produtoCategoriaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
