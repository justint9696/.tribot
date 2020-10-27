package scripts.Agility.nodes;

import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Game;
import org.tribot.api2007.Options;

import scripts.Agility.utils.Node;

public class Run implements Node {
	
	private final ABCUtil ABCUtil = new ABCUtil();
	private int nextRun = ABCUtil.generateRunActivation();

	@Override
	public boolean validate() {
		return !Game.isRunOn() && Game.getRunEnergy() > nextRun;
	}

	@Override
	public void execute() {
		if (run()) {
			nextRun = ABCUtil.generateRunActivation();
		}
	}
	
	private boolean run() {
		return Options.setRunOn(true);
	}

}
