package scripts.LavaRunecrafter.nodes;

import org.tribot.api.Timing;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class DuelArena implements Node {

	@Override
	public boolean validate() {
		return Inventory.isFull() && Variables.pouchState() && Objects.find(10, 4483).length > 0;
	}

	@Override
	public void execute() {
		final int[] ids = { 2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566 };
		RSItem[] ringSlot = Equipment.find(ids);
		if (ringSlot.length > 0) {
			RSItem ringOfDueling = ringSlot[0];
			if (ringOfDueling.hover()) {
				if (ringOfDueling.click("Duel Arena")) {
					if (Timing.waitCondition(() -> Player.getAnimation() == 714, 5000)) {
						Timing.waitCondition(() -> Player.getAnimation() == -1, 5000);
					}
				}
			}
		}
	}

	@Override
	public String display() {
		return "Teleport to Duel Arena";
	}

}
