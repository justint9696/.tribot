package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.LavaRunecrafter.utils.Node;

public class WalkToRuins implements Node {
	
	RSObject[] MysteriousRuins;
	final RSTile DuelArena = new RSTile(3315, 3235, 0);
	final RSTile[] PathToRuins = { new RSTile(3310, 3240), new RSTile(3310, 3245), new RSTile(3310, 3250) };

	@Override
	public boolean validate() {
		MysteriousRuins = Objects.find(30, 34817);
		return !Player.isMoving() && Player.getPosition().distanceTo(DuelArena) < 5 && MysteriousRuins.length > 0 && !(MysteriousRuins[0].isClickable() || MysteriousRuins[0].isOnScreen());
	}

	@Override
	public void execute() {
		Walking.walkPath(PathToRuins);
		Camera.setCamera(General.random(340, 360), 35);
	}

	@Override
	public String display() {
		return "Walk to Ruins";
	}

}
