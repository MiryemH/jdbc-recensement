package fr.diginamic.recensement.core.service;

import fr.diginamic.recensement.core.dao.*;
import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Region;
import fr.diginamic.recensement.core.entites.Ville;

import java.util.List;

/** Une classe de service permettant de faire les opérations CRUD (Créer, Lire, Mettre à jour et Supprimer les villes)
 * @author Miryem HRARTI
 */
public class VilleService {
    /**
     * Objets DAO qui vont communiquer avec la BDD pour effectuer les opérations CRUD
     */
    private RegionDao regionDao;
    private DepartementDao departementDao;
    private VilleDao villeDao;

    /**
     * Constructeur pour Instancier les différents objets DAO
     */
    public VilleService() {
        regionDao = new RegionDaoImpl();
        departementDao = new DepartementDaoImpl();
        villeDao = new VilleDaoImpl();
    }

    /**
     * Insérer une ville dans la BDD et mettre à jour la population dans département et région
     * @param ville nouvelle ville à insérer dans la BDD
     */
    public void creerVille( Ville ville){
        String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
        Ville villeExiste = villeDao.getById(criteres);
        if(villeExiste == null) {
            villeDao.insert(ville);
            Departement departement = departementDao.getById(ville.getDepartement().getCode());
            Region region = regionDao.getById(ville.getRegion().getCode());
            int nvPopulationRegion = region.getPopulation() + ville.getPopulation();
            int nvPopulationDept = departement.getPopulation() + ville.getPopulation();
            regionDao.update(ville.getRegion().getCode(), nvPopulationRegion);
            departementDao.update(ville.getDepartement().getCode(), nvPopulationDept);
        }
    }

    /**
     * Mettre à jour la population  d'une ville dans la BDD et mettre à jour la population dans département et région
     * @param ville à modifier
     * @param nvPopulation la nouvelle population
     */
    public void mettreAjour(Ville ville, int nvPopulation){
        String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
        Ville villeExiste = villeDao.getById(criteres);
        if(villeExiste != null) {
            int anciennePopulation = ville.getPopulation();
            int nb = villeDao.update(ville, nvPopulation);
            if(nb != 0) {
                Departement departement = departementDao.getById(ville.getDepartement().getCode());
                Region region = regionDao.getById(ville.getRegion().getCode());
                int nvPopulationRegion = region.getPopulation() + nvPopulation - anciennePopulation;
                int nvPopulationDept = departement.getPopulation() + nvPopulation - anciennePopulation;
                regionDao.update(region.getCode(), nvPopulationRegion);
                departementDao.update(departement.getCode(), nvPopulationDept);
                System.out.println(ville.getNom() + " a été mise à jour (population modifiée)");            }
            else
                System.out.println(ville.getNom() + " n'a pas été mise à jour (population modifiée)");
        }
    }

    /**
     * On cherche une ville par une combinaison de critères: code, nom, code_region, code_dept et population
     * @param criteres sur lesquels on se base pour récupérer la ville
     */
    public void getVille(String criteres) {
        Ville ville = villeDao.getById(criteres);
        if (ville == null)
            System.out.println("Aucune région avec ces critères: " + criteres);
        else
            System.out.println(ville);
    }

    /**
     * Lorsqu'on supprime une ville, on doit mettre à jour la population (reduire la population) du
     * département et de la ville à laquelle elle appartient
     * @param ville objet ville à supprimer
     */
    public void supprimerVille (Ville ville){
        String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
        Ville villeExiste = villeDao.getById(criteres);
        if(villeExiste != null) {
            boolean estSupprimee = villeDao.delete(ville);
            if (!estSupprimee)
                System.out.println(ville.getNom() + " n'a pas été supprimée");
                //il faut mettre à jour la population du département et de la région
            else {
                Departement departement = departementDao.getById(ville.getDepartement().getCode());
                Region region = regionDao.getById(ville.getRegion().getCode());
                int nvPopulationRegion = region.getPopulation() - ville.getPopulation();
                int nvPopulationDept = departement.getPopulation() - ville.getPopulation();
                regionDao.update(region.getCode(), nvPopulationRegion);
                departementDao.update(departement.getCode(), nvPopulationDept);
                System.out.println(ville.getNom() + " est supprimée de la BDD");

            }
        }
    }

    /**
     * Afficher la liste de toutes les villes
     */
    public void afficherListe(){
        List<Ville> villesDeFrance = villeDao.extraire();
        System.out.println("-------------------VILLES DE FRANCE---------------");
        villesDeFrance.stream().forEach(ville -> System.out.println(ville));
        System.out.println("TOTAL: "+villesDeFrance.size()+" villes");
    }


}
