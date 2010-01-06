package models.outils;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Port;

/**
 * Cette classe permet de gerer tous les sons de l'application.
 * <p>
 * Celle-ci est entierement statique ce qui permet de gerer les sons depuis n'importe
 * ou dans la structure.
 * <p>
 * Remarques : <br>
 * Les sons sont "threades", c'est-a-dire qu'on peut sans autre lire plusieurs
 * fois le meme son dans le code.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurelien Da Campo
 * @author Lazhar Farjallah
 * @version 17 dec. 2009
 * @since jdk1.6.0_16
 */
public class GestionnaireSons
{
    // liste des sons
    private static Vector<Son2> sons = new Vector<Son2>();
    // controle d'entree pour le volume.
    private static Control ctrlIn;
    // ecouteur de son de la classe
    private static EcouteurDeSon eds;
    
    static
    {
        // une fois le son terminee, on le supprime de la collection des sons
        eds = new EcouteurDeSon(){
            @Override
            public void estTerminee(Son2 son)
            {
                sons.remove(son);
            }
        };
    }
    
    /**
     * Permet d'ajouter un son
     * 
     * @param son le son a ajouter
     */
    public static void ajouterSon(Son2 son)
    {
        son.ajouterEcouteurDeSon(eds); // ajout de l'ecouteur
        sons.add(son);
    }
    
    /**
     * Arrete tous les sons actuellement en lecture
     */
    public static void arreterTousLesSons()
    {
        synchronized (sons)
        {
            Enumeration<Son2> eSons = sons.elements();
            
            Son2 son;
            while(eSons.hasMoreElements())
            {
                son = eSons.nextElement();
                if(son.isAlive())
                    son.arreter();
            }
     
            sons.clear();
        }
    }
    
    /**
     * Arrete tous les sons du meme fichier actuellement en lecture
     * 
     * @param fichier le fichier des sons a arreter
     */
    public static void arreterTousLesSons(File fichier)
    {
        synchronized (sons)
        {
            Iterator<Son2> iSons = sons.iterator();
            Son2 son;
            while(iSons.hasNext())
            {
                son = iSons.next();
                
                if(son.getFichier() == fichier)
                {
                    son.arreter();
                    iSons.remove();
                }
            }
        }
    }
    
    /**
     * Permet de recuperer le nombre de sons actuellement en lecture
     *  
     * @return le nombre de sons actuellement en lecture
     */
    public static int getNbSonsEnLecture()
    {
        int nbSons = 0;
        
        for(Son2 son : sons)
            if(son.isAlive())
                nbSons++;
        
        return nbSons;
    }
    
    /**
     * Permet de recuper le nombre de sons du meme fichier actuellement en lecture
     * 
     * @param fichier le fichier des sons compter
     * 
     * @return le nombre de sons du meme fichier actuellement en lecture
     */
    public static int getNbSonsEnLecture(File fichier)
    {
        int nbSons = 0;
    
        synchronized (sons)
        {
            for(Son2 son : sons)
                if(son.getFichier() == fichier && son.isAlive())
                    nbSons++;
        
        }
        return nbSons;
    }
    
    /**
     * Cette methode permet de savoir (en pourcentage) quel est le volume actuel
     * du systeme.
     * 
     * @return Le pourcentage du volume du systeme actuel.
     */
    public static int getVolumeSysteme()
    {
        return 100 * (int) (((FloatControl) ctrlIn).getValue() / ((FloatControl) ctrlIn)
                .getMaximum());
    }

    /**
     * Cette methode permet de regler le volume du systeme en fonction d'un
     * pourcentage donne.
     * 
     * @param volumePourcent Le pourcentage du volume qu'on veut appliquer.
     */
    public static void setVolumeSysteme(int volumePourcent)
    {
        // Pour les ports de sortie audio.
        Port lineOut;

        try
        {
            // Le systeme possede une sortie LINE_OUT.
            if (AudioSystem.isLineSupported(Port.Info.LINE_OUT))
            {
                lineOut = (Port) AudioSystem.getLine(Port.Info.LINE_OUT);
                // On ouvre ce port.
                lineOut.open();
            }
            // Le systeme possede une sortie HEADPHONE.
            else if (AudioSystem.isLineSupported(Port.Info.HEADPHONE))
            {
                lineOut = (Port) AudioSystem.getLine(Port.Info.HEADPHONE);
                // On ouvre ce port.
                lineOut.open();
            }
            // Le systeme possede une sortie SPEAKER.
            else if (AudioSystem.isLineSupported(Port.Info.SPEAKER))
            {
                lineOut = (Port) AudioSystem.getLine(Port.Info.SPEAKER);
                // On ouvre ce port.
                lineOut.open();
            }
            // Le systeme ne possede pas de sortie audio (triste!).
            else
            {
                System.out.println("Impossible d'avoir une sortie audio!");
                return;
            }

            // On recupere le controle d'entree du volume.
            ctrlIn = lineOut.getControl(FloatControl.Type.VOLUME);
            // On change le volume du systeme avec celui donne en parametre.
            ((FloatControl) ctrlIn).setValue(volumePourcent / 100.0f);
        } catch (Exception erreur) // Une erreur liee au volume est survenue.
        {
            erreur.printStackTrace();
        }
    }
}