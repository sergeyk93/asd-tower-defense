package models.creatures;

import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe de gestion d'une creature.
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 1.0 | 27 novemenbre 2009
 * @since jdk1.6.0_16
 * @see Creature
 */
public class Creature1 extends Creature
{
	private static final long serialVersionUID = 1L;
	
	private static final Image IMAGE;
	
	
	static
	{
		IMAGE = Toolkit.getDefaultToolkit().getImage("img/creatures/creature1.png");
	}
	
	/**
	 * Constructeur de base.
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 */
	public Creature1(int santeMax, int nbPiecesDOr)
	{
		this(0, 0, santeMax, nbPiecesDOr);
	}
	
	/**
	 * Constructeur avec position initiale.
	 * 
	 * @param x la position sur l'axe X de la creature
	 * @param y la position sur l'axe Y de la creature
	 * @param santeMax la sante max de cette creature
	 * @param nbPiecesDOr le nombre de pieces d'or de cette creature
	 */
	public Creature1(int x, int y, int santeMax, int nbPiecesDOr)
	{
		super(x, y, 14, 14, santeMax,nbPiecesDOr,IMAGE);
	}

	/**
	 * permet de copier la creature
	 */
	public Creature copier()
	{
		return new Creature1(x,y,getSanteMax(),getNbPiecesDOr());
	}
}