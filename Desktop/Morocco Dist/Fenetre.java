import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Fenetre extends JFrame
{
	public Fenetre(DataHolder[] dataHolder,City[] villes)
	{
		setTitle("Calcule distances interville");
		setSize(700,700);
		//posing it at the middle of the screen
		Toolkit tk = getToolkit();
		Dimension dim = tk.getScreenSize();
		setLocation((int)dim.getWidth()/2 - 350,(int)dim.getHeight()/2 - 360);
		//fin posing it at the middle
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		m_ConteneurPrincipale = getContentPane();
		m_ConteneurPrincipale.setLayout(new BoxLayout(m_ConteneurPrincipale,BoxLayout.Y_AXIS));
		m_graphic = new Graphic(dataHolder,villes,this);
 

		m_ConteneurPrincipale.add(m_graphic);
		//m_ConteneurPrincipale.setPreferredSize(m_ConteneurPrincipale.getPreferredSize());

		m_cPanel = new ControlPanel(this);
		setVisible(true);
	}
	
	public Graphic getGraph()
	{
		return m_graphic;
	}

	public void flagCPanelClosed()
	{
		m_cPanel = null;
	}

	public void newCPanel()
	{
		if(m_cPanel == null)
		{
			m_cPanel = new ControlPanel(this);
		}
	}

	private Graphic m_graphic;
	private Container m_ConteneurPrincipale;
	private ControlPanel m_cPanel;
}

class Graphic extends JComponent implements MouseMotionListener,MouseListener
{
	public Graphic(DataHolder[] dataHolder,City[] villes,Fenetre parent)
	{
		m_nomCarte = "./imgs/carte.png";
		m_parent = parent;
		m_carte = Toolkit.getDefaultToolkit().getImage(m_nomCarte);
		m_settings = Toolkit.getDefaultToolkit().getImage("./imgs/settings.png");
		m_dataHolder = dataHolder;
		m_villes = villes;
		m_gVilles = new Shape[m_villes.length];
		m_colors = new Color[3];
		m_rectBackg = new Color(255,255,175,155);
		m_txtColor = new Color(0,0,0,255);
		m_from = -1;//initialled to -1 which means we don't have a start YET
		m_to = -1;
		m_showInfoRect = false;
		m_resultRectMoving = false;
		x_Base = 420;
		y_Base = 460;
		for(int i = 0;i < m_gVilles.length;i++)
		{
			m_gVilles[i] = new Ellipse2D.Double(m_villes[i].getX() - 5,m_villes[i].getY() - 5,10,10);
		}
		m_colors[0] = new Color(125,0,0,255);
		m_colors[1] = new Color(255,0,0,255);
		m_colors[2] = new Color(0,255,0,255);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	
	public void clearPath()
	{
		//no start no end
		m_from = -1;
		m_to = -1;
		//reset result stuff
		m_distance = 0;
		m_prixCarb = 0;
		m_prixTotal = 0;
		m_carburant = 0;
		//clear path (resetting to default status)
		setCitiesToDefault();
		m_path = null;
		//repainting
		repaint();
	}

	public void paintCarte(Graphics2D g2d)
	{
		g2d.drawImage(m_carte,0,0,700,700,this);
		g2d.drawImage(m_settings,0,0,50,50,this);
	}

	public void setCitiesToDefault()
	{
		for(int i = 0;i < m_villes.length;i++)
		{
			m_villes[i].setGEtat(0);
		}
	}

	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		//affiche la carte
		paintCarte(g2d);
		//affiche le "path"..(chemin)
		paintPath(g2d);
		//affiche les villes
		paintCities(g2d);
		//affiche les distances../carburant consumée..
		paintResults(g2d);
		//affiche le rectange d'information (au moment du hover)
		paintInformationRect(g2d);
	}

	public void paintInformationRect(Graphics2D g2d)
	{
		if(m_showInfoRect)
		{
			int rectX,rectY,strX,strY;
			//it will be about 200pixels wide/50pixels long so we need to check if we have such space
			if(m_villes[m_villeId].getX() + 200 < 700 && m_villes[m_villeId].getY() + 50 < 700)
			{
				rectX = m_villes[m_villeId].getX();
				rectY = m_villes[m_villeId].getY();
			}
			else
			{
				rectX = m_villes[m_villeId].getX();
				rectY = m_villes[m_villeId].getY();
				if(m_villes[m_villeId].getX() + 200 > 700)
				{
					rectX -= 220;
				}
				if(m_villes[m_villeId].getY() + 50 > 700)
				{
					rectY -= 70;
				}
			}
		rectX += 10;
		rectY += 10;
		FontMetrics fm = g2d.getFontMetrics();
		//un peut des testes pour que le nom soit au milieu au niveau du Y
		strY = rectY + 30;
		//une formule magique pour centre le texte sur un rect de longeur de 200px
		//2X + W = 200 dont X = le decallement sur les x,W la taille du text
		//donc X = (200 - W)/2
		strX = rectX + (200 - fm.stringWidth(m_villes[m_villeId].getNom()))/2;
		//un peut de looking est toujours le bienvenu
		g2d.setColor(m_rectBackg);
		g2d.fillRoundRect(rectX,rectY,200,50,20,20);
		g2d.setColor(m_txtColor);
		g2d.drawRoundRect(rectX,rectY,200,50,20,20);
		g2d.setColor(m_txtColor);
		g2d.drawString(m_villes[m_villeId].getNom(),strX,strY);
		}
	}

	public void paintResults(Graphics2D g2d)
	{
		int y_offset;
		int x_offset = 10;

		//drawing resultss rect
		g2d.setColor(m_rectBackg);
		g2d.fillRect(x_Base,y_Base,260,200);
		g2d.setColor(m_txtColor);
		g2d.drawRect(x_Base,y_Base,260,200);
		//FIN drawing results rect

		//drawing results
		g2d.setColor(m_txtColor);
		y_offset = 20;
		
		String[] distU = {"Kilometre","Metre","Miles"};
		g2d.drawString("Distance : " + m_distance + " " + distU[m_uDistance],x_Base + x_offset,y_Base + y_offset);
		y_offset += 20;
		g2d.drawString("Carburant Consommée : " + m_carburant + " Litres",x_Base + x_offset,y_Base + y_offset);
		y_offset += 20;
		g2d.drawString("Prix Total : " + m_prixTotal,x_Base + x_offset,y_Base + y_offset);
		y_offset += 20;
		//a sotisficated method to paint path :D
		if(m_path != null)
		{
			g2d.drawString("Chemin : ",x_Base + x_offset,y_Base + y_offset);
			FontMetrics fm = g2d.getFontMetrics();
			x_offset += fm.stringWidth("Chemin : ");
			for(int i = 0;i < m_path.size();i++)
			{
				g2d.drawString(m_villes[m_path.get(i)].getNom(),x_Base + x_offset,y_Base + y_offset);
				if(fm.stringWidth(m_villes[m_path.get(i)].getNom()) + x_offset > 240)
				{
					x_offset = 10;
					y_offset += 20;
				}
				else
				{
					x_offset += fm.stringWidth(m_villes[m_path.get(i)].getNom() + " ");
				}
			}
		}
		//FIN drawgin results
	}

	public void paintPath(Graphics2D g2d)
	{
		if(m_from != -1 && m_to != -1)
		{
			float dists[] = {1,1000,1.609f};
			if(m_path == null || (m_path.get(0) == m_from && m_path.get(m_path.size() - 1) == m_to))
			{
				m_path = Calcule.findPath(m_from,m_to,m_dataHolder,null);
			}
			if(m_path == null)
			{
				clearPath();
				javax.swing.JOptionPane.showMessageDialog(null,"Villes non connecté!");
				return;
			}
			m_distance = Calcule.totalKm(m_dataHolder,m_path);
			m_carburant = m_distance*m_consomationCarb;//consomation par km
			m_distance *= dists[m_uDistance];
			m_prixTotal = m_prixCarb*m_carburant;
			if(m_path.size() > 1)
			{
				setCitiesToDefault();
				for(int i = 0;i < m_path.size() - 1;i++)
				{
					m_villes[m_path.get(i)].setGEtat(2);
					g2d.drawLine(m_villes[m_path.get(i)].getX(),m_villes[m_path.get(i)].getY(),
					    m_villes[m_path.get(i + 1)].getX(),m_villes[m_path.get(i + 1)].getY());
				}
				m_villes[m_path.get(m_path.size() - 1)].setGEtat(2);
			}
		}
	}

	public void paintCities(Graphics2D g2d)
	{
		for(int i = 0;i < m_gVilles.length;i++)
		{
			g2d.setColor(m_colors[m_villes[i].getGEtat()]);
			g2d.fill(m_gVilles[i]);
		}
	}

