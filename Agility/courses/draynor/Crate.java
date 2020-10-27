package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class Crate extends Obstacle {

	public Crate() {
		super(11632, "Climb-down", "Crate");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getPosition().getPlane() == 3 && Player.getPosition().getX() > 3095 && Player.getPosition().getX() < 3102 && Player.getPosition().getY() > 3255;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
