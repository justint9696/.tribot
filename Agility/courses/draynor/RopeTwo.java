package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class RopeTwo extends Obstacle {
	
	public RopeTwo() {
		super(11406, "Cross", "Tightrope");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getPosition().getPlane() == 3 && Player.getPosition().getX() < 3092 && Player.getPosition().getY() < 3277;
	}

	@Override
	public void execute() {
		super.interact();
	}

}