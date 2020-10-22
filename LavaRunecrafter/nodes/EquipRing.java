package scripts.LavaRunecrafter.nodes;

import org.tribot.api.Timing;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.utils.Node;

public class EquipRing implements Node {
	
	final int[] ids = { 2552, 2554, 2556, 2558, 2560, 2562, 2564 };

	@Override
	public boolean validate() {
		return Inventory.find(ids).length > 0;
	}

	@Override
	public void execute() {
		RSItem[] ringOfDueling = Inventory.find(ids);
		if (ringOfDueling[0].hover()) {
			if (ringOfDueling[0].click("Wear")) {
				Timing.waitCondition(() -> Equipment.find(ids).length > 0, 5000);
			}
		}
	}

	@Override
	public String display() {
		return "Equip Ring of Dueling";
	}

}
