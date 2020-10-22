package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class CraftRunes implements Node {
	
	RSObject[] FireAltar;

	@Override
	public boolean validate() {
		FireAltar = Objects.find(15, 34764);
		return FireAltar.length > 0 && (FireAltar[0].isClickable() || FireAltar[0].isOnScreen()) && Inventory.find("Pure essence").length > 0 && Variables.imbueState();
	}

	@Override
	public void execute() {
		RSItem[] EarthRune = Inventory.find("Earth rune");
		if (EarthRune.length > 0) {
			if (EarthRune[0].hover()) {
				if (EarthRune[0].click("Use")) {
					if (FireAltar[0].hover()) {
						do {
							General.sleep(100);
						} while (Player.getAnimation() > -1);
						if (FireAltar[0].click("Use")) {
							Timing.waitCondition(() -> Player.getAnimation() > -1, 5000);
						}
					}
				}
			}
		}
	}

	@Override
	public String display() {
		return "Craft Lava runes";
	}

}
