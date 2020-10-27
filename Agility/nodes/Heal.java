package scripts.Agility.nodes;

import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;

import scripts.Agility.utils.Node;

public class Heal implements Node {

	private final ABCUtil ABCUtil = new ABCUtil();

	@Override
	public boolean validate() {
		return Player.getRSPlayer().getHealthPercent() < 0.50D;
	}

	@Override
	public void execute() {
		RSItem[] food = Inventory.find(Filters.Items.actionsEquals("Eat"));
		if (food.length > 0) {
			if (food[0].hover()) {
				food[0].click("Eat");
			}
		}
	}

}
