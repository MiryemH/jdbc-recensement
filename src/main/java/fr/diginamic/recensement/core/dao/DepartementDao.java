package fr.diginamic.recensement.core.dao;

import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Ville;

import java.util.List;

/**
 * @author Miryem HRARTI
 * interface avec ses 4 opérations CRUD
 */
public interface DepartementDao {
    /**
     * Récupère la liste des départements de la BDD
     * @return liste des départements extraits de la BDD
     */
    List<Departement> extraire();

    /**
     * insère un nouveau département dans la BDD
     * @param departement à insérer
     */
    void insert(Departement departement);

    /**
     * met à jour la population du département identifié par codeDept
     * @param codeDept le code du département à modifier
     * @param nvPopulation la nouvelle population du département
     * @return un entier 1 si mise à jour réussie, 0 sinon
     */
    int update(String  codeDept, int nvPopulation);

    /**
     * supprime le département passé en paramètres
     * @param departement à supprimer
     * @return true si la suppression est effectuée avec succès, false sinon
     */
    boolean delete(Departement departement);

    /**
     * recherche un département par un code
     * @param code permettant de chercher un departement
     * @return un département s'il est trouvé, null sinon
     */
    Departement getById(String code);

    /**
     * récupérer les villes d'un départements
     * @param code du département
     * @return la liste des villes du département
     */
    List<Ville> getVillesBy(String code);
}
