package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.LavaRunecrafter.utils.Node;

public class EnterRuins implements Node {
	
	RSObject[] MysteriousRuins;

	@Override
	public boolean validate() {
		MysteriousRuins = Objects.find(15, 34817);
		return MysteriousRuins.length > 0 && (MysteriousRuins[0].isClickable() || MysteriousRuins[0].isOnScreen());
	}

	@Override
	public void execute() {
		if (MysteriousRuins[0].hover()) {
			if (MysteriousRuins[0].click("Enter")) {
				do {
					General.sleep(100);
				} while (Player.isMoving());
			}
		}
	}

	@Override
	public String display() {
		return "Enter Mysterious Ruins";
	}

}
