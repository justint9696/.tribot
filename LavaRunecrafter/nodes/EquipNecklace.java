package scripts.LavaRunecrafter.nodes;

import org.tribot.api.Timing;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.utils.Node;

public class EquipNecklace implements Node {
	
	@Override
	public boolean validate() {
		return Inventory.find("Binding necklace").length > 0;
	}

	@Override
	public void execute() {
		RSItem[] BindingNecklace = Inventory.find("Binding necklace");
		if (BindingNecklace[0].hover()) {
			if (BindingNecklace[0].click("Wear")) {
				Timing.waitCondition(() -> Equipment.find("Binding necklace").length > 0, 5000);
			}
		}
	}

	@Override
	public String display() {
		return "Equip Binding necklace";
	}

}
