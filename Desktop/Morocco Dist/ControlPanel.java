import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

public class ControlPanel extends JFrame implements ActionListener
{
	public ControlPanel(Fenetre parent)
	{
		setTitle("Panneau de Control");
		setSize(150,250);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		m_parent = parent;
		m_conteneur = new JPanel();
		GridLayout gLayout = new GridLayout(10,1);//10 lignes 1 collone
		m_conteneur.setLayout(gLayout);


		m_supprimerChemain = new JButton("Effacer le Chemin");
		m_modifier = new JButton("Modifier");


		m_luniteDist = new JLabel("Unité de distance");
		String[] uniteDist = {"Kilometre","Metre","Miles"};
		m_uniteDist = new JComboBox(uniteDist);
		m_luniteDist.setLabelFor(m_uniteDist);


		m_lcarb = new JLabel("Carburant");
		String[] carburant = {"Essence","Mazout"};
		m_carb = new JComboBox(carburant);
		m_lcarb.setLabelFor(m_carb);


		m_tprixCarb = new JTextField();
		m_tprixCarb.setText("" + 12.24f);
		m_lprixCarb = new JLabel("Prix carburant (1 litres)");
		m_lprixCarb.setLabelFor(m_tprixCarb);
		
		m_tConsomCarb = new JTextField();
		m_tConsomCarb.setText("" + 0.74f);
		m_lConsomCarb = new JLabel("Consomation Carburant");


		m_uniteDist.setSelectedIndex(m_parent.getGraph().getUDistance());
		m_carb.setSelectedIndex(m_parent.getGraph().getCarb());


		m_conteneur.add(m_supprimerChemain);
		m_conteneur.add(m_luniteDist);
		m_conteneur.add(m_uniteDist);
		m_conteneur.add(m_lcarb);
		m_conteneur.add(m_carb);
		m_conteneur.add(m_lprixCarb);
		m_conteneur.add(m_tprixCarb);
		m_conteneur.add(m_lConsomCarb);
		m_conteneur.add(m_tConsomCarb);
		m_conteneur.add(m_modifier);
		getContentPane().add(m_conteneur);
		

		m_supprimerChemain.addActionListener(this);
		m_modifier.addActionListener(this);


		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				m_parent.flagCPanelClosed();
			}
		});

		setVisible(true);
	}
	public void actionPerformed(ActionEvent aEvnt)
	{
		if(aEvnt.getSource() == m_supprimerChemain)
		{
			m_parent.getGraph().clearPath();
		}
		else if(aEvnt.getSource() == m_modifier)
		{
			m_parent.getGraph().setUDistance(m_uniteDist.getSelectedIndex());
			m_parent.getGraph().setCarb(m_carb.getSelectedIndex());
			m_parent.getGraph().setPrixCarb(Float.parseFloat(m_tprixCarb.getText()));
			m_parent.getGraph().setConsomationCarb(Float.parseFloat(m_tConsomCarb.getText()));
			m_parent.getGraph().repaint();
		}
	}

	private Fenetre m_parent;
	private JPanel m_conteneur;
	private JLabel m_luniteDist;
	private JLabel m_lcarb;
	private JLabel m_lprixCarb;
	private JComboBox m_uniteDist;
	private JComboBox m_carb;
	private JTextField m_tprixCarb;
	private JButton m_supprimerChemain;
	private JButton m_modifier;
	private JTextField m_tConsomCarb;
	private JLabel m_lConsomCarb;
}