package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class WithdrawRing implements Node {
	
	final int[] ids = { 2552, 2554, 2556, 2558, 2560, 2562, 2564 };

	@Override
	public boolean validate() {
		return Banking.isBankScreenOpen() && Equipment.find(ids).length == 0;
	}

	@Override
	public void execute() {
		RSItem[] ringOfDueling = Banking.find(ids);
		if (ringOfDueling.length > 0) {
			if (ringOfDueling[0].hover()) {
				if (Banking.withdraw(1, ids)) {
					Timing.waitCondition(() -> Inventory.find(ids).length > 1, 5000);
				}
			}
		} else {
			General.println("Out of Ring of Duelings.\nLogging out.");
			Banking.close();
			Login.logout();
			Variables.stopRunning();
		}
	}

	@Override
	public String display() {
		return "Withdraw Ring of Dueling";
	}

}
