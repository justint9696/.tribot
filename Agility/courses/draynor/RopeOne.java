package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class RopeOne extends Obstacle {
	
	public RopeOne() {
		super(11405, "Cross", "Tightrope");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getPosition().getPlane() == 3 && Player.getPosition().getX() > 3097;
	}

	@Override
	public void execute() {
		super.interact();
	}

}