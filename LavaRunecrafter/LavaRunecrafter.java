package scripts.LavaRunecrafter;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.nodes.CastleWars;
import scripts.LavaRunecrafter.nodes.CloseBank;
import scripts.LavaRunecrafter.nodes.CraftRunes;
import scripts.LavaRunecrafter.nodes.Deposit;
import scripts.LavaRunecrafter.nodes.DuelArena;
import scripts.LavaRunecrafter.nodes.EmptyPouches;
import scripts.LavaRunecrafter.nodes.EnterRuins;
import scripts.LavaRunecrafter.nodes.EquipNecklace;
import scripts.LavaRunecrafter.nodes.EquipRing;
import scripts.LavaRunecrafter.nodes.FillPouches;
import scripts.LavaRunecrafter.nodes.MagicImbue;
import scripts.LavaRunecrafter.nodes.OpenBank;
import scripts.LavaRunecrafter.nodes.WalkToAltar;
import scripts.LavaRunecrafter.nodes.WalkToRuins;
import scripts.LavaRunecrafter.nodes.WithdrawEssence;
import scripts.LavaRunecrafter.nodes.WithdrawNecklace;
import scripts.LavaRunecrafter.nodes.WithdrawRing;
import scripts.LavaRunecrafter.nodes.WithdrawRunes;
import scripts.LavaRunecrafter.utils.Node;

@ScriptManifest(authors = { "justint9696" }, category = "Runecrafting", name = "Lava Runecrafter")
public class LavaRunecrafter extends Script implements Painting, MessageListening07 {
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private String currentTask;
	private final long StartXP = Skills.getXP(SKILLS.RUNECRAFTING);

	@Override
	public void run() {
		nodes.add(new OpenBank());
		nodes.add(new Deposit());
		nodes.add(new WithdrawRunes());
		nodes.add(new WithdrawRing());
		nodes.add(new WithdrawNecklace());
		nodes.add(new WithdrawEssence());
		nodes.add(new CloseBank());
		nodes.add(new EquipRing());
		nodes.add(new EquipNecklace());
		nodes.add(new FillPouches());
		nodes.add(new DuelArena());
		nodes.add(new WalkToRuins());
		nodes.add(new EnterRuins());
		nodes.add(new WalkToAltar());
		nodes.add(new MagicImbue());
		nodes.add(new CraftRunes());
		nodes.add(new EmptyPouches());
		nodes.add(new CastleWars());
		do {
			for (Node node : nodes) {
				if (node.validate()) {
					currentTask = node.display();
					node.execute();
				}
			}
			sleep(100);
		} while (Variables.isRunning());
	}

	@Override
	public void onPaint(Graphics g) {
		double multiplier = getRunningTime() / 3600000.0D;
		long GainedXP = Skills.getXP(SKILLS.RUNECRAFTING) - StartXP;
		long XPPerHour = (long) (GainedXP / multiplier);
		
		g.setColor(Color.WHITE);
		g.drawString("Runtime: " + Timing.msToString(getRunningTime()), 400, 300);
		g.drawString("Experience: " + GainedXP + "(" + XPPerHour + ")", 400, 315);
		g.drawString("Current Task: " + currentTask, 400, 330);
	}
	
	@Override
	public void serverMessageReceived(String msg) {
		if (msg.contains("Your Magic Imbue charge has ended.") ^ msg.contains("You need an earth talisman to bind lava runes."))
			Variables.imbueInactive();
	}
}
