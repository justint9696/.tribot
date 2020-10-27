package scripts.Agility;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import org.tribot.api.Timing;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;

import scripts.Agility.courses.Course;
import scripts.Agility.utils.Node;

@ScriptManifest(authors = { "justint9696" }, category = "Agility", name = "Agility Dev")
public class Agility extends Script implements Painting {

	private final long XPStart = Skills.getXP(SKILLS.AGILITY);
	private final int LevelStart = Skills.getActualLevel(SKILLS.AGILITY);

	@Override
	public void run() {
		final List<Node> nodes = Course.DRAYNOR.getNodeSet();
		do {
			for (Node node : nodes) {
				if (node.validate()) {
					node.execute();
				}
			}
			sleep(100);
		} while (true);
	}

	@Override
	public void onPaint(Graphics g) {
		double multiplier = getRunningTime() / 3600000.0D;
		long XPCurrent = Skills.getXP(SKILLS.AGILITY);
		long XPGained = (XPCurrent - XPStart);
		long XPPerHour = (long) (XPGained / multiplier);
		int LevelCurrent = Skills.getActualLevel(SKILLS.AGILITY);
		int LevelGains = (LevelCurrent - LevelStart);
		int XPTNL = Skills.getXPToLevel(SKILLS.AGILITY, LevelCurrent + 1);
		double TTNL;
		try {
			TTNL = (3600000.0D * XPTNL / XPPerHour);
		} catch (ArithmeticException e) {
			TTNL = 0.0D;
		}
		g.setColor(Color.WHITE);
		g.drawString("Agility Dev Version", 315, 270);
		g.drawString("Runtime: " + Timing.msToString(getRunningTime()), 315, 285);
		g.drawString("Experience: " + XPGained + " (" + XPPerHour + "/hr)", 315, 300);
		g.drawString("Current Level: " + LevelCurrent + " (" + LevelGains + ")", 315, 315);
		g.drawString("Time Until Level: " + Timing.msToString(Math.round(TTNL)), 315, 330);
	}

}
