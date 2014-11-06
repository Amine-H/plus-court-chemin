
public class City
{
	public City(int id,String nom,int x,int y)
	{
		m_id = id;
		m_nom = nom;
		m_x = x;
		m_y = y;
		m_graphicEtat = 0;
	}

	//just in case we ever need such thing
	public static String getNomById(City[] villes,int id)
	{
		if(villes[id].getId() == id)
			return villes[id].getNom();
		else
		{
			for(int i = 0;i < villes.length;i++)
			{
				if(villes[i].getId() == id)
				{
					return villes[i].getNom();
				}
			}
		}
		return "CityNotFound";
	}

	public int getId()
	{
		return m_id;
	}

	public String getNom()
	{
		return m_nom;
	}

	public int getX()
	{
		return m_x;
	}

	public int getY()
	{
		return m_y;
	}

	public int getGEtat()
	{
		return m_graphicEtat;
	}

	public void setGEtat(int etat)
	{
		m_graphicEtat = etat;
	}

	private int m_id;
	private String m_nom;
	private int m_x;
	private int m_y;
	private int m_graphicEtat;
	/**    m_graphicEtat
	 * 0 : rouge; default
	 * 1 : rouge foncée; hover
	 * 2 : vert; selected/in path
	 **/
}
