package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class Log extends Obstacle {
	
	public Log() {
		super(23145, "Walk-across", "Log balance");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 0 && Player.getPosition().getY() > 3435;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
