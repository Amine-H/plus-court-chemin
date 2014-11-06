import java.io.*;

public class Configuration
{
	private String m_server;
	private String m_port;
	private String m_dbName;
	private String m_user;
	private String m_pwd;
	private final String m_fileName = "config.conf";
	private boolean m_isSet;
	private boolean isModified;
	private boolean okToSave;
	
	public Configuration()
	{
		m_server = new String("localhost");
		m_port = new String("3306");
		m_dbName = new String("pfe_db");
		m_user = new String("root");
		m_pwd = new String("");
		m_isSet = false;
		isModified = false;
		okToSave = false;
	}

	public void setIsModified(boolean b)
	{
		isModified = b;
	}

	public boolean isModified()
	{
		return isModified;
	}

	public void saveToFile()
	{
		try
		{
			PrintWriter output = new PrintWriter(new FileWriter(m_fileName));

			output.println("Server = " + m_server);
			output.println("Port = " + m_port);
			output.println("DB_Name = " + m_dbName);
			output.println("Username = " + m_user);
			output.println("Password = " + m_pwd);

			output.flush();

			output.close();
		}
		catch (IOException e){e.printStackTrace();}//s'il ne peut pas ecrire .. pas de grand problem
	}

	public boolean readFromFile()
	{
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(m_fileName));
			String line = null;

			do
			{
				line = input.readLine();
				if(line != null)
				{
					String[] tabLine = line.split(" ");//separateur = espace
					if(line.startsWith("Server"))
					{
						if(tabLine.length > 2)
						{
							m_server = tabLine[tabLine.length - 1];
						}
						else
						{
							m_server = "";
						}
					}
					else if(line.startsWith("Port"))
					{
						if(tabLine.length > 2)
						{
							m_port = tabLine[tabLine.length - 1];
						}
						else
						{
							m_port = "";
						}
					}
					else if(line.startsWith("DB_Name"))
					{
						if(tabLine.length > 2)
						{
							m_dbName = tabLine[tabLine.length - 1];
						}
						else
						{
							m_dbName = "";
						}
					}
					else if(line.startsWith("Username"))
					{
						if(tabLine.length > 2)
						{
							m_user = tabLine[tabLine.length - 1];
						}
						else
						{
							m_user = "";
						}
					}
					else if(line.startsWith("Password"))
					{
						if(tabLine.length > 2)
						{
							m_pwd = tabLine[tabLine.length - 1];
						}
						else
						{
							m_pwd = "";
						}
					}
				}
			}
			while(line != null);
		}
		catch (Exception e){return false;}//s'il ne peut pas lire .. pas de grand problem
		return true;
	}

	public void setServer(String server)
	{
		m_server = server;
		isModified = true;
	}

	public void setPort(String port)
	{
		m_port = port;
		isModified = true;
	}

	public void setdbName(String dbName)
	{
		m_dbName = dbName;
		isModified = true;
	}

	public void setUser(String user)
	{
		m_user = user;
		isModified = true;
	}

	public void setPwd(String pwd)
	{
		m_pwd = pwd;
		isModified = true;
	}

	public String server()
	{
		return m_server;
	}

	public String port()
	{
		return m_port;
	}

	public String dbName()
	{
		return m_dbName;
	}

	public String user()
	{
		return m_user;
	}
	
	public String pwd()
	{
		return m_pwd;
	}
	
	public boolean isSet()
	{
		return m_isSet;
	}
	
	public void toggleSet()
	{
		m_isSet = true;
	}

	public void Save(boolean b)
	{
		okToSave = b;
	}

	public boolean okToSave()
	{
		return okToSave;
	}
}
