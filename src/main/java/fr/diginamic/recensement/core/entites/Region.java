package fr.diginamic.recensement.core.entites;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe Region caractérisée par un code, un nom, un nombre d'habitants, une liste de villes et de département
 * @author  Miryem HRARTI
 */
public class Region {
    private int code;
    private String nom;
    private int population;
    private List<Ville> villes = new ArrayList<>();
    private List<Departement> departements = new ArrayList<>();
    /**
     *
     * @return le code de la région
     */
    public int getCode() {
        return code;
    }
    /**
     * change le code de la région par un nouveau code
     * @param code : nouveau code
     */
    public void setCode(int code) {
        this.code = code;
    }
    /**
     *
     * @return le nom de la région
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @param nom le nouveau nom de la région
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @return le nombre d'habitants de la région
     */
    public int getPopulation() {
        return population;
    }

    /**
     *
     * @param population le nouveau nombre d'habitants
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     *
     * @return la liste des villes de la région
     */
    public List<Ville> getVilles() {
        return villes;
    }

    /**
     *
     * @param villes la nouvelle liste de villes de la région
     */
    public void setVilles(List<Ville> villes) {
        this.villes = villes;
    }

    /**
     *
     * @return la liste des départements de la région
     */
    public List<Departement> getDepartements() {
        return departements;
    }

    /**
     *
     * @param departements la nouvelle liste des départements de la région
     */
    public void setDepartements(List<Departement> departements) {
        this.departements = departements;
    }

    /**
     *
     * @return une chaine de caractères: description d'une région
     */
    @Override
    public String toString() {
        StringBuffer str = new StringBuffer(getClass().getSimpleName()+ "(CODE: " + code +
                ", NOM:" + nom + ", POPULATION: " + population + " HABIT");
               /*", NOMS VILLES={");
        for(Ville ville : villes)
            str.append(ville.getNom()+", ");
        str.append(" }, CODES DEPART: {");
        for(Departement departement : departements)
            str.append(departement.getCode()+", ");*/
        str.append(")");
        return str.toString();
    }

    /**
     * ajoute une ville à la liste des villes de la région
     * @param ville à ajouter à la liste des villes d'une région
     */
    public void ajouterVille(Ville ville){
        this.villes.add(ville);
        this.population += ville.getPopulation();
    }
    /**
     * ajoute un département à la liste des départements de la région
     * @param dept à ajouter à la liste
     */
    public void ajouterDepartement(Departement dept){
        this.departements.add(dept);
    }
    public void supprimerDepartement(Departement dept){
        this.departements.remove(dept);
    }
    public void supprimerVille(Ville ville){
        this.villes.remove(ville);
    }
}
