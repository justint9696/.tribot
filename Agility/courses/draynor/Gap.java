package scripts.Agility.courses.draynor;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class Gap extends Obstacle {
	
	public Gap() {
		super(11631, "Jump", "Gap");
	}

	@Override
	public boolean validate() {
		return Player.getAnimation() == -1 && Player.getPosition().getPlane() == 3 && Player.getPosition().getX() > 3087 && Player.getPosition().getY() == 3255;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
