package fr.diginamic.recensement.core.dao;

import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Region;
import fr.diginamic.recensement.core.entites.Ville;

import java.util.List;
/**
 * @author Miryem HRARTI
 * interface avec ses 4 opérations CRUD
 */
public interface RegionDao {
    /**
     * Récupère la liste des régions de la BDD
     * @return liste des régions extraits de la BDD
     */
    List<Region> extraire();

    /**
     * insère une nouvelle région dans la BDD
     * @param region à insérer
     */
    void insert(Region region);

    /**
     * met à jour la population d'une région donnée
     * @param codeRegion le code de la région  à modifier
     * @param nvPopulation la nouvelle population de la région
     * @return un entier 1 si mise à jour réussie, 0 sinon
     */
    int update(int codeRegion, int nvPopulation);

    /**
     * supprime la région passée en paramètres
     * @param region à supprimer
     * @return true si la suppression est effectuée avec succès, false sinon
     */
    boolean delete(Region region);

    /**
     * recherche une région donnée par son code
     * @param codeRegion permettant de chercher une région
     * @return une region si elle est trouvée, null sinon
     */
    Region getById(int codeRegion);

    /**
     * récupérer les villes d'une région
     * @param code de la région
     * @return la liste des villes de la région
     */
    List<Ville> getVillesBy(int code);

    /**
     * récupérer les departements d'une région
     * @param code de la région
     * @return la liste des departements de la région
     */
    List<Departement> getDeptBy(int code);

}
