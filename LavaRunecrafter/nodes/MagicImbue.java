package scripts.LavaRunecrafter.nodes;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Login;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Objects;
import org.tribot.api2007.types.RSObject;

import scripts.LavaRunecrafter.data.Variables;
import scripts.LavaRunecrafter.utils.Node;

public class MagicImbue implements Node {
	
	RSObject[] FireAltar;

	@Override
	public boolean validate() {
		FireAltar = Objects.find(15, 34764);
		return FireAltar.length > 0 && !Variables.imbueState();
	}

	@Override
	public void execute() {
		if (GameTab.open(TABS.MAGIC)) {
			if (Interfaces.get(218, 124) != null) {
				if (Interfaces.get(218, 124).getTextureID() == 552) {
					Clicking.click(Interfaces.get(218, 124));
					Variables.imbueActive();
				} else {
					General.println("Out of Magic Imbue Runes.\nLogging out.");
					Login.logout();
					Variables.stopRunning();
				}
			}
		}
	}

	@Override
	public String display() {
		return "Cast Magic Imbue";
	}

}