	public void mouseClicked(MouseEvent mEvnt)
	{
		if(mEvnt.getX() <= 50 && mEvnt.getY() <= 50)
		{
			if(m_cPanel == null)
			{
				m_parent.newCPanel();
			}
			return;
		}
		if((mEvnt.getX() > x_Base && mEvnt.getX() < x_Base + 260) && 
				(mEvnt.getY() > y_Base && mEvnt.getY() < y_Base + 200))
		{
			if(m_resultRectMoving)
			{
				m_resultRectMoving = false;
			}
			else
			{
				m_resultRectMoving = true;
				m_mouseTmpX = mEvnt.getX();
				m_mouseTmpY = mEvnt.getY();
			}
		}
		if(m_from == -1)
		{
			for(int i = 0;i < m_gVilles.length;i++)
			{
				if(m_gVilles[i].contains(mEvnt.getPoint()))
				{
					m_from = i;
					m_villes[i].setGEtat(2);
					repaint();
					break;
				}
			}
		}
		else
		{
			for(int i = 0;i < m_gVilles.length;i++)
			{
				if(m_gVilles[i].contains(mEvnt.getPoint()))
				{
					if(m_from != i)
					{
						m_to = i;
					}
					repaint();
					return;
				}
			}
		}
	}
	
	public void mouseMoved(MouseEvent mEvnt)
	{
		//clear hover state from a city if it has it and it has no hover anymore
		for(int i = 0;i < m_gVilles.length;i++)
		{

			if(!m_gVilles[i].contains(mEvnt.getPoint()))
			{
				if(m_villes[i].getGEtat() == 1)
				{
					m_villes[i].setGEtat(0);
				}
				m_showInfoRect = false;
				repaint();
			}

		}

		//check if it's on a city
		for(int i = 0;i < m_gVilles.length;i++)
		{
			if(m_gVilles[i].contains(mEvnt.getPoint()))
			{
				if(m_villes[i].getGEtat() != 2)
				{
					m_villes[i].setGEtat(1);		
				}
				m_villeId = i;
				m_showInfoRect = true;
				repaint();
				return;
			}
		}

		//check if the user is dragging the mouse
		if(m_resultRectMoving)
		{
			x_Base += mEvnt.getX() - m_mouseTmpX;
			y_Base += mEvnt.getY() - m_mouseTmpY;
			m_mouseTmpX = mEvnt.getX();
			m_mouseTmpY = mEvnt.getY();
			repaint();
		}
	}
	//accesseurs et manupilateurs pour ControlPanel
	public void setUDistance(int ud)
	{
		m_uDistance = ud;
	}
	public void setCarb(int carb)
	{
		m_carb = carb;
	}
	public void setPrixCarb(float pCarb)
	{
		m_prixCarb = pCarb;
	}
	public void setConsomationCarb(float cPKm)
	{
		m_consomationCarb = cPKm;
	}

	public float getConsomationCarb()
	{
		return m_consomationCarb;
	}
	public int getUDistance()
	{
		return m_uDistance;
	}
	public int getCarb()
	{
		return m_carb;
	}
	public float getPrixCarb()
	{
		return m_prixCarb;
	}
	//FIN accesseurs/manupilateurs
	
	ControlPanel m_cPanel;
	//information rectangle vars
	private boolean m_showInfoRect;
	private int m_villeId;
	Color m_rectBackg;
	Color m_txtColor;
	//end information rectangle vars
	private ArrayList<Integer> m_path;
	private Image m_carte;
	private Image m_settings;
	private String m_nomCarte;
	private DataHolder[] m_dataHolder;
	private City[] m_villes;
	private Shape m_gVilles[];
	private Color m_colors[];
	private int m_from;
	private int m_to;
	private int m_uDistance;
	private int m_carb;
	private float m_prixCarb;
	//result vars
	private boolean m_resultRectMoving;
	private int m_mouseTmpX;
	private int m_mouseTmpY;
	private float m_carburant;
	private float m_consomationCarb;
	private float m_distance;
	private float m_prixTotal;
	//results rect cordinals
	private int x_Base;
	private int y_Base;
	//FIN result vars
	private Fenetre m_parent;
	public void mouseReleased(MouseEvent mEvnt){}
	public void mouseEntered(MouseEvent mEvnt){}
	public void mouseExited(MouseEvent mEvnt){}
	public void mousePressed(MouseEvent mEvnt){}
	public void mouseDragged(MouseEvent mEvnt){}
}