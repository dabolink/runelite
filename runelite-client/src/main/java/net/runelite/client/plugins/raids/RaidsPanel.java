/*
 * Copyright (c) 2018, Psikoi <https://github.com/psikoi>
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.raids;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.GUARDIANS;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.MUTTADILES;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.MYSTICS;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.SHAMANS;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.TEKTON;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.VANGUARDS;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.VASA;
import static net.runelite.client.plugins.raids.RaidRoom.Boss.VESPULA;
import static net.runelite.client.plugins.raids.RaidRoom.Puzzle.CRABS;
import static net.runelite.client.plugins.raids.RaidRoom.Puzzle.ICE_DEMON;
import static net.runelite.client.plugins.raids.RaidRoom.Puzzle.THIEVING;
import static net.runelite.client.plugins.raids.RaidRoom.Puzzle.TIGHTROPE;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.StackFormatter;

class RaidsPanel extends PluginPanel
{
	private static final String HTML_LABEL_TEMPLATE =
		"<html><body style='color:%s'>%s<span style='color:white'>%s</span></body></html>";

	// When there is no loot, display this
	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	// Handle loot boxes
	private final JPanel timeContainer = new JPanel();

	// Handle overall session data
	private final JPanel overallTime = new JPanel();
	private final JLabel overallBestSegments = new JLabel();
	private final JLabel overallBestPoints = new JLabel();

	private final RaidsPlugin plugin;
	private final RaidsTimer timer;
	private final RaidsConfig config;
	private ItemManager itemManager;

	RaidsPanel(final RaidsPlugin plugin, ItemManager itemManager, RaidsTimer timer, final RaidsConfig config)
	{
		this.plugin = plugin;
		this.itemManager = itemManager;
		this.config = config;
		this.timer = timer;

		setBorder(new EmptyBorder(6, 6, 6, 6));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		// Create layout panel for wrapping
		final JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		add(layoutPanel, BorderLayout.NORTH);

		final JPanel tektonPanel = new SpeedRunPanel(TEKTON, itemManager);
		layoutPanel.add(tektonPanel, BorderLayout.NORTH);
		final JPanel crabsPanel = new SpeedRunPanel(CRABS, itemManager);
		layoutPanel.add(crabsPanel, BorderLayout.NORTH);
		final JPanel iceDemonPanel = new SpeedRunPanel(ICE_DEMON, itemManager);
		layoutPanel.add(iceDemonPanel, BorderLayout.NORTH);
		final JPanel shamanPanel = new SpeedRunPanel(SHAMANS, itemManager);
		layoutPanel.add(shamanPanel, BorderLayout.NORTH);
		final JPanel down1Panel = new SpeedRunPanel("Floor 1", ItemID.LADDER_TOP, itemManager);
		layoutPanel.add(down1Panel, BorderLayout.NORTH);

		final JPanel vanguardsPanel = new SpeedRunPanel(VANGUARDS, itemManager);
		layoutPanel.add(vanguardsPanel, BorderLayout.NORTH);
		final JPanel thievingPanel = new SpeedRunPanel(THIEVING, itemManager);
		layoutPanel.add(vanguardsPanel, BorderLayout.NORTH);
		final JPanel vespulaPanel = new SpeedRunPanel(VESPULA, itemManager);
		layoutPanel.add(vespulaPanel, BorderLayout.NORTH);
		final JPanel tightropePanel = new SpeedRunPanel(TIGHTROPE, itemManager);
		layoutPanel.add(tightropePanel, BorderLayout.NORTH);
		final JPanel down2Panel = new SpeedRunPanel("Floor 2", ItemID.LADDER_TOP, itemManager);
		layoutPanel.add(down2Panel, BorderLayout.NORTH);

		final JPanel guardiansPanel = new SpeedRunPanel(GUARDIANS, itemManager);
		layoutPanel.add(guardiansPanel, BorderLayout.NORTH);
		final JPanel vasaPanel = new SpeedRunPanel(VASA, itemManager);
		layoutPanel.add(vasaPanel, BorderLayout.NORTH);
		final JPanel mysticsPanel = new SpeedRunPanel(MYSTICS, itemManager);
		layoutPanel.add(mysticsPanel, BorderLayout.NORTH);
		final JPanel muttadilePanel = new SpeedRunPanel(MUTTADILES, itemManager);
		layoutPanel.add(muttadilePanel, BorderLayout.NORTH);
		final JPanel down3Panel = new SpeedRunPanel("Floor 3", ItemID.LADDER_TOP, itemManager);
		layoutPanel.add(down3Panel, BorderLayout.NORTH);

		final JPanel olmPanel = new SpeedRunPanel("Olm", ItemID.OLMLET, itemManager);
		layoutPanel.add(olmPanel, BorderLayout.NORTH);



		// Add error pane
		//errorPanel.setContent("Speed Run Raids Tracker", "You haven't started a raid yet.");
		//add(errorPanel);
	}

	/**
	 * Rebuilds all the boxes from scratch using existing listed records, depending on the grouping mode.
	 */
	private void rebuild()
	{
		/*
		logsContainer.removeAll();
		boxes.clear();
		int start = 0;
		if (!groupLoot && records.size() > MAX_LOOT_BOXES)
		{
			start = records.size() - MAX_LOOT_BOXES;
		}
		for (int i = start; i < records.size(); i++)
		{
			buildBox(records.get(i));
		}
		boxes.forEach(LootTrackerBox::rebuild);
		updateOverall();
		logsContainer.revalidate();
		logsContainer.repaint();
		 */
	}


	private static String htmlLabel(String key, long value)
	{
		final String valueStr = StackFormatter.quantityToStackSize(value);
		return String.format(HTML_LABEL_TEMPLATE, ColorUtil.toHexColor(ColorScheme.LIGHT_GRAY_COLOR), key, valueStr);
	}

	public void loadHeaderIcon(BufferedImage bufferedImage)
	{
		//overallIcon.setIcon(new ImageIcon(bufferedImage));
	}
}
