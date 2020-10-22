package scripts.LavaRunecrafter.nodes;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.LavaRunecrafter.utils.Node;

public class OpenBank implements Node {
	
	RSObject[] BankChest;

	@Override
	public boolean validate() {
		BankChest = Objects.find(10, 4483);
		return BankChest.length > 0 ? !Banking.isBankScreenOpen() && !Inventory.isFull() : false;
	}

	@Override
	public void execute() {
			if (BankChest[0].isClickable() && BankChest[0].isOnScreen()) {
				if (BankChest[0].hover()) {
					if (BankChest[0].click("Use")) {
						Timing.waitCondition(() -> Banking.isBankScreenOpen(), 5000);
					}
				}
		} else {
			if (Walking.clickTileMM(new RSTile(BankChest[0].getPosition().getX() + General.random(-3, 3), BankChest[0].getPosition().getY() + General.random(-3, 3)), 1)) {
				BankChest[0].adjustCameraTo();
			}
		}
	}

	@Override
	public String display() {
		return "Open Bank";
	}
	
	public boolean hasSupplies() {
		return Inventory.find("Earth rune").length > 0 && Inventory.find("Pure essence").length > 0 && Inventory.isFull();
	}

}
