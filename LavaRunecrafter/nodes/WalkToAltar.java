package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.LavaRunecrafter.utils.Node;

public class WalkToAltar implements Node {
	
	RSObject[] FireAltar;

	@Override
	public boolean validate() {
		FireAltar = Objects.find(15, 34764);
		return FireAltar.length > 0 && !(FireAltar[0].isClickable() || FireAltar[0].isOnScreen()) && !Player.isMoving();
	}

	@Override
	public void execute() {
		final int x = FireAltar[0].getPosition().getX() + General.random(-3, 3);
		final int y = FireAltar[0].getPosition().getY() + General.random(-3, 3);
		final RSTile coordinates = new RSTile(x, y);
		
		if (Walking.clickTileMM(coordinates, 1)) {
			Camera.setCamera(General.random(200, 240), General.random(80, 100));
			
		}
	}

	@Override
	public String display() {
		return "Walk to Altar";
	}

}
