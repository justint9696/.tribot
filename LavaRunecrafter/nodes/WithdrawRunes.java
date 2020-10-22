package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.utils.Node;

public class WithdrawRunes implements Node {

	@Override
	public boolean validate() {
		return Banking.isBankScreenOpen() ? Inventory.find("Earth rune").length == 0 && !Inventory.isFull() : false;
	}

	@Override
	public void execute() {
		RSItem[] runes = Banking.find("Earth rune");
		if (runes.length > 0) {
			if (runes[0].hover()) {
				if (Banking.withdraw(0, "Earth rune")) {
					Timing.waitCondition(() -> Inventory.find("Earth rune").length > 0, 5000);
				}
			}
		}
	}

	@Override
	public String display() {
		return "Withdraw runes";
	}

}
