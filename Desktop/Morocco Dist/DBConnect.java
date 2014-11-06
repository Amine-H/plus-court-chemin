import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

class DBConnect
{
	public DBConnect(Configuration config)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			
			m_connection = DriverManager.getConnection("jdbc:mysql://" +
			config.server() + ":" + config.port() + "/"+ config.dbName(),config.user(),config.pwd());
			m_statement = m_connection.createStatement();
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null,"Exception thrown  : " + e); 
			System.exit(1);
		}
	}

	public DataHolder[] fetchData()
	{
		DataHolder[] data = null;
		try
		{
			m_resultSet = m_statement.executeQuery("select count(*) as nombre from distances");
			m_resultSet.next();
			data = new DataHolder[m_resultSet.getInt("nombre")];

			m_resultSet = m_statement.executeQuery("select * from distances");

			int i = 0;
			while(m_resultSet.next())
			{
				data[i] = new DataHolder();
				data[i].setCity1(m_resultSet.getInt(1) - 1);
				data[i].setCity2(m_resultSet.getInt(2) - 1);
				data[i].setDistance(m_resultSet.getFloat(3));
				i++;
			}
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null,"Exception thrown : " + e);
			System.exit(1);
		}

		return data;
	}

	public City[] fetchCities()
	{
		City[] villes = null;

		try
		{
			m_resultSet = m_statement.executeQuery("select count(*) as nombre from cities");
			m_resultSet.next();
			villes = new City[m_resultSet.getInt("nombre")];

			m_resultSet = m_statement.executeQuery("select * from cities");

			int i = 0;
			while(m_resultSet.next())
			{
				villes[i] = new City(m_resultSet.getInt(1) - 1,m_resultSet.getString(2),
						m_resultSet.getInt(3),m_resultSet.getInt(4));
				i++;
			}
		}
		catch(Exception e)
		{
			javax.swing.JOptionPane.showMessageDialog(null,"Exception thrown : " + e);
			System.exit(1);
		}

		return villes;
	}

	private Connection m_connection;
	private Statement m_statement;
	private ResultSet m_resultSet;
}