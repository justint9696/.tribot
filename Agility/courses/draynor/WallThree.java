package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class WallThree extends Obstacle {

	public WallThree() {
		super(11630, "Jump-up", "Wall");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getPosition().getPlane() == 3 && Player.getPosition().getX() < 3089 && Player.getPosition().getY() < 3262 && Player.getPosition().getY() > 3255;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
