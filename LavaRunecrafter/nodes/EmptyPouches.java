package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class EmptyPouches implements Node {

	RSObject[] FireAltar;
	
	@Override
	public boolean validate() {
		FireAltar = Objects.find(15, 34764);
		return FireAltar.length > 0 && !Inventory.isFull() && Variables.pouchState();
	}

	@Override
	public void execute() {
		final int[] ids = { 5514, 5512, 5510, 5509 };
		for (RSItem essencePouch : Inventory.find(ids)) {
			if (essencePouch.hover()) {
				if (essencePouch.click("Empty")) {
					General.sleep(100, 330);
				}
			}
		}
		Variables.emptyPouches();
	}

	@Override
	public String display() {
		return "Empty pouches";
	}

}
