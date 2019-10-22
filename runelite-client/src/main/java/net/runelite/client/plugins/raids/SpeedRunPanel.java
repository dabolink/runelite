package net.runelite.client.plugins.raids;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.shadowlabel.JShadowedLabel;
import net.runelite.client.util.AsyncBufferedImage;

class SpeedRunPanel extends JPanel
{
	private String name;
	private AsyncBufferedImage image;

	SpeedRunPanel(RaidRoom.Boss boss, ItemManager itemManager)
	{
		this.name = boss.getName();
		this.image = itemManager.getImage(boss.getItemID());
		build();
	}

	SpeedRunPanel(String name, int itemID, ItemManager itemManager)
	{
		this.name = name;
		this.image = itemManager.getImage(itemID);
		build();
	}

	SpeedRunPanel(RaidRoom.Puzzle puzzle, ItemManager itemManager)
	{
		this.name = puzzle.getName();
		this.image = itemManager.getImage(puzzle.getItemID());
		build();
	}

	private void build()
	{
		final JPanel container = new JPanel();
		container.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		add(container);

		final JLabel label = new JShadowedLabel(name);
		image.addTo(label);

		label.setFont(FontManager.getRunescapeSmallFont());
		label.setForeground(Color.WHITE);

		container.add(label, BorderLayout.EAST);

		final JLabel time = new JLabel("--:--:--");
		container.add(time, BorderLayout.WEST);
	}
}
