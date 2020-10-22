package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;

import scripts.LavaRunecrafter.utils.Node;

public class CloseBank implements Node {

	@Override
	public boolean validate() {
		return Banking.isBankScreenOpen() && Inventory.isFull();
	}

	@Override
	public void execute() {
		if (Banking.close()) {
			General.sleep(100, 330);
		}
	}

	@Override
	public String display() {
		return "Close Bank";
	}

}
