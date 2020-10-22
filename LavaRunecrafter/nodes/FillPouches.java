package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class FillPouches implements Node {

	@Override
	public boolean validate() {
		return !Banking.isBankScreenOpen() && !Variables.pouchState() && Inventory.find("Pure essence").length > 0 && Objects.find(10, 4483).length > 0;
	}

	@Override
	public void execute() {
		final int[] ids = { 5514, 5512, 5510, 5509 };
		for (RSItem essencePouch : Inventory.find(ids)) {
			if (essencePouch.hover()) {
				if (essencePouch.click("Fill")) {
					General.sleep(100, 330);
				}
			}
		}
		Variables.fullPouches();
	}

	@Override
	public String display() {
		return "Fill pouches";
	}

}
