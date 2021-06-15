package fr.diginamic.recensement.core.service;

import fr.diginamic.recensement.core.dao.*;
import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Region;
import fr.diginamic.recensement.core.entites.Ville;

import java.util.List;
/** Une classe de service permettant de faire les opérations CRUD (Créer, Lire, Mettre à jour et Supprimer les départements)
 * @author Miryem HRARTI
 */
public class DepartementService {

    /**
     * Objets DAO qui vont communiquer avec la BDD pour effectuer les opérations CRUD
     */
    private RegionDao regionDao;
    private DepartementDao departementDao;
    private VilleDao villeDao;
    /**
     * Constructeur pour Instancier les différentes objets DAO
     */
    public DepartementService() {
        regionDao = new RegionDaoImpl();
        departementDao = new DepartementDaoImpl();
        villeDao = new VilleDaoImpl();
    }
    /**
     * Insérer un département dans la BDD (insérer aussi les villes et les départements qui lui sont associés)
     * @param departement nouvelle région à insérer dans la BDD
     */
    public void creerDepartement( Departement departement){
        Departement departementExiste = departementDao.getById(departement.getCode());
        if(departementExiste == null){
            departementDao.insert(departement);
            for(Ville ville: departement.getVilles()){
                String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
                Ville villeExiste = villeDao.getById(criteres);
                if(villeExiste == null) {
                    villeDao.insert(ville);
                }
                else
                    System.err.println("ATTENTION!!! La ville"+ville.getNom()+" existe déjà dans la BDD");
            }
            Region region = regionDao.getById(departement.getRegion().getCode());
            int nvPopulationRegion = region.getPopulation() + departement.getPopulation();
            regionDao.update(region.getCode(), nvPopulationRegion);
        }
        else
            System.err.println("ATTENTION!!!! Le département "+departement.getCode()+" existe déjà dans la BDD");

    }

    /**
     * Permet de récuperer une région par son code
     * @param code du département à récupérer
     */
    public void getDepartement(String code) {
        Departement departement = departementDao.getById(code);
        if (departement == null)
            System.out.println("Aucun département avec ce code: " + code);
        else
            System.out.println(departement);
    }

    /**
     * Lorsqu'on supprime un département, à cause de la contrainte d'intégrité, il faut supprimer ses villes
     *  avant de le supprimer
     * @param departement objet region à supprimer
     */
    public void supprimerDepartement (Departement departement){
        boolean estSupprime;
        Departement departementExiste = departementDao.getById(departement.getCode());
        if(departementExiste != null) {
            List<Ville> villes = departementDao.getVillesBy(departement.getCode());
            for(Ville ville: villes){
                System.out.println("Ville: "+ville);
                String criteres = ville.getCode()+";"+ville.getNom()+";"+ville.getPopulation()+";"+ville.getDepartement().getCode()+";"+ville.getRegion().getCode();
                Ville villeExiste = villeDao.getById(criteres);
                if(villeExiste != null) {
                    estSupprime = villeDao.delete(ville);
                    if (estSupprime) {
                        System.out.println(ville.getNom() + " est supprimée de la BDD");
                    }
                    else
                        System.out.println(ville.getNom() + " n'a pas été supprimée de la BDD");
                }
            }

            estSupprime = departementDao.delete(departement);
            if (estSupprime) {
                System.out.println(departement.getCode() + " est supprimé de la BDD");
                //update la population de la région
                Region region = regionDao.getById(departement.getRegion().getCode());
                int nvPopulationRegion = region.getPopulation() - departement.getPopulation();
                regionDao.update(region.getCode(), nvPopulationRegion);
            } else
                System.out.println(departement.getCode() + " n'a pas été supprimé de la BDD");
        }
    }
    /**
     * Afficher la liste de toutes les départements
     */
    public void afficherListe(){
        List<Departement> departementsDeFrance = departementDao.extraire();
        System.out.println("-------------------REGIONS DE FRANCE---------------");
        departementsDeFrance.stream().forEach(departement -> System.out.println(departement));
        System.out.println("TOTAL: "+departementsDeFrance.size()+" régions");
    }

    /**
     * modifier la population d'un département donné par son code
     * @param codeDept code du département que l'on veuille modifier
     * @param nvPopulation la nouvelle population
     */
    public void mettreAjour(String codeDept, int nvPopulation) {
        Departement departementExiste = departementDao.getById(codeDept);
        if (departementExiste != null) {
            int anciennePopulation = departementExiste.getPopulation();
            int nb = departementDao.update(codeDept, nvPopulation);
            if (nb != 0){
                System.out.println("Département N° " + codeDept + "a été mis à jour");
                Region region = regionDao.getById(departementExiste.getRegion().getCode());
                //update la population de la région
                int nvPopulationRegion = region.getPopulation() - anciennePopulation + nvPopulation;
                regionDao.update(region.getCode(), nvPopulationRegion);
            }

            else
                System.out.println("Département N° " + codeDept + "n'a pas été mis à jour");
        }
    }
}
