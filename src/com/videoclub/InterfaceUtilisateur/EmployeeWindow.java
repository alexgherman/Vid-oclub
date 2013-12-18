package com.videoclub.InterfaceUtilisateur;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import com.videoclub.models.movie.Movie;

@SuppressWarnings("serial")
/**
 * Fen�tre de l'employ�/client
 * Construit un Cart
 * 
 * @author Maxime Dupuis
 *
 */
public class EmployeeWindow extends JDialog
{
	private VideoClub videoClub;

	JPanel panel;

	private JButton pickMoviesButton = new JButton("Choisir Films");
	private JButton pickItemsButton = new JButton("Choisir Articles");

	JPanel cartPanel = new JPanel();
	private Cart cart;

	private JButton payButton = new JButton("Payer");
	private JButton emptyCartButton = new JButton("Effacer tout");

	/**
	 * Constructeur
	 */
	EmployeeWindow(final VideoClub videoClub)
	{
		super((Frame) null, "Mode Employ�", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Cart panel
		cart = new Cart(videoClub);
		this.videoClub = videoClub;

		// Screen Size
		setSize(300, 500);
		setMinimumSize(new Dimension(200, 200));

		// Screen centered
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);

		// Window Layout
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.weightx = 1;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(pickMoviesButton, c);

		c.gridx = 1;
		panel.add(pickItemsButton, c);

		updateCartPanel();

		c.gridx = 0;
		c.gridy = 2;
		panel.add(payButton, c);

		c.gridx = 1;
		panel.add(emptyCartButton, c);

		getContentPane().add(panel);

		pickMoviesButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					PickMoviesWindow win = new PickMoviesWindow(videoClub);
					win.setVisible(true);

					ArrayList<Movie> selectedMovies = win.getSelection();

					if (!selectedMovies.isEmpty())
					{
						cart.setMovies(selectedMovies);
						updateCartPanel();
					}
				}
			});

		pickItemsButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{

					PickItemsWindow win = new PickItemsWindow(videoClub);
					win.setVisible(true);

					ArrayList<SellableItem> selectedItems = win.getSelection();

					if (!selectedItems.isEmpty())
					{
						cart.setItems(selectedItems);
						updateCartPanel();
					}
				}
			});

		payButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					// If a customer rents a movie he needs to login
					LoginInfo movieRentingCustomer = null;
					if (!cart.getMovies().isEmpty())
					{
						LoginWindow win = new LoginWindow();
						win.setVisible(true);

						movieRentingCustomer = win.getLoginInfo();
						if (!videoClub.validUser(movieRentingCustomer)) // Login
																		// failed,
																		// wanna
																		// add a
																		// member?
						{
							int answer = JOptionPane.showConfirmDialog(null, "D�sirez-vous ajouter un membre au syst�me?", "�chec d'identification!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

							if (answer == JOptionPane.YES_OPTION) // Yes, add a
																	// member!
							{
								CreateUserWindow createUserWin = new CreateUserWindow();
								createUserWin.setVisible(true);

								LoginInfo info = createUserWin.getLoginInfo();
								if (info != null) // Here's a new member!
								{
									if (!videoClub.createUser(info)) //CreateUser failed (maybe a user with the same name exists)
									{
										return;
									}
									else
									{
										movieRentingCustomer = info;
									}
								}
								else
								// Changed my mind, abort!

								{
									return;
								}
							}
							else
							// No, abort!
							{
								return;
							}
						}
					}

					// Customer doesn't want to rent a movie OR he's
					// successfully logged in

					// Ask for a confirmation before doing the transaction
					int confirmation = JOptionPane.showConfirmDialog(null, "Veuillez confirmer la transaction de: " + ((float) cart.getTotal() / 100) + " $", "Confirmation", JOptionPane.OK_CANCEL_OPTION);

					if (confirmation == JOptionPane.OK_OPTION)
					{
						videoClub.buyItems(cart.getItems());
						videoClub.rentMovies(cart.getMovies(), movieRentingCustomer);
					}
				}
			});

		emptyCartButton.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					cart.clear();
					updateCartPanel();
				}
			});
	}

	private void updateCartPanel()
	{
		cartPanel.removeAll();
		cartPanel = cartPanel(cart, videoClub);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weighty = 1;
		c.weightx = 1;

		panel.add(cartPanel, c);

		panel.revalidate();
	}

	private JPanel cartPanel(Cart cart, VideoClub videoClub)
	{
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

		JScrollPane scrollPane = new JScrollPane(listPanel);

		for (Movie movie : cart.getMovies())
		{
			String thing = "Location: " + movie.getDescription().getTitle();
			String price = ((float) videoClub.getMoviePrice(movie) / 100) + " $";

			JPanel linePanel = new JPanel();
			linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));

			linePanel.add(new JLabel(thing));
			linePanel.add(Box.createHorizontalGlue());
			linePanel.add(new JLabel(price));

			listPanel.add(linePanel);
		}

		for (SellableItem item : cart.getItems())
		{
			String thing = "Achat: " + item.getName();
			String price = ((float) item.getPrice() / 100) + " $";

			JPanel linePanel = new JPanel();
			linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));

			linePanel.add(new JLabel(thing));
			linePanel.add(Box.createHorizontalGlue());
			linePanel.add(new JLabel(price));

			listPanel.add(linePanel);
		}

		listPanel.add(new JSeparator());

		String price = ((float) cart.getTotal() / 100) + " $";

		JPanel linePanel = new JPanel();
		linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));

		linePanel.add(new JLabel("Total"));
		linePanel.add(Box.createHorizontalGlue());
		linePanel.add(new JLabel(price));

		listPanel.add(linePanel);

		// Put everything in a single JPanel that will be returned
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		panel.add(new JLabel("Panier"), c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.weighty = 1;
		c.weightx = 1;
		panel.add(scrollPane, c);

		return panel;
	}
}
