package com.alura.factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Por convención: la clase debe llevar este nombre y su paquete: factory en referencia al metodo de dideño factory.
 * @author matias
 */
public class ConnectionFactory {

    private final DataSource dataSource;

    /**
     * el pool de conexiones permite que se puedan realizar multiples conexiones al mismo tienmpo,
     * sin que estas se estorben entre si y por lo tanto,
     * no se encolen para hacer las peticiones en caso de que se realizan muchas en simultaneo
     */
    public ConnectionFactory() { //creamos el constructor para que la conectionFactory utilice el Pool de Conexiones
        var pooleadDataSource = new ComboPooledDataSource();
        pooleadDataSource.setJdbcUrl("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC");
        pooleadDataSource.setUser("root");
        pooleadDataSource.setPassword("");
        pooleadDataSource.setMaxPoolSize(10); //cambiamos la cantidad de conexiones multiples a 10 como maximo

        this.dataSource = pooleadDataSource;
    }

    public Connection recuperaConexion() throws SQLException {
        return this.dataSource.getConnection();
    }
}
