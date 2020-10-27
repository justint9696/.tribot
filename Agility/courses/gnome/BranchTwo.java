package scripts.Agility.courses.gnome;

import org.tribot.api2007.Player;

import scripts.Agility.courses.Obstacle;

public class BranchTwo extends Obstacle {

	public BranchTwo() {
		super(23560, "Climb-down", "Tree branch");
	}

	@Override
	public boolean validate() {
		return Player.getPosition().getPlane() == 2 && Player.getPosition().getX() > 2482;
	}

	@Override
	public void execute() {
		super.interact();
	}

}
