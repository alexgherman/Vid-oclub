
/**
 * Dummy class
 * @author Maxime Dupuis
 *
 */
public class LocationFilm
{
	String nom;
	boolean nouveaut�;
	
	public LocationFilm(String nom, boolean nouveaut�)
	{
		this.nom = nom;
		this.nouveaut� = nouveaut�;
	}
	
	public String toString()
	{
		String str = "(" + nom + ": ";
		
		if(nouveaut�)
			str += "Nouveaut�";
		else
			str += "R�gulier";
		
		str += ")";
		
		
		return str;
	}
}
