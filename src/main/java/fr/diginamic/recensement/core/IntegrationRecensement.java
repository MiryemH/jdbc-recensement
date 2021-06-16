package fr.diginamic.recensement.core;

import fr.diginamic.recensement.core.entites.Departement;
import fr.diginamic.recensement.core.entites.Region;
import fr.diginamic.recensement.core.entites.Ville;
import fr.diginamic.recensement.core.service.DepartementService;
import fr.diginamic.recensement.core.service.RegionService;
import fr.diginamic.recensement.core.service.VilleService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Miryem HRARTI
 * Une classe exécutable permettant de tester les services:
 * Création, mise à jour, suppression, affichage à partir d'une BDD
 * offerts par les classes XXXService
 */
public class IntegrationRecensement {
    /**
     * Objets de classe permettant de récupérer les régions, les départements et les villes de FRANCE
     * lus à partir d'un fichier de recensement
     */
    private static Map<Integer, Region> regions = new HashMap<Integer, Region>();
    private static List<Ville> villes = new ArrayList<>();
    private static Map<String, Departement>  departements = new HashMap<>();

    /**
     * méthode exécutable
     * @param args non utilisés
     */
    public static void main(String[] args) {
        RegionService regionService = new RegionService();
        DepartementService departementService = new DepartementService();
        VilleService villeService = new VilleService();
        try {
            //Lire le fichier et construire les villes, départements et régions
            List<String> lignes = lireFichier("recensement_population.csv");
            construireEntites(lignes);
            /*---------------------------------------------------------------------------------------
                                         Insertion sans duplication des
                                          regions, villes et départements
             --------------------------------------------------------------------------------------*/
            //Il suffit juste d'appeler RegionService (l'insertion se fait dans les trois tables)
            /*for(Region region : new ArrayList<>(regions.values()))
                regionService.creerRegion(region);


            regionService.afficherListe();
            departementService.afficherListe();
            villeService.afficherListe();*/
            /*---------------------------------------------------------------------------------------
                                         Scenario d'insertion, suppression et mise à jour de region, departements et villes
             --------------------------------------------------------------------------------------*/
            Region nvRegion = new Region();
            nvRegion.setCode(119);
            nvRegion.setNom("Nouvelle Normandie");
            Departement dept1 = new Departement();
            dept1.setCode("MSY1");
            dept1.setRegion(nvRegion);
            Departement dept2 = new Departement();
            dept2.setCode("MSY2");
            dept2.setRegion(nvRegion);
            Ville v1Dept1 = new Ville();
            v1Dept1.setCode(5001);
            v1Dept1.setNom("ville1");
            v1Dept1.setPopulation(15000);
            v1Dept1.setRegion(nvRegion);
            v1Dept1.setDepartement(dept1);

            Ville v2Dept1 = new Ville();
            v2Dept1.setCode(5002);
            v2Dept1.setNom("ville2");
            v2Dept1.setPopulation(50000);
            v2Dept1.setRegion(nvRegion);
            v2Dept1.setDepartement(dept1);

            Ville v1Dept2 = new Ville();
            v1Dept2.setCode(6001);
            v1Dept2.setNom("ville3");
            v1Dept2.setPopulation(6500);
            v1Dept2.setRegion(nvRegion);
            v1Dept2.setDepartement(dept2);

            Ville v2Dept2 = new Ville();
            v2Dept2.setCode(6002);
            v2Dept2.setNom("ville4");
            v2Dept2.setPopulation(50000);
            v2Dept2.setRegion(nvRegion);
            v2Dept2.setDepartement(dept2);

            nvRegion.ajouterVille(v1Dept1);
            nvRegion.ajouterVille(v2Dept1);
            nvRegion.ajouterVille(v1Dept2);
            nvRegion.ajouterVille(v2Dept2);
            dept1.ajouterVille(v1Dept1);
            dept1.ajouterVille(v2Dept1);
            dept2.ajouterVille(v1Dept2);
            dept2.ajouterVille(v2Dept2);
            nvRegion.ajouterDepartement(dept1);
            nvRegion.ajouterDepartement(dept2);
            //insertion + suppression
            regionService.creerRegion(nvRegion);
            villeService.supprimerVille(v1Dept1);
            departementService.getDepartement(dept1.getCode());
            regionService.getRegion(nvRegion.getCode());
            Departement dept3 = new Departement();
            dept3.setCode("MSY3");
            dept3.setRegion(nvRegion);

            // insertion + mise à jour
            Ville v1Dept3 = new Ville();
            v1Dept3.setCode(7003);
            v1Dept3.setNom("ville5");
            v1Dept3.setPopulation(65000);
            v1Dept3.setRegion(nvRegion);
            v1Dept3.setDepartement(dept3);
            Ville v2Dept3 = new Ville();
            v2Dept3.setCode(7004);
            v2Dept3.setNom("ville6");
            v2Dept3.setPopulation(70000);
            v2Dept3.setRegion(nvRegion);
            v2Dept3.setDepartement(dept3);
            dept3.ajouterVille(v1Dept3);

            departementService.creerDepartement(dept3);
            villeService.creerVille(v2Dept3);
            villeService.mettreAjour(v2Dept3, 100000);
            departementService.getDepartement(dept3.getCode());
            regionService.getRegion(nvRegion.getCode());

            departementService.supprimerDepartement(dept2);
            regionService.getRegion(nvRegion.getCode());
            regionService.supprimerRegion(nvRegion);

        } catch (IOException ioException) {
            System.err.println("Erreur d'accès au fichier. "+ioException.getMessage());
        }
    }

