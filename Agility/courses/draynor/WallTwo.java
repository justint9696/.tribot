package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class WallTwo extends Obstacle {

	public WallTwo() {
		super(11430, "Balance", "Narrow wall");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getAnimation() == -1 && Player.getPosition().getPlane() == 3 && Player.getPosition().getX() < 3093 && Player.getPosition().getY() > 3261;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
