package com.videoclub.InterfaceUtilisateur;


/**
 * Dummy class
 * @author Maxime Dupuis
 *
 */
public class SellableItem
{
	private String name;
	private int price;	//Le prix est en cent(�) pour �tre plus pr�cis *ATTENTION*
	
	public SellableItem(String nom, int prix)
	{
		this.name = nom;
		this.price = prix;
	}
	
	public String toString()
	{
		String str = name + ": " + (float)price / 100 + "$";
		return str;
	}
}