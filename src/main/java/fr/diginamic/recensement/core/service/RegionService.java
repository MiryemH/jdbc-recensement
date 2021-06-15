package fr.diginamic.recensement.core.service;

import fr.diginamic.recensement.core.dao.*;
import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Region;
import fr.diginamic.recensement.core.entites.Ville;

import java.util.List;
/** Une classe de service permettant de faire les opérations CRUD (Créer, Lire, Mettre à jour et Supprimer les régions)
 * @author Miryem HRARTI
 */
public class RegionService {
    /**
     * Objets DAO qui vont communiquer avec la BDD pour effectuer les opérations CRUD
     */
    private RegionDao regionDao;
    private DepartementDao departementDao;
    private VilleDao villeDao;
    /**
     * Constructeur pour Instancier les différentes objets DAO
     */
    public RegionService() {
        regionDao = new RegionDaoImpl();
        departementDao = new DepartementDaoImpl();
        villeDao = new VilleDaoImpl();
    }
    /**
     * Insérer une région dans la BDD (insérer aussi les villes et les départements qui lui sont associés)
     * @param region nouvelle région à insérer dans la BDD
     */
    public void creerRegion( Region region){
        Region regionExiste = regionDao.getById(region.getCode());
        if(regionExiste == null){
            regionDao.insert(region);
            for(Departement dept: region.getDepartements()){
                Departement departementExiste = departementDao.getById(dept.getCode());
                if(departementExiste == null){
                    departementDao.insert(dept);
                }
                else
                    System.err.println("ATTENTION!!! Le département "+dept.getCode()+" existe déjà dans la BDD");
            }
            for(Ville ville: region.getVilles()){
                String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
                Ville villeExiste = villeDao.getById(criteres);
                if(villeExiste == null) {
                    villeDao.insert(ville);
                }
                else
                    System.err.println("ATTENTION!!! La ville"+ville.getNom()+" existe déjà dans la BDD");
            }
        }
        else
            System.err.println("ATTENTION!!!! La région "+region.getNom()+" existe déjà dans la BDD");

    }

    /**
     * Permet de récuperer une région par son code
     * @param id de la région à récupérer
     */
    public void getRegion(int id) {
        Region region = regionDao.getById(id);
        if (region == null)
            System.out.println("Aucune région avec l'id: " + id);
        else
            System.out.println(region);
    }

    /**
     * Lorsqu'on supprime une région, à cause de la contrainte d'intégrité, il faut supprimer les villes
     * de la région puis ses départements avant de la supprimer
     * @param region objet region à supprimer
     */
    public void supprimerRegion (Region region){
        boolean estSupprime;
        Region regionExiste = regionDao.getById(region.getCode());
        if(regionExiste != null) {
            List<Ville> villes = regionDao.getVillesBy(region.getCode());
            List<Departement> departements = regionDao.getDeptBy(region.getCode());
            for(Ville ville: villes){
                String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
                Ville villeExiste = villeDao.getById(criteres);
                if(villeExiste != null) {
                    estSupprime = villeDao.delete(ville);
                    if (estSupprime)
                        System.out.println(ville.getNom() + " est supprimée de la BDD");
                    else
                        System.out.println(ville.getNom() + " n'a pas été supprimée de la BDD");
                }
            }

            for(Departement dept: departements) {
                Departement departementExiste = departementDao.getById(dept.getCode());
                if(departementExiste != null){
                    estSupprime = departementDao.delete(dept);
                    if (estSupprime)
                        System.out.println(dept.getCode() + " est supprimé de la BDD");
                    else
                        System.out.println(dept.getCode() + " n'a pas été supprimé de la BDD");
                }
            }
            estSupprime = regionDao.delete(region);
            if (estSupprime)
                System.out.println(region.getNom() + " est supprimée de la BDD");
            else
                System.out.println(region.getNom() + " n'a pas été supprimée de la BDD");
        }
    }
    /**
     * Afficher la liste de toutes les villes
     */
    public void afficherListe(){
        List<Region> regionsDeFrance = regionDao.extraire();
        System.out.println("-------------------REGIONS DE FRANCE---------------");
        regionsDeFrance.stream().forEach(region -> System.out.println(region));
        System.out.println("TOTAL: "+regionsDeFrance.size()+" régions");
    }

    /**
     * modifier la population d'une région donnée par son code
     * @param codeRegion code de la région que l'on veuille modifier
     * @param population la nouvelle population
     */
    public void mettreAjour(int codeRegion, int population){
        int nb = regionDao.update(codeRegion, population);
        if(nb != 0)
            System.out.println("Region N° "+codeRegion + "a été mise à jour");
        else
            System.out.println("Region N° "+codeRegion + "n'a pas été mise à jour");
    }
}
