import java.util.ArrayList;

public class Main
{	
	public static void main(String args[])
	{
		//creating a configuration instance
		m_config = new Configuration();

		//get the data from the file if exists
		m_config.readFromFile();

		//turn off the modification flag so we'll know if the user modified anything or not
		m_config.setIsModified(false);

		//get the data from the user
		ConfigFenetre confFen = new ConfigFenetre(m_config);

		//simple solution pour que le program attend la reponse de l'utilisateur
		while(!m_config.isSet());

		//check if the user modified anything
		if(m_config.isModified() && m_config.okToSave())
		{
			m_config.saveToFile();
		}


		
		//connects to the Mysql Server
		m_connect = new DBConnect(m_config);
		
		//fetches the data to the data array
		m_data = m_connect.fetchData();
		
		//fetches the data into the cities array
		m_villes = m_connect.fetchCities();
		
		if(m_data != null && m_villes != null)
		{
			//here we shall show our main window
			Fenetre window = new Fenetre(m_data,m_villes);
		}
		else
		{
			javax.swing.JOptionPane.showMessageDialog(null,"Error Occured, please re-open the application");
		}
	}

	private static DBConnect m_connect;
	private static Configuration m_config;
	private static DataHolder[] m_data;
	private static City[] m_villes;
}