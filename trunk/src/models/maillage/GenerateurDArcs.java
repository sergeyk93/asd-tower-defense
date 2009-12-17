package models.maillage;

import org.jgrapht.*;

/**
 * Fichier : GenerateurDArcs.java
 * 
 * <p>
 * But : Permet de générer les arcs du graphique à partir de deux noeuds.
 * <p>
 * Remarques : -
 * 
 * @author Pierre-Dominique Putallaz
 * @author Aurélien Da Campo
 * @author Lazhar Farjallah
 * @version 5 déc. 2009
 * @since jdk1.6.0_16
 */
public class GenerateurDArcs implements EdgeFactory<Noeud, Arc>
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jgrapht.EdgeFactory#createEdge(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public Arc createEdge(Noeud sourceVertex, Noeud targetVertex)
	{
		return new Arc(sourceVertex, targetVertex);
	}

}