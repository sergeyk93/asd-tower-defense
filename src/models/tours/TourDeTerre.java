/*
  Copyright (C) 2010 Aurelien Da Campo, Lazhar Farjallah
  
  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package models.tours;

import i18n.Langue;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import models.attaques.BouleDeTerre;
import models.creatures.Creature;

/**
 * Classe de gestion d'une tour de terre.
 * <p>
 * La tour de terre est une tour lente qui fait enormement de degats
 * et qui a une tres grande portee. Malheureusement, elle n'attaque
 * que les creatures terrestre.
 * 
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Tour
 */
public class TourDeTerre extends Tour
{
    private static final long serialVersionUID = 1L;
    public static final Color COULEUR;
    public static final Image IMAGE;
    public static final Image ICONE;
    public static final int NIVEAU_MAX = 5;
    public static final int PRIX_ACHAT = 250;
    private static final double RAYON_IMPACT = 30;
   
    private static final String DESCRIPTION = Langue.getTexte(Langue.ID_TXT_DESC_TOUR_TERRE); 

    static
    {
        COULEUR = new Color(128,64,32);
        IMAGE   = Toolkit.getDefaultToolkit().getImage("img/tours/tourDeTerre.png");
        ICONE   = Toolkit.getDefaultToolkit().getImage("img/tours/icone_tourDeTerre.png");
    }
    
    public TourDeTerre()
    {
        super(0,                // x
              0,                // y
              20,               // largeur
              20,               // hauteur
              COULEUR,          // couleur de fond
              Langue.getTexte(Langue.ID_TXT_NOM_TOUR_TERRE),          // nom
              PRIX_ACHAT,       // prix achat
              400,              // degats
              150,              // rayon de portee
              0.5,              // cadence de tir (tirs / sec.)
              Tour.TYPE_TERRESTRE,// type
              IMAGE,            // image sur terrain
              ICONE);           // icone pour bouton 
    
        description = DESCRIPTION;
    }
    
    public void ameliorer()
    {
        if(peutEncoreEtreAmelioree())
        {
            // le prix total est ajouté du prix d'achat de la tour
            prixTotal   += prixAchat;
            
            // augmentation du prix du prochain niveau
            prixAchat   *= 2;
            
            // augmentation des degats
            degats      = getDegatsLvlSuivant();
            
            // augmentation du rayon de portee
            rayonPortee = getRayonPorteeLvlSuivant();
            
            // raccourcissement du temps de preparation du tire
            setCadenceTir(getCadenceTirLvlSuivant());
        
            niveau++;
        }
    }

    public void tirer(Creature creature)
    {
        jeu.ajouterAnimation(new BouleDeTerre(jeu,this,creature,degats,RAYON_IMPACT));
    }

    public Tour getCopieOriginale()
    {
        return new TourDeTerre();
    }

    public boolean peutEncoreEtreAmelioree()
    {
        return niveau < NIVEAU_MAX;
    }
    
    @Override
    public double getCadenceTirLvlSuivant()
    {
        return getCadenceTir() * 1.2;
    }

    @Override
    public long getDegatsLvlSuivant()
    {
        return (long) (degats * 1.5);
    }

    @Override
    public double getRayonPorteeLvlSuivant()
    {
        return rayonPortee + 10;
    }
}
