/*
  Copyright (C) 2010 Aurelien Da Campo
  
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

package vues.reseau;

import i18n.Langue;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import exceptions.AucunEmplacementDisponibleException;
import exceptions.AucunePlaceDisponibleException;
import exceptions.JeuEnCoursException;
import models.jeu.*;
import models.joueurs.Joueur;
import models.outils.GestionnaireSons;
import models.terrains.*;
import outils.*;
import reseau.CanalException;
import vues.GestionnaireDesPolices;
import vues.LookInterface;
import vues.Panel_MenuPrincipal;
import vues.commun.Panel_EmplacementsTerrain;
import vues.commun.TableCellRenderer_Image;
import vues.solo.Fenetre_JeuSolo;

/**
 * Panel de création d'une partie réseau.
 * 
 * @author Aurelien Da Campo
 * @version 1.0 | mai 2010
 */
@SuppressWarnings("serial")
public class Panel_CreerPartieMulti extends JPanel implements ActionListener
{
    private final int MARGES_PANEL = 40;
    private final Dimension DEFAULT_DIMENTION_COMP = new Dimension(120, 25);

    private JFrame parent;
 
    // form
    private JLabel lblNomServeur = new JLabel(Langue.getTexte(Langue.ID_TITRE_NOM_SERVEUR));
    private JTextField tfNomServeur = new JTextField(Configuration.getPseudoJoueur()+"Server");
    //private JLabel lblEquipeAleatoire = new JLabel("Equipes aleatoires :");
    //private JCheckBox cbEquipeAleatoire = new JCheckBox();
    private JLabel lblTitreTerrains = new JLabel(Langue.getTexte(Langue.ID_TITRE_CHOISISSEZ_VOTRE_TERRAIN));
    private JLabel lblEtat = new JLabel();
    
