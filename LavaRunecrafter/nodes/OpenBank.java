package scripts.LavaRunecrafter.nodes;

import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;

import scripts.LavaRunecrafter.utils.Node;

public class OpenBank implements Node {
	
	RSObject[] obj;

	@Override
	public boolean validate() {
		obj = Objects.find(10, 4483);
		return obj.length > 0 ? !Banking.isBankScreenOpen() && !Inventory.isFull() : false;
	}

	@Override
	public void execute() {
		RSObject bankChest = obj[0];
		if (bankChest.hover()) {
			if (bankChest.click("Use")) {
				Timing.waitCondition(() -> Banking.isBankScreenOpen(), 1000);
			}
		}
	}

	@Override
	public String display() {
		return "Open Bank";
	}
	
	public boolean hasSupplies() {
		return Inventory.find("Earth rune").length > 0 && Inventory.find("Pure essence").length > 0 && Inventory.isFull();
	}

}
