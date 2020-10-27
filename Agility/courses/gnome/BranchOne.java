package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class BranchOne extends Obstacle {

	public BranchOne() {
		super(23559, "Climb", "Tree branch");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 1 && Player.getPosition().getY() < 3425;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
