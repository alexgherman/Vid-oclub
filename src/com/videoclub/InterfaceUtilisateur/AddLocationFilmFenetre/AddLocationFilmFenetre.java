import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * Cr�e une fen�tre reli�e � un DataBase.
 * Permet d'ajouter des films � la DataBase si l'utilisateur le d�sire
 * @author Maxime Dupuis
 *
 */
@SuppressWarnings("serial")
public class AddLocationFilmFenetre extends JFrame
{
	public static void main(String[] args)
	{
		DataBase db = new DataBase();
		new AddLocationFilmFenetre(db);
	}
	
	DataBase db;
	
	JLabel nomLabel = new JLabel("Nom du film");
	JTextField nomTxtField = new JTextField();
	
	JLabel quantit�Label = new JLabel("Quantit�");
	JSpinner quantit�Spinner = new JSpinner();
	
	JLabel nouveaut�Label = new JLabel("Nouveaut�");
	JCheckBox nouveaut�CheckBox = new JCheckBox();

	JButton enregistrerBouton = new JButton("Enregistrer");
	JButton annulerBouton = new JButton("Annuler");
	
	
	
	
	AddLocationFilmFenetre(final DataBase db)
	{
		super("Ajouter Location");
		this.db = db;
		setSize(400, 250);
		setVisible(true);
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,10,10,10);
		
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		panel.add(nomLabel,c);
		
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 10;
		panel.add(nomTxtField,c);
		
		
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		panel.add(quantit�Label,c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 10;
		panel.add(quantit�Spinner,c);
		
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		panel.add(nouveaut�Label,c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = 10;
		panel.add(nouveaut�CheckBox,c);
		
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		panel.add(enregistrerBouton,c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = 0;
		panel.add(annulerBouton,c);
		
		getContentPane().add(panel);
		
		quantit�Spinner.setValue(1);
		
		enregistrerBouton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String nom = nomTxtField.getText();
				boolean nouveaut� = nouveaut�CheckBox.isSelected();
				LocationFilm film = new LocationFilm(nom, nouveaut�);
				
				for(int i=0; i < (Integer)quantit�Spinner.getValue(); ++i)
					db.addLocation(film);
			}
		});
		
		annulerBouton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				dispose();
			}
		});
	}
}
