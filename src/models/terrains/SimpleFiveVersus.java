package models.terrains;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import models.jeu.Jeu;
import models.jeu.ModeDeJeu;
import models.joueurs.EmplacementJoueur;
import models.joueurs.Equipe;

/**
 * Classe de gestion du fameux terrain Element TD repris de chez Blizzard.
 * 
 * Cette classe herite de la classe Terrain de base.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 13 decembre 2009
 * @since jdk1.6.0_16
 * @see Terrain
 */
public class SimpleFiveVersus extends Terrain
{
    private static final long serialVersionUID = 1L;
    public final static Image IMAGE_DE_FOND;
    public final static Image IMAGE_MENU;
    public final static File  FICHIER_MUSIQUE_DE_FOND;
    public final static String NOM = "Simple Versus de 2 à 5 joueurs";

    static
    {
        FICHIER_MUSIQUE_DE_FOND = new File("snd/blizzard/Human_I_(Fanfare).mp3");
        
        IMAGE_MENU    = Toolkit.getDefaultToolkit().getImage(
                                          "img/cartes/menu_principal/elementTD.png");
    	IMAGE_DE_FOND = Toolkit.getDefaultToolkit().getImage(
    	                                                 "img/cartes/simpleFiveVersus.png");
    }
	
    /**
     * Constructeur d'un terrain ElementTD selon la celebre map de Blizzard.
     */
    public SimpleFiveVersus (Jeu jeu) 
    {
        super(  jeu,
                520,  // largeur
                500,  // hauteur
                100,  // nbPiecesOrInitiales
                20,   // nbViesInitiales
                0,    // positionMaillageX
                0,    // positionMaillageY
                520,  // largeurMaillage
                500,  // hauteurMaillage
                ModeDeJeu.MODE_VERSUS, // mode de jeu
                new Color(197,148,90), // couleur de fond
                new Color(91,123,43),  // couleur des murs
                IMAGE_DE_FOND, // imageDeFond
                NOM  // nom
          );
 
        
        // Création des équipes
        Equipe e1 = new Equipe("Les Rouges",Color.RED);
        e1.ajouterZoneDepartCreatures(new Rectangle(110, 0, 80, 20));
        e1.setZoneArriveeCreatures(new Rectangle(230, 0, 80, 20));
        e1.ajouterEmplacementJoueur(new EmplacementJoueur(new Rectangle(20,0,80,500),Color.RED));
        equipes.add(e1);
        
        Equipe e2 = new Equipe("Les Bleus",Color.BLUE);
        e2.ajouterZoneDepartCreatures(new Rectangle(110, 0, 80, 20));
        e2.setZoneArriveeCreatures(new Rectangle(230, 0, 80, 20));
        e2.ajouterEmplacementJoueur(new EmplacementJoueur(new Rectangle(120,0,80,500),Color.BLUE));
        equipes.add(e2);
        
        Equipe e3 = new Equipe("Les Verts",Color.GREEN);
        e3.ajouterZoneDepartCreatures(new Rectangle(110, 0, 80, 20));
        e3.setZoneArriveeCreatures(new Rectangle(230, 0, 80, 20));
        e3.ajouterEmplacementJoueur(new EmplacementJoueur(new Rectangle(220,0,80,500),Color.GREEN));
        equipes.add(e3);
        
        Equipe e4 = new Equipe("Les Jaunes",Color.YELLOW);
        e4.ajouterZoneDepartCreatures(new Rectangle(110, 0, 80, 20));
        e4.setZoneArriveeCreatures(new Rectangle(230, 0, 80, 20));
        e4.ajouterEmplacementJoueur(new EmplacementJoueur(new Rectangle(320,0,80,500),Color.YELLOW));
        equipes.add(e4);
        
        Equipe e5 = new Equipe("Les Noirs",Color.BLACK);
        e5.ajouterZoneDepartCreatures(new Rectangle(110, 0, 80, 20));
        e5.setZoneArriveeCreatures(new Rectangle(230, 0, 80, 20));
        e5.ajouterEmplacementJoueur(new EmplacementJoueur(new Rectangle(420,0,80,500),Color.BLACK));
        equipes.add(e5);
        
        fichierMusiqueDAmbiance = FICHIER_MUSIQUE_DE_FOND;
        
        /*
         * Définition des murs du labyrinthe.
         */
        ajouterMur(new Rectangle(20, 0, 80, 20));
        ajouterMur(new Rectangle(0, 0, 20, 500));
        ajouterMur(new Rectangle(20, 480, 440, 20));
        ajouterMur(new Rectangle(460, 0, 20, 500));
        ajouterMur(new Rectangle(320, 0, 140, 20));
        
        ajouterMur(new Rectangle(200, -40, 20, 140));
        
        ajouterMur(new Rectangle(120, 100, 240, 20));
        ajouterMur(new Rectangle(120, 120, 20, 20));
        ajouterMur(new Rectangle(340, 120, 20, 260));
        ajouterMur(new Rectangle(120, 360, 220, 20));
        ajouterMur(new Rectangle(20, 240, 220, 20));
        ajouterMur(new Rectangle(220, 220, 20, 20));
    }
}