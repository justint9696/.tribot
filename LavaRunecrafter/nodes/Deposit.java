package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;

import scripts.LavaRunecrafter.utils.Node;

public class Deposit implements Node {
	
	final int[] ids = { 5514, 5512, 5510, 5509, 557, 12791, 7936 };

	@Override
	public boolean validate() {
		return Banking.isBankScreenOpen() && Inventory.find(Filters.Items.idNotEquals(ids)).length > 0;
	}

	@Override
	public void execute() {
		RSItem[] items = Inventory.find(Filters.Items.idNotEquals(ids));
		if (items.length > 0) {
			for (RSItem item : items) {
				if (item.hover()) {
					if (Banking.deposit(0, item.getID())) {
						General.sleep(100, 330);
					}
				}
			}
		}
	}

	@Override
	public String display() {
		return "Deposit Items";
	}

}
