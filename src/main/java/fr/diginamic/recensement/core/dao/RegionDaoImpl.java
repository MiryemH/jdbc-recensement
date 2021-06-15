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
public class RegionDaoImpl implements RegionDao {

    /**
     * Récupère la liste des régions de la BDD
     * @return liste des régions extraits de la BDD
     */
    @Override
    public List<Region> extraire() {
        List<Region> regions = new ArrayList<>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            String sql = "SELECT * FROM Regions;";
            preparedStatement = connexion.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Region region = new Region();
                region.setCode(rs.getInt("code_region"));
                region.setNom(rs.getString("nom_region"));
                region.setPopulation(rs.getInt("population"));
                regions.add(region);
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
        return regions;
    }

    /**
     * insère une nouvelle région dans la BDD
     * @param region à insérer
     */
    @Override
    public void insert(Region region) {
            Connection connexion = null;
            PreparedStatement preparedStatement = null;
            try {
                connexion = ConnexionManagerBDD.getInstance();
                preparedStatement = connexion.prepareStatement("INSERT INTO Regions (code_region, nom_region, population) VALUES(?,?,?);");
                preparedStatement.setInt(1, region.getCode());
                preparedStatement.setString(2, region.getNom());
                preparedStatement.setInt(3, region.getPopulation());
                int nb = preparedStatement.executeUpdate();
                if (nb != 0) {
                    System.out.println("Region " + region.getNom() + " insérée dans la BDD");
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
     * met à jour la population d'une région donnée
     * @param codeRegion le code de la région  à modifier
     * @param nvPopulation la nouvelle population de la région
     * @return un entier 1 si mise à jour réussie, 0 sinon
     */
    @Override
    public int update(int codeRegion, int nvPopulation) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        int nb = 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("UPDATE Regions SET population = ? WHERE code_region=?;");
            preparedStatement.setInt(1,nvPopulation);
            preparedStatement.setInt(2, codeRegion);
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
     * supprime la région passée en paramètres
     * @param region à supprimer
     * @return true si la suppression est effectuée avec succès, false sinon
     */
    @Override
    public boolean delete(Region region) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        int nbR = 0;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("DELETE FROM Regions WHERE code_region=?;");
            preparedStatement.setInt(1, region.getCode());
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
     * recherche une région donnée par son code
     * @param codeRegion permettant de chercher une région
     * @return une region si elle est trouvée, null sinon
     */
    @Override
    public Region getById(int codeRegion) {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Region region = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            preparedStatement = connexion.prepareStatement("SELECT * FROM Regions WHERE code_region=?");
            preparedStatement.setInt(1, codeRegion);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                region = new Region();
                region.setCode(rs.getInt("code_region"));
                region.setNom(rs.getString("nom_region"));
                region.setPopulation(rs.getInt("population"));
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
        return region;
    }
    /**
     * récupérer les villes d'une région
     * @param code de la région
     * @return la liste des villes de la région
     */
    public List<Ville> getVillesBy(int code){
        List<Ville> villes = new ArrayList<>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            String sql = "SELECT * FROM Villes WHERE code_region= ? ;";
            preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setInt(1,code);
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

    /**
     * récupérer les departements d'une région
     * @param code de la région
     * @return la liste des departements de la région
     */
    public List<Departement> getDeptBy(int code){
        List<Departement> departements = new ArrayList<>();
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connexion = ConnexionManagerBDD.getInstance();
            String sql = "SELECT * FROM Departements WHERE code_region= ? ;";
            preparedStatement = connexion.prepareStatement(sql);
            preparedStatement.setInt(1,code);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Departement departement = new Departement();
                departement.setCode(rs.getString("code_dept"));
                Region region = new RegionDaoImpl().getById(rs.getInt("code_region"));
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

}


