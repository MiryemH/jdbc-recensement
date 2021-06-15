package fr.diginamic.recensement.core.entites;

/**
 * La classe Ville représente une ville ayant un code, un nom, un nombre d'habitants, region et département auxquels appartient elle appartient
 * @author: Miryem HRARTI
 *
 */
public class Ville {
    /**
     * id: pour la clé primaire côté BDD
     * code: code de la ville
     * nom: nom de la ville
     * region:  à laquelle appartient une ville
     * departement: code département auquel appartient la ville
     * population: nbre d'habitants dans la ville
     */
    private int id;
    private int code;
    private String nom;
    private Region region;
    private Departement departement;
    private int population;
    /**
     * retourne l'id de la ville
     * @return l'id de la ville
     */
    public int getId() {
        return id;
    }

    /**
     * change le code de la ville par un nouveau code
     * @param id : nouveau code
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return le code de la ville
     */
    public int getCode() {
        return code;
    }

    /**
     * change le code de la ville par un nouveau code
     * @param code : nouveau code
     */
    public void setCode(int code) {
        this.code = code;
    }


    /**
     *
     * @return le nom de la ville
     */
    public String getNom() {
        return nom;
    }

    /**
     * change le nom de la ville par un nouveau nom
     * @param nom : nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     *
     * @return la région à laquelle appartient la ville
     */
    public Region getRegion() {
        return region;
    }

    /**
     *
     * @param region: la nouvelle région
     */
    public void setRegion(Region region) {
        this.region= region;
    }

    /**
     *
     * @return le département de la ville
     */
    public Departement getDepartement() {
        return departement;
    }

    /**
     *
     * @param departement: nouveau département
     */
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    /**
     *
     * @return le nombre d'habitants de la ville
     */
    public int getPopulation() {
        return population;
    }

    /**
     * change le nombre d'habitants par un autre
     * @param population: nouveau nombre de population
     */
    public void setPopulation(int population) {
        this.population = population;
    }

    /**
     * retourne la description de la ville: code, nom, region, etc.
     * @return une chaine de caractères
     */
    @Override
    public String toString() {
        return getClass().getSimpleName()+
                "(CODE: " + code +
                ", NOM: " + nom  +
                ", REGION{CODE:" +region.getCode() + ", NOM: "+region.getNom()+
                "}, CODE DEPT:" + departement.getCode() +
                ", " + population + " HABIT)";
    }
}