    /**
     * Une méthode de classe permettant de lire un fichier
     * en elevant la première ligne, elle retourne une liste de lignes et lève une exception si
     * on n'arrive pas à accéder au fichier
     * @param nomFichier à lire
     * @return une liste de lignes lues à partir du fichier
     * @throws IOException
     */
    public static List<String> lireFichier(String nomFichier) throws IOException {
        Path fichierIn = Paths.get(nomFichier);
        List<String> lignesFichier = new ArrayList<>();
        if(Files.isRegularFile(fichierIn) && Files.isReadable(fichierIn)){
            List<String> lignes = Files.readAllLines(fichierIn);
            Iterator<String> iterator = lignes.iterator();
            //Sauter la premiere ligne contenant les noms de colonnes
            iterator.next();
            while(iterator.hasNext()){
                String ligne = iterator.next().trim();
                lignesFichier.add(ligne);
            }
        }
        else{
            System.err.println("ATTENTION!!!! Le fichier en question ne peut pas être lu");
        }
        return lignesFichier;

    }

    /**
     * parse chaque ligne et stocke les informations dans les classes Ville, Region, Departement
     * @param lignes liste des lignes à lire
     */
    public static void construireEntites(List<String> lignes){
        for(String ligne : lignes) {
            String[] infos = ligne.split(";");
            Region region = new Region();
            region.setCode(Integer.parseInt(infos[0]));
            region.setNom(infos[1]);
            Departement departement = new Departement();
            departement.setCode(infos[2]);
            departement.setRegion(region);
            Ville ville = new Ville();
            ville.setCode(Integer.parseInt(infos[5])); //code
            ville.setNom(infos[6]);//nom
            ville.setPopulation(Integer.parseInt(infos[9].replace(" ","")));//population
            ville.setDepartement(departement);
            ville.setRegion(region);
            region.ajouterVille(ville);
            region.ajouterDepartement(departement);
            departement.ajouterVille(ville);
            //La sauvegarde
            villes.add(ville);
            if (!regions.containsKey(region.getCode()))
                regions.put(region.getCode(), region);
            else {
                Region region1 = regions.get(region.getCode());
                region1.setPopulation(region1.getPopulation() + region.getPopulation());
            }
            if (!departements.containsKey(departement.getCode()))
                departements.put(departement.getCode(), departement);
            else {
                Departement departement1 = departements.get(departement.getCode());
                departement1.setPopulation(departement1.getPopulation() + region.getPopulation());
            }
        }
    }


}
