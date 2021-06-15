package fr.diginamic.recensement.core;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Cette classe permet de créer un pool de connexion avec c3p0,
 * les paramètres de configuration se trouvent dans ressources/c3p0-config.xml
 * @author  Miryem HRARTI
 */


public class ConnexionManagerBDD {
    /**
     * attribut de classe: pool de connexion
     */
    private static ComboPooledDataSource connPool;

    /**
     * Cette méthode initialise le pool de connexion, et lève une exception de type SQLException
     * dans le cas ou on arrive pas à avoir une connexion
     * @return une connection
     * @throws SQLException
     */
    public static  Connection getInstance() throws SQLException{
        if (connPool == null) {
           connPool= new ComboPooledDataSource();
        }
        return connPool.getConnection();
    }
}



