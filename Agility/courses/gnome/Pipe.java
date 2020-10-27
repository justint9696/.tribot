package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class Pipe extends Obstacle {
	
	public Pipe() {
		super(23138, "Squeeze-through", "Obstacle pipe");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 0 && Player.getPosition().getY() > 3427 && Player.getPosition().getY() < 3433 && Player.getPosition().getX() > 2480;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
