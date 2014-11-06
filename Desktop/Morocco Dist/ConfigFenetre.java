import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigFenetre extends JFrame implements ActionListener
{
	public ConfigFenetre(Configuration config)
	{
		setTitle("Configurations");
		setSize(250,250);
		//posing it at the middle of the screen
		Toolkit tk = getToolkit();
		Dimension dim = tk.getScreenSize();
		setLocationRelativeTo(null);
		//fin posing it at the middle
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_conteneur = new JPanel();
		GridLayout gLayout = new GridLayout(7,2);//6 lignes 2 collones
		m_conteneur.setLayout(gLayout);
		m_config = config;
		
		m_lserver = new JLabel("Serveur :");
		m_lport = new JLabel("Port :");
		m_ldbName = new JLabel("Nom de la BD :");
		m_luser = new JLabel("Utilisateur :");
		m_lpwd = new JLabel("Mot de Passe :");
		m_lrem = new JLabel("Memoriser :");
		m_buttonOk = new JButton("Confirmer");
		m_buttonCancel = new JButton("Annuler");

		m_lserver.setLabelFor(m_tfserver);
		m_lport.setLabelFor(m_tfport);
		m_ldbName.setLabelFor(m_tfdbName);
		m_luser.setLabelFor(m_tfuser);
		m_lpwd.setLabelFor(m_tfpwd);

		m_tfserver = new JTextField(m_config.server());
		m_tfport = new JTextField(m_config.port());
		m_tfdbName = new JTextField(m_config.dbName());
		m_tfuser = new JTextField(m_config.user());
		m_tfpwd = new JPasswordField(m_config.pwd());
		m_cBoxrem = new JCheckBox();

		m_buttonOk.addActionListener(this);
		m_buttonCancel.addActionListener(this);

		m_conteneur.add(m_lserver);
		m_conteneur.add(m_tfserver);
		m_conteneur.add(m_lport);
		m_conteneur.add(m_tfport);
		m_conteneur.add(m_ldbName);
		m_conteneur.add(m_tfdbName);
		m_conteneur.add(m_luser);
		m_conteneur.add(m_tfuser);
		m_conteneur.add(m_lpwd);
		m_conteneur.add(m_tfpwd);
		m_conteneur.add(m_lrem);
		m_conteneur.add(m_cBoxrem);
		m_conteneur.add(m_buttonOk);
		m_conteneur.add(m_buttonCancel);
		getContentPane().add(m_conteneur);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent aEvnt)
	{
		if(aEvnt.getSource() == m_buttonOk)
		{
			if(!m_config.server().equals(m_tfserver.getText()))
				{m_config.setServer(m_tfserver.getText());}
			if(!m_config.port().equals(m_tfport.getText()))
				{m_config.setPort(m_tfport.getText());}
			if(!m_config.dbName().equals(m_tfdbName.getText()))
				{m_config.setdbName(m_tfdbName.getText());}
			if(!m_config.user().equals(m_tfuser.getText()))
				{m_config.setUser(m_tfuser.getText());}
			if(!m_config.pwd().equals(m_tfpwd.getText()))
				{m_config.setPwd(m_tfpwd.getText());}
			if(m_cBoxrem.isSelected())
				{m_config.Save(true);}
			m_config.toggleSet();
			//i think this closes the window
			dispose();
		}
		else if(aEvnt.getSource() == m_buttonCancel)
		{
			System.exit(0);
		}
	}

	private JPanel m_conteneur;
	private Configuration m_config;
	private JTextField m_tfserver;
	private JTextField m_tfport;
	private JTextField m_tfdbName;
	private JTextField m_tfuser;
	private JPasswordField m_tfpwd;
	private JLabel m_lserver;
	private JLabel m_lport;
	private JLabel m_ldbName;
	private JLabel m_luser;
	private JLabel m_lpwd;
	private JLabel m_lrem;
	private JButton m_buttonOk;
	private JButton m_buttonCancel;
	private JCheckBox m_cBoxrem;
}