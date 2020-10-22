package scripts.LavaRunecrafter.nodes;

import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.utils.Node;

public class WithdrawNecklace implements Node {

	@Override
	public boolean validate() {
		return Banking.isBankScreenOpen() && Equipment.find("Binding necklace").length == 0;
	}

	@Override
	public void execute() {
		RSItem[] BindingNecklace = Banking.find("Binding necklace");
		if (BindingNecklace.length > 0)  {
			if (BindingNecklace[0].hover()) {
				if (Banking.withdraw(1, "Binding necklace")) {
					Timing.waitCondition(() -> Inventory.find("Binding necklace").length > 0, 5000);
				}
			}
		}
	}

	@Override
	public String display() {
		return "Withdraw Binding necklace";
	}

}
