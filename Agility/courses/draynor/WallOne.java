package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class WallOne extends Obstacle {

	public WallOne() {
		super(11404, "Climb", "Rough Wall");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getPosition().getPlane() == 0 && Player.getPosition().getX() <= 3105;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
