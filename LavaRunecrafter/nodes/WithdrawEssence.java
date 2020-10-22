package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class WithdrawEssence implements Node {

	@Override
	public boolean validate() {
		return Banking.isBankScreenOpen() ? Inventory.find("Earth rune").length > 0 && !Inventory.isFull() : false;
	}

	@Override
	public void execute() {
		RSItem[] essence = Banking.find("Pure essence");
		if (essence.length > 0) {
			if (essence[0].hover()) {
				if (Banking.withdraw(0, "Pure essence")) {
					Timing.waitCondition(() -> Inventory.isFull(), 5000);
				}
			}
		} else {
			General.println("Out of Pure essence.\nLogging out.");
			Login.logout();
			Variables.stopRunning();
		}
	}

	@Override
	public String display() {
		return "Withdraw Pure essence";
	}

}
