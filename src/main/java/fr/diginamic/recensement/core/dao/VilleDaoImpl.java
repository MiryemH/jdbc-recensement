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
public class VilleDaoImpl implements VilleDao{

    /**
     * Récupère la liste des villes de la BDD
     * @return liste des villes extraits de la BDD
     */
    @Override
    public List<Ville> extraire() {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Ville> villes = new ArrayList<>();

        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("SELECT * FROM Villes ;");
            rs = preparedStatement.executeQuery();
            while(rs.next()){
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
        }catch (SQLException sqlException) {
            System.err.println("Problème d'accès à la BDD "+ sqlException.getMessage());
        }
        finally {
            try {
                if(rs != null)
                    rs.close();
                if(preparedStatement != null)
                    preparedStatement.close();
                if(connexion != null)
                    connexion.close();
            } catch (SQLException sqlException) {
                System.err.println("Problème de fermeture des ressources "+sqlException.getMessage());
            }
        }
        return villes;
    }

    /**
     * insère une nouvelle ville dans la BDD
     * @param ville à insérer
     */
    @Override
    public void insert(Ville ville) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        int nb= 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("INSERT INTO Villes(code_ville, nom, population, code_dept, code_region) VALUES(?,?,?,?,?);",PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, ville.getCode());
            preparedStatement.setString(2, ville.getNom());
            preparedStatement.setInt(3, ville.getPopulation());
            preparedStatement.setString(4, ville.getDepartement().getCode());
            preparedStatement.setInt(5, ville.getRegion().getCode());
            nb = preparedStatement.executeUpdate();

            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()){
                nb=rs.getInt(1);
                ville.setId(nb);
                System.out.println(ville.getNom()+ " est insérée dans la BDD");
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
     * met à jour la population d'une ville donnée
     * @param ville  à modifier
     * @param nvPopulation la nouvelle population de la ville
     * @return un entier 1 si mise à jour réussie, 0 sinon
     */
    @Override
    public int update(Ville ville, int nvPopulation) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        int nb = 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("UPDATE Villes SET population = ? WHERE code_ville=? AND nom = ? AND code_dept = ? AND code_region = ?;");
            preparedStatement.setInt(1, nvPopulation);
            preparedStatement.setInt(2,ville.getCode());
            preparedStatement.setString(3,ville.getNom());
            preparedStatement.setString(4,ville.getDepartement().getCode());
            preparedStatement.setInt(5, ville.getRegion().getCode());
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
     * supprime la ville passée en paramètres
     * @param ville à supprimer
     * @return true si la suppression est effectuée avec succès, false sinon
     */
    @Override
    public boolean delete(Ville ville) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        int nb = 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("DELETE FROM Villes WHERE code_ville=? AND nom = ? AND population = ? AND code_dept = ? AND code_region = ?;");
            preparedStatement.setInt(1, ville.getCode());
            preparedStatement.setString(2,ville.getNom());
            preparedStatement.setInt(3, ville.getPopulation());
            preparedStatement.setString(4, ville.getDepartement().getCode());
            preparedStatement.setInt(5, ville.getRegion().getCode());
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
        return nb == 0 ? false : true;
    }
    /**
     * recherche une ville  par un ensemble de critères: code, population, nom, code région, etc.
     * @param criteres permettant de chercher une ville
     * @return une ville si elle est trouvée, null sinon
     */
    @Override
    public Ville getById(String criteres) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Ville ville = null;
        String []tabCriteres = criteres.split(";");
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("SELECT * FROM Villes WHERE code_ville=? AND nom = ? AND population = ?" +
                    " AND code_dept = ? AND code_region = ? ");
            preparedStatement.setInt(1, Integer.parseInt(tabCriteres[0]));
            preparedStatement.setString(2, tabCriteres[1]);
            preparedStatement.setInt(3, Integer.parseInt(tabCriteres[2]));
            preparedStatement.setString(4, tabCriteres[3]);
            preparedStatement.setInt(5, Integer.parseInt(tabCriteres[4]));

            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                ville = new Ville();
                ville.setId(rs.getInt("id"));
                ville.setCode(rs.getInt("code_ville"));
                ville.setNom(rs.getString("nom"));
                ville.setPopulation(rs.getInt("population"));
                Departement departement = new Departement();
                departement.setCode(rs.getString("code_dept"));
                ville.setDepartement(departement);
                Region region = new Region();
                region.setCode(rs.getInt("code_region"));
                ville.setRegion(region);
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
        return ville;
    }



}