    private JLabel lblPseudo = new JLabel(Langue.getTexte(Langue.ID_TITRE_PSEUDO));
    private JTextField tfPseudo = new JTextField("", 10);
    private JButton bCreer = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_CREER));
    private JButton bRetour = new JButton(Langue.getTexte(Langue.ID_TXT_BTN_RETOUR));
    
    // terrains
    private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
    private DefaultTableModel model = new DefaultTableModel();
    private JTable tbTerrains;
    Panel_EmplacementsTerrain pEmplacementTerrain = new Panel_EmplacementsTerrain(150, 150);
   
    /**
     * Constructeur
     * 
     * @param parent la fenetre parent
     */
    public Panel_CreerPartieMulti(JFrame parent)
    {
        // initialisation
        super(new BorderLayout());
        this.parent = parent;
        parent.setTitle(Langue.getTexte(Langue.ID_TITRE_CREER_PARTIE_MULTI));
        setBorder(new EmptyBorder(new Insets(MARGES_PANEL, MARGES_PANEL,
                MARGES_PANEL, MARGES_PANEL)));

        setBackground(LookInterface.COULEUR_DE_FOND_PRI);

        // ---------
        // -- TOP --
        // ---------
        JPanel pTop = new JPanel(new BorderLayout());

        pTop.setOpaque(false);

        JLabel lblTitre = new JLabel(Langue.getTexte(Langue.ID_TITRE_CREER_PARTIE_MULTI));
        lblTitre.setFont(GestionnaireDesPolices.POLICE_TITRE);
        lblTitre.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        pTop.add(lblTitre, BorderLayout.NORTH);

        add(pTop, BorderLayout.NORTH);

        // ------------
        // -- CENTER --
        // ------------
        JPanel pCentre = new JPanel(new GridBagLayout());
        pCentre.setBorder(new LineBorder(LookInterface.COULEUR_DE_FOND_SEC));
        pCentre.setOpaque(false);

        int ligne = 0;
        
        GridBagConstraints c = new GridBagConstraints();
        final int margesCellule = 15;
        c.insets = new Insets(margesCellule, margesCellule, margesCellule,
                margesCellule);
        c.anchor = GridBagConstraints.LINE_START;

 
        // --------------------
        // -- nom du serveur --
        // --------------------
/*
        c.gridx = 0;
        c.gridy = ligne;

        lblNomServeur.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblNomServeur.setForeground(GestionnaireDesPolices.COULEUR_TXT_SUR_COULEUR_DE_FOND);
        pCentre.add(lblNomServeur, c);

        c.gridx = 1;
        c.gridy = ligne;

        tfNomServeur.setPreferredSize(DEFAULT_DIMENTION_COMP);

        pCentre.add(tfNomServeur, c);
        */
        // ----------------------
        // -- equipe aléatoire --
        // ----------------------

        c.gridx = 2;
        c.gridy = ligne;
        
        /*
        lblEquipeAleatoire.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblEquipeAleatoire.setForeground(GestionnaireDesPolices.COULEUR_TXT_SUR_COULEUR_DE_FOND);
        pCentre.add(lblEquipeAleatoire, c);

        c.gridx = 3;
        c.gridy = ligne;

        pCentre.add(cbEquipeAleatoire, c);
        */

        // changement de ligne
        ligne++;
        
        
        // --------------
        // -- terrains --
        // --------------

        c.gridx = 0;
        c.gridy = ligne;
        c.gridwidth = 4;
        
        JPanel pTerrains = new JPanel(new BorderLayout());
        pTerrains.setPreferredSize(new Dimension(650, 250));
        pTerrains.setOpaque(false);

        
        
        lblTitreTerrains.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblTitreTerrains.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        pTerrains.add(lblTitreTerrains,BorderLayout.NORTH);
        
        // création de la table avec boquage des editions
        tbTerrains = new JTable(model)
        {
            public boolean isCellEditable(int rowIndex, int colIndex)
            {
                return false; // toujours désactivé
            }
        };
        
        // evenement sur le changement de sélection
        tbTerrains.getSelectionModel().addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent){
                
                if (listSelectionEvent.getValueIsAdjusting())
                    return;
                    
                ListSelectionModel lsm = (ListSelectionModel)listSelectionEvent.getSource();
                
                if (!lsm.isSelectionEmpty()) 
                {
                    int ligneSelectionnee = lsm.getMinSelectionIndex();
                    
                    pEmplacementTerrain.setTerrain(terrains.get(ligneSelectionnee));
                }
            }});

        // Simple selection
        tbTerrains.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // nom de colonnes
        model.addColumn(Langue.getTexte(Langue.ID_TXT_DESCRIPTION));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_MODE));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_JOUEURS_MAX));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_EQUIPES_MAX));
        model.addColumn(Langue.getTexte(Langue.ID_TXT_APERCU));

        
        // Taille des colonnes
        tbTerrains.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  
        tbTerrains.getColumnModel().getColumn(0).setPreferredWidth(210);
        tbTerrains.getColumnModel().getColumn(1).setPreferredWidth(60);
        tbTerrains.getColumnModel().getColumn(2).setPreferredWidth(60);
        tbTerrains.getColumnModel().getColumn(3).setPreferredWidth(60);
        tbTerrains.getColumnModel().getColumn(4).setPreferredWidth(60);
         
        // propiete des
        tbTerrains.setRowHeight(60);
        
        tbTerrains.getColumnModel().getColumn(4).setCellRenderer(
                new TableCellRenderer_Image());

        // Chargement de toutes les maps
        File repertoireMaps = new File("maps/multi");
        File[] listFiles = repertoireMaps.listFiles();
        
        Terrain t;
        String extFichier;
        
        int i = 0;
        for (File f2 : listFiles)
        {
            extFichier = OutilsFichier.getExtension(f2);

            if (extFichier.equals(Terrain.EXTENSION_FICHIER))
            {
                try{
                    t = Terrain.charger(f2);

                    terrains.add(t);
                    
                    Object[] obj = new Object[] { t.getBreveDescription(), ModeDeJeu.getNomMode(t.getMode()), t.getNbJoueursMax(), 
                            t.getEquipesInitiales().size()+"", t };
                    
                    model.addRow(obj);
                    
                    i++;
                } 
                catch (IOException e)
                {
                    lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                    lblEtat.setText("Error during loading of maps");
                    e.printStackTrace();
                } 
                catch (ClassNotFoundException e)
                {
                    lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                    lblEtat.setText("Error during loading of maps");
                    e.printStackTrace();
                } 
            }
        }
        
        // selection de la première map
        if(i > 0)
            tbTerrains.setRowSelectionInterval(0, 0);
        
        pTerrains.add(new JScrollPane(tbTerrains), BorderLayout.WEST);
        
        JPanel pTmp = new JPanel(new BorderLayout());
        pTmp.setOpaque(false);
        pTmp.add(new JScrollPane(pEmplacementTerrain), BorderLayout.NORTH);

        pTerrains.add(pTmp, BorderLayout.EAST);
        
        pCentre.add(pTerrains, c);

        
        // changement de ligne
        ligne++;
        
        // ajout du panel central
        add(pCentre, BorderLayout.CENTER);

        // ------------
        // -- BOTTOM --
        // ------------
        JPanel pBottom = new JPanel(new BorderLayout());
        pBottom.setOpaque(false);

        JPanel pMilieu = new JPanel();
        pMilieu.setOpaque(false);
        
        // nom du serveur
        lblNomServeur.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblNomServeur.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        pMilieu.add(lblNomServeur);

        tfNomServeur.setPreferredSize(DEFAULT_DIMENTION_COMP);

        pMilieu.add(tfNomServeur);
        
        
        // pseudo
        //JPanel pAlignementADroite = new JPanel(new BorderLayout());
        //pAlignementADroite.setOpaque(false);

        lblPseudo.setFont(GestionnaireDesPolices.POLICE_SOUS_TITRE);
        lblPseudo.setForeground(LookInterface.COULEUR_TEXTE_PRI);
        pMilieu.add(lblPseudo);
        
        tfPseudo.setText(Configuration.getPseudoJoueur());
        pMilieu.add(tfPseudo);
        
        
        
        
        
        
        
        
        
        pBottom.add(pMilieu, BorderLayout.CENTER);

        // bouton créer
        bCreer.setPreferredSize(new Dimension(100, 50));
        GestionnaireDesPolices.setStyle(bCreer);
        pBottom.add(bCreer, BorderLayout.EAST);
        bCreer.addActionListener(this);

        pBottom.add(lblEtat, BorderLayout.SOUTH);

        bRetour.addActionListener(this);
        GestionnaireDesPolices.setStyle(bRetour);
        bRetour.setPreferredSize(new Dimension(80,50));
        pBottom.add(bRetour, BorderLayout.WEST);

        add(pBottom, BorderLayout.SOUTH);
    }

    public String[] listFiles(String dir) throws Exception
    {
        return new File(dir).list();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object src = e.getSource();

        if (src == bCreer)
        {
            // Test des champs...
            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
            
            if(tfNomServeur.getText().isEmpty())
            {
                lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_NOM_SERVEUR_VIDE));
                return;
            }
 
            if(tbTerrains.getSelectedRow() == -1)
            {
                lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_PAS_DE_TERRAIN_SELECTIONNE));
                return;
            }
  
            if(tfPseudo.getText().isEmpty())
            {
                lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_PSEUDO_VIDE));
                return;
            }
            
            Configuration.setPseudoJoueur(tfPseudo.getText());

            //---------------------
            //-- Création du jeu --
            //---------------------
            
            Terrain terrain = terrains.get(tbTerrains.getSelectedRow());
            terrain.initialiser();
            
            if(terrain.getMode() == ModeDeJeu.MODE_SOLO)
            {
                Jeu_Solo jeu = new Jeu_Solo();

                jeu.setTerrain(terrain);
                terrain.setJeu(jeu);
                
                Joueur j = new Joueur("sans nom");
                jeu.setJoueurPrincipal(j);
                
                try
                {
                    jeu.ajouterJoueur(j);
                    
                    jeu.getTerrain().setLargeurMaillage(jeu.getTerrain().getLargeur());
                    jeu.getTerrain().setHauteurMaillage(jeu.getTerrain().getHauteur());
                    
                    jeu.getTerrain().initialiser();
                    jeu.initialiser();
                    
                    GestionnaireSons.arreterTousLesSons();
                    
                    new Fenetre_JeuSolo(jeu);
                    
                    parent.dispose();
                } 
                catch (JeuEnCoursException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } 
                catch (AucunePlaceDisponibleException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } 
            }
            else
            {
                Jeu_Serveur jeuServeur = new Jeu_Serveur();
                jeuServeur.setTerrain(terrain);
                
                terrain.setJeu(jeuServeur);
               
                Joueur joueur = new Joueur(tfPseudo.getText());
                
                
                // --------------------------------
                // -- Création du serveur de jeu --
                // --------------------------------
                try
                {
                    jeuServeur.etablissementDuServeur();
    
                    // ---------------------------------
                    // -- Connexion au serveur de jeu --
                    // ---------------------------------
                    
                    Jeu_Client jeuClient = new Jeu_Client(joueur);
                    
                    try
                    {
                        lblEtat.setForeground(LookInterface.COULEUR_TEXTE_PRI);
                        lblEtat.setText(Langue.getTexte(Langue.ID_TXT_TENTATIVE_DE_CONNEXION));
                        
                        // TODO port dynamique
                        try
                        {
                            jeuClient.connexionAvecLeServeur("127.0.0.1", Configuration.getPortSJ());
                        
                            // ---------------------------------------------------------------
                            // -- Enregistrement du serveur sur le serveur d'enregistrement --
                            // ---------------------------------------------------------------
                        
                            // Information
                            lblEtat.setForeground(LookInterface.COULEUR_TEXTE_PRI);
                            lblEtat.setText(Langue.getTexte(Langue.ID_TXT_ENREGISTREMENT_AU_SRV_CENTRAL)+"...");
    
                            if (jeuServeur.enregistrerSurSE(tfNomServeur.getText(),
                                    terrain.getNbJoueursMax(),
                                    (String) model.getValueAt(tbTerrains.getSelectedRow(), 0),
                                    terrain.getMode()))
                            {
                                lblEtat.setForeground(LookInterface.COULEUR_SUCCES);
                                lblEtat.setText(Langue.getTexte(Langue.ID_TXT_ENREGISTREMENT_AU_SRV_CENTRAL_REUSSI));
                            } 
                            else
                            {
                                lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                                lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_ENREGISTREMENT_AU_SRV_CENTRAL_ECHOUE));
                            }
                            
                            // connexion réussie
                            parent.getContentPane().removeAll();
                            parent.getContentPane().add(
                                    new Panel_AttendreJoueurs(parent, jeuServeur, jeuClient),
                                    BorderLayout.CENTER);
                            parent.getContentPane().validate();
                        } 
                        catch (AucunEmplacementDisponibleException e1)
                        {
                            lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                            lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_PAS_DE_PLACE));
                        }
                    }
                    catch (ConnectException e2)
                    {
                        lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                        lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_CONNEXION_IMPOSSIBLE));
                    } 
                    catch (CanalException e3)
                    {
                        lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                        lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_CONNEXION_IMPOSSIBLE));
                    } 
                } 
                catch (IOException e1)
                {
                    lblEtat.setForeground(LookInterface.COULEUR_ERREUR);
                    lblEtat.setText(Langue.getTexte(Langue.ID_ERREUR_CREATION_IMPOSSIBLE_UN_SRV_PAR_MACHINE));
                }
            }
        } 
        else if (src == bRetour)
        {
            parent.getContentPane().removeAll();
            parent.getContentPane().add(new Panel_MenuPrincipal(parent),
                    BorderLayout.CENTER);
            parent.getContentPane().validate();
        }
    }
}
