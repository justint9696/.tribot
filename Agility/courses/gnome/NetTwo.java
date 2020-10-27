package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class NetTwo extends Obstacle {
	
	public NetTwo() {
		super(23135, "Climb-over", "Obstacle net");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 0 && Player.getPosition().getY() >= 3420 && Player.getPosition().getY() < 3428;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
