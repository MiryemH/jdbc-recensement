package fr.diginamic.recensement.core.dao;

import fr.diginamic.recensement.core.ConnexionManagerBDD;
import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Region;
import fr.diginamic.recensement.core.entites.Ville;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de l'interface DepartementDao
 * @author Miryem HRARTI
 */

public class DepartementDaoImpl implements DepartementDao{

    /**
     * Récupère la liste des départements de la BDD
     * @return liste des départements extraits de la BDD
     */
    @Override
    public List<Departement> extraire() {
        List<Departement> departements = new ArrayList<>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            String sql = "SELECT d.code_dept, d.code_region, d.population, r.code_region, r.nom_region, r.population FROM Departements d\n" +
                    "JOIN Regions r ON d.code_region = r.code_region  ;";
            preparedStatement = connexion.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Departement departement = new Departement();
                departement.setCode(rs.getString("code_dept"));
                Region region = new Region();
                region.setCode(rs.getInt("code_region"));
                region.setNom(rs.getString("nom_region"));
                region.setPopulation(rs.getInt("population"));
                departement.setRegion(region);
                departement.setPopulation(rs.getInt("population"));
                departements.add(departement);
            }
        } catch (SQLException sqlException) {
            System.err.println("Problème d'accès à la BDD " + sqlException.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connexion != null)
                    connexion.close();
            } catch (SQLException sqlException) {
                System.err.println("Problème de fermeture des ressources " + sqlException.getMessage());
            }
        }
        return departements;
    }

    /**
     * insère un nouveau département dans la BDD
     * @param departement à insérer
     */
    @Override
    public void insert(Departement departement) {
       Connection connexion = null;
       PreparedStatement preparedStatement = null;
       try {
           connexion = ConnexionManagerBDD.getInstance();
           preparedStatement = connexion.prepareStatement("INSERT INTO Departements(code_dept,population, code_region) VALUES(?,?,?);");
           preparedStatement.setString(1, departement.getCode());
           preparedStatement.setInt(2, departement.getPopulation());
           preparedStatement.setInt(3, departement.getRegion().getCode());
           int nb = preparedStatement.executeUpdate();
           if(nb !=0 ) {
               System.out.println(departement.getCode()+" inséré dans la BDD");
           }
       } catch (SQLException sqlException) {
           System.err.println("Problème d'accès à la BDD " + sqlException.getMessage());
       } finally {
           try {
               if (preparedStatement != null)
                   preparedStatement.close();
               if (connexion != null)
                   connexion.close();
           } catch (SQLException sqlException) {
               System.err.println("Problème de fermeture des ressources " + sqlException.getMessage());
           }
       }
    }

    /**
     * met à jour la population du département identifié par codeDept
     * @param codeDept le code du département à modifier
     * @param population la nouvelle population du département
     * @return un entier 1 si mise à jour réussie, 0 sinon
     */
    @Override
    public int update(String codeDept, int population) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        int nb = 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("UPDATE Departements SET population = ? WHERE code_dept=?;");
            preparedStatement.setInt(1,population);
            preparedStatement.setString(2,codeDept);
            nb = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.err.println("Problème d'accès à la BDD "+ sqlException.getMessage());
        }
        finally {
            try {
                if(preparedStatement != null)
                    preparedStatement.close();
                if(connexion != null)
                    connexion.close();
            } catch (SQLException sqlException) {
                System.err.println("Problème de fermeture des ressources "+sqlException.getMessage());
            }
        }
        return nb;
    }

    /**
     * supprime le département passé en paramètres
     * @param departement à supprimer
     * @return true si la suppression est effectuée avec succès, false sinon
     */
    @Override
    public boolean delete(Departement departement) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        int nbR = 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("DELETE FROM Departements WHERE code_dept=?;");
            preparedStatement.setString(1, departement.getCode());
            nbR = preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.err.println("Problème d'accès à la BDD "+ sqlException.getMessage());
        }
        finally {
            try {
                if(preparedStatement != null)
                    preparedStatement.close();
                if(connexion != null)
                    connexion.close();
            } catch (SQLException sqlException) {
                System.err.println("Problème de fermeture des ressources "+sqlException.getMessage());
            }
        }
        return nbR == 0 ? false : true;
    }
    /**
     * recherche un département par un code
     * @param code permettant de chercher un departement
     * @return un département s'il est trouvé, null sinon
     */
    @Override
    public Departement getById(String code) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Departement departement = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("SELECT * FROM Departements WHERE code_dept=?");
            preparedStatement.setString(1,code);
            rs = preparedStatement.executeQuery();
            if(rs.next()){
                departement = new Departement();
                departement.setCode(rs.getString("code_dept"));
                Region region = new RegionDaoImpl().getById(rs.getInt("code_region"));
                departement.setRegion(region);
                departement.setPopulation(rs.getInt("population"));
            }
        } catch (SQLException sqlException) {
            System.err.println("Problème d'accès à la BDD " + sqlException.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connexion != null)
                    connexion.close();
            } catch (SQLException sqlException) {
                System.err.println("Problème de fermeture des ressources " + sqlException.getMessage());
            }
        }
        return departement;
    }

    /**
     * récupérer les villes d'un départements
     * @param code du département
     * @return la liste des villes du département
     */
    public List<Ville> getVillesBy(String code){
        List<Ville> villes = new ArrayList<>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            String sql = "SELECT * FROM Villes WHERE code_dept= ? ;";
            preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setString(1,code);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Ville ville = new Ville();
                ville.setId(rs.getInt("id"));
                ville.setCode(rs.getInt("code_ville"));
                ville.setNom(rs.getString("nom"));
                ville.setPopulation(rs.getInt("population"));
                Region region = new RegionDaoImpl().getById(rs.getInt("code_region"));
                Departement departement = new DepartementDaoImpl().getById(rs.getString("code_dept"));
                ville.setRegion(region);
                ville.setDepartement(departement);
                villes.add(ville);
            }
        } catch (SQLException sqlException) {
            System.err.println("Problème d'accès à la BDD " + sqlException.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connexion != null)
                    connexion.close();
            } catch (SQLException sqlException) {
                System.err.println("Problème de fermeture des ressources " + sqlException.getMessage());
            }
        }
        return villes;
    }
}
