package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class NetOne extends Obstacle {
	
	public NetOne() {
		super(23134, "Climb-over", "Obstacle net");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 0 && Player.getPosition().getY() < 3430 && Player.getPosition().getX() < 2480;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
