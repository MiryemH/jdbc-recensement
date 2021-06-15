package fr.diginamic.recensement.core.dao;

import fr.diginamic.recensement.core.entites.Ville;

import java.util.List;
/**
 * @author Miryem HRARTI
 * interface avec ses 4 opérations CRUD
 */
public interface VilleDao {

    /**
     * Récupère la liste des villes de la BDD
     * @return liste des villes extraits de la BDD
     */
    List<Ville> extraire( );

    /**
     * insère une nouvelle ville dans la BDD
     * @param ville à insérer
     */
    void insert(Ville ville);

    /**
     * met à jour la population d'une ville donnée
     * @param ville  à modifier
     * @param nvPopulation la nouvelle population de la ville
     * @return un entier 1 si mise à jour réussie, 0 sinon
     */
    int update(Ville ville, int nvPopulation);

    /**
     * supprime la ville passée en paramètres
     * @param ville à supprimer
     * @return true si la suppression est effectuée avec succès, false sinon
     */
    boolean delete(Ville ville);

    /**
     * recherche une ville  par un ensemble de critères: code, population, nom, code région, etc.
     * @param criteres permettant de chercher une ville
     * @return une ville si elle est trouvée, null sinon
     */
    Ville getById(String criteres);

}
