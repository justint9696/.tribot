package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class Rope extends Obstacle {
	
	public Rope() {
		super(23557, "Walk-on", "Balancing rope");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 2 && Player.getPosition().getX() < 2480;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
