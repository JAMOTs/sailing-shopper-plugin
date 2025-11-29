package net.runelite.client.plugins.shoppinglist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ShoppingPanel extends PluginPanel
{
	private final JPanel listContainer = new JPanel();

	public ShoppingPanel()
	{
		setLayout(new BorderLayout());
		setBackground(ColorScheme.DARK_GRAY_COLOR);

		listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
		listContainer.setBackground(ColorScheme.DARK_GRAY_COLOR);

		// Title
		JLabel title = new JLabel("Shopping List");
		title.setForeground(Color.WHITE);
		title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		add(title, BorderLayout.NORTH);
		add(listContainer, BorderLayout.CENTER);
	}

	public void addItem(String itemName, int quantity)
	{
		SwingUtilities.invokeLater(() ->
		{
			JPanel itemPanel = new JPanel();
			itemPanel.setLayout(new BorderLayout());
			itemPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			itemPanel.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 40));
			itemPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

			JLabel label = new JLabel(itemName + " (" + quantity + ")");
			label.setForeground(Color.ORANGE);

			// Delete button
			JButton deleteBtn = new JButton("X");
			deleteBtn.setPreferredSize(new Dimension(40, 20));
			deleteBtn.addActionListener(e ->
			{
				listContainer.remove(itemPanel);
				listContainer.revalidate();
				listContainer.repaint();
			});

			itemPanel.add(label, BorderLayout.CENTER);
			itemPanel.add(deleteBtn, BorderLayout.EAST);

			listContainer.add(itemPanel);
			listContainer.revalidate();
			listContainer.repaint();
		});
	}
}
