package fr.diginamic.recensement.core.entites;

import java.util.ArrayList;
import java.util.List;

/**
 * La classe Departement représente un département ayant un code, un nombre d'habitants, une region à laquelle appartient
 * et une liste de villes qu'il contient
 *  @author: Miryem HRARTI
 */
public class Departement {
    private String code;
    private int population;
    private Region region;
    private List<Ville> villes = new ArrayList<>();


    /**
     *
     * @return le code du département
     */
    public String getCode() {
        return code;
    }
    /**
     * change le code du département par un nouveau code
     * @param code : nouveau code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     *
     * @return le nombre d'habitants du département
     */
    public int getPopulation() {
        return population;
    }
    /**
     * change le nombre d'habitants du département
     * @param population: nouveau nombre de population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     *
     * @return  la région à laquelle appartient le département
     */
    public Region getRegion() {
        return region;
    }
    /**
     *
     * @param region: nouvelle région
     */
    public void setRegion(Region region) {
        this.region = region;
    }

    /**
     *
     * @return les villes du département
     */

    public List<Ville> getVilles() {
        return villes;
    }

    /**
     *
     * @param villes la nouvelle liste de villes
     */
    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }

    /**
     * ajoute une ville à la liste des villes du département
     * @param ville à ajouter à la liste des villes d'un département
     */
    public void ajouterVille(Ville ville){
        this.villes.add(ville);
        this.population += ville.getPopulation();
    }
    public void supprimerVille(Ville ville){
        this.villes.remove(ville);
    }
    /**
     * retourne une description du département: code, population, et nom région
     * @return une chaine de caractères
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer(getClass().getSimpleName()+ "(CODE: " + code+ " REGION{ CODE: "+region.getCode() +
                ", NOM: "+region.getNom()+"}, POPULATION: " + population + " HABIT" +")");
        return str.toString();
    }
}
