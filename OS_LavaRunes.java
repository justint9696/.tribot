package scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.types.RSVarBit;
import org.tribot.api2007.util.DPathNavigator;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MousePainting;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = {
		"MaxedNinja123" }, category = "Runecrafting", name = "OS Lava Runes", description = "Runecrafts lava runes. Ring of dueling support and stamina poition support. ABC2 features implemented.")
public class OS_LavaRunes extends Script implements Painting, MousePainting {

	public long startTime = System.currentTimeMillis();
	public RSTile bankChest = new RSTile(2443, 3083, 0);
	public RSTile duelArena = new RSTile(3316, 3236, 0);
	public RSTile castleWars = new RSTile(2439, 3090, 0);
	public RSTile mysteriousRuins = new RSTile(3312, 3253, 0);
	public RSTile fireAltar = new RSTile(2583, 4838, 0);
	public int[] ring = { 2552, 2554, 2556, 2558, 2560, 2562, 2564 };
	public int[] stamina = { 12625, 12627, 12629, 12631 };
	public int[] bankItems = { 12625, 12627, 12629, 12631, 557, 5514, 5512, 5510, 5509 };
	public int[] ESSENCE_POUCHES = { 5514, 5512, 5510, 5509 };
	public int startExp = Skills.getXP(SKILLS.RUNECRAFTING);
	public ABCUtil abc_util = new ABCUtil();
	public DPathNavigator path = new DPathNavigator();

	@Override
	public void run() {
		println("OS Lava Runecrafter started.");
		Mouse.setSpeed(General.random(115, 125));

		while (true) {
			antiban();
			switch (getState()) {
			case USING_STAMINA:
				stamina();
				break;
			case WALKING_TO_BANK:
				walkToBank();
				break;
			case BANKING:
				bank();
				break;
			case TELEPORTING_TO_DUEL_ARENA:
				teleport("Duel Arena");
				break;
			case WALKING_TO_RUINS:
				walkToRuins();
				break;
			case ENTERING_RUINS:
				enterRuins();
				break;
			case WALKING_TO_ALTAR:
				walkToAltar();
				break;
			case CRAFTING_RUNES:
				craftRunes();
				break;
			case TELEPORTING_TO_CASTLE_WARS:
				teleport("Castle Wars");
				break;
			default:
				break;
			}
			sleep(300, 800);
		}
	}

	private enum State {
		USING_STAMINA, WALKING_TO_BANK, BANKING, FILLING_POUCHES, TELEPORTING_TO_DUEL_ARENA, WALKING_TO_RUINS,
		ENTERING_RUINS, WALKING_TO_ALTAR, CRAFTING_RUNES, TELEPORTING_TO_CASTLE_WARS, LOST;
	}

	private State getState() {
		if (Game.getRunEnergy() < 30) {
			return State.USING_STAMINA;
		} else if (isAtBank()) {
			if (Inventory.find("Rune essence", "Pure essence").length == 0)
				return State.BANKING;
			else {
				return State.TELEPORTING_TO_DUEL_ARENA;
			}
		} else if (isAtDuelArena()) {
			return State.WALKING_TO_RUINS;
		} else if (isAtCastleWars()) {
			return State.WALKING_TO_BANK;
		} else if (isAtFireAltar()) {
			return State.WALKING_TO_ALTAR;
		} else {
			RSObject[] ruins = Objects.findNearest(15, "Mysterious ruins");
			if (ruins != null && ruins.length > 0)
				return State.ENTERING_RUINS;
			else {
				RSObject[] altar = Objects.findNearest(5, "Altar");
				if (altar != null && altar.length > 0) {
					if (Inventory.find("Rune essence", "Pure essence").length > 0)
						return State.CRAFTING_RUNES;
					else
						return State.TELEPORTING_TO_CASTLE_WARS;
				}
			}
		}
		return State.LOST;
	}

	private boolean isAtBank() {
		return Player.getPosition().distanceTo(bankChest) < 3;
	}

	private boolean isAtDuelArena() {
		return Player.getPosition().distanceTo(duelArena) < 8;
	}

	private boolean isAtCastleWars() {
		return Player.getPosition().distanceTo(castleWars) < 8;
	}

	private boolean isAtFireAltar() {
		RSObject[] portal = Objects.findNearest(8, "Portal");
		if (portal != null && portal.length > 0) {
			return true;
		}
		return false;
	}

	private void stamina() {
		RSItem[] staminaPotion = Inventory.find(stamina);
		if (staminaPotion != null && staminaPotion.length > 0)
			staminaPotion[0].click("Drink");
	}

	private void bank() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		else {
			withdrawRing();
			withdrawRunes();
			withdrawTalisman();
			withdrawEssence();
			Banking.close();
		}
	}

	private void withdrawRing() {
		if (isLastCharge() || !Equipment.isEquipped(ring)) {
			RSItem[] ringOfDueling1 = Banking.find(ring);
			if (ringOfDueling1 != null && ringOfDueling1.length > 0) {
				Banking.withdraw(1, ring);
				Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						return Inventory.find(ring).length > 0;
					}
				}, General.random(8000, 9000));
			} else {
				Login.logout();
				println("No ring of dueling in bank.");
				stopScript();
			}
			Banking.close();

			RSItem[] ringOfDueling2 = Inventory.find(ring);
			if (ringOfDueling2 != null && ringOfDueling2.length > 0) {
				if (ringOfDueling2[0].click("Wear"))
					Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							return Equipment.isEquipped(ring);
						}

					}, General.random(8000, 9000));
			}
			Banking.openBank();
		}
		Banking.depositAllExcept(bankItems);
		if (Inventory.find(stamina).length < 1) {

			RSItem[] staminaPotion = Banking.find(stamina);
			if (staminaPotion != null && staminaPotion.length > 0) {
				Banking.withdraw(1, stamina);
			}
		}
	}

	private void withdrawRunes() {
		RSItem[] earthRunes = Banking.find("Earth rune");
		if (earthRunes.length < 0) {
			Login.logout();
			println("No earth runes in bank.");
			stopScript();
		} else {
			Banking.withdraw(0, "Earth rune");
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					return Inventory.find("Earth rune").length > 0;
				}

			}, General.random(8000, 9000));
		}
	}

	private void withdrawTalisman() {
		RSItem[] earthTalisman = Banking.find("Earth talisman");
		if (earthTalisman.length < 0) {
			Login.logout();
			println("No earth rune in bank");
			stopScript();
		} else {
			Banking.withdraw(1, "Earth talisman");
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					return Inventory.find("Earth talisman").length > 0;
				}

			}, General.random(8000, 9000));
		}
	}

	private void withdrawPouches() {
		RSItem[] pouches = Banking.find(ESSENCE_POUCHES);
		if (pouches.length > 0) {
			for (RSItem pouch : pouches)
				Banking.withdraw(0, ESSENCE_POUCHES);
		}
	}

	private void withdrawEssence() {
		RSItem[] essence = Banking.find("Rune essence", "Pure essence");
		if (essence.length < 0) {
			Login.logout();
			println("No essence in bank");
			stopScript();
		} else {
			Banking.withdraw(0, "Rune essence", "Pure essence");
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					return Inventory.find("Rune essence", "Pure essence").length > 0;
				}
			}, General.random(8000, 9000));
		}
	}

	private boolean isLastCharge() {
		RSItem[] ring = Equipment.find(SLOTS.RING);
		if (ring != null && ring.length > 0) {
			return ring[0].getID() == 2566;
		}
		return false;
	}

	private void teleport(String location) {
		RSItem[] ring = Equipment.find(SLOTS.RING);
		if (ring != null && ring.length > 0) {
			if (ring[0].click(location)) {
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return Player.getAnimation() == 714;
					}

				}, General.random(8000, 9000));
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						return Player.getAnimation() == -1;
					}

				}, General.random(8000, 9000));
			}
		}
	}

	private void walkToBank() {
		if (isAtCastleWars()) {
			if (Player.getPosition().distanceTo(bankChest) > 3) {
				Walking.clickTileMM(bankChest, 1);
				Camera.setCameraRotation(General.random(250, 290));
				while (Player.isMoving())
					sleep(100);
			}
		}
	}

	private void walkToRuins() {
		if (Player.getPosition().distanceTo(mysteriousRuins) > 3) {
			path.traverse(mysteriousRuins);
			Camera.setCameraRotation(General.random(300, 330));
		}
	}

	private void walkToAltar() {
		if (Player.getPosition().distanceTo(fireAltar) > 3) {
			path.traverse(fireAltar);
			Camera.setCameraRotation(General.random(200, 250));
			Camera.setCameraAngle(General.random(80, 100));
		}
	}

	private void enterRuins() {
		RSObject[] ruins = Objects.findNearest(15, "Mysterious ruins");
		if (ruins != null && ruins.length > 0) {
			if (ruins[0].isOnScreen()) {
				if (ruins[0].isClickable()) {
					ruins[0].click("Enter");
				}
			}
		}
	}

	private void craftRunes() {
		RSObject[] craftRune = Objects.find(15, 34764);
		if (craftRune != null && craftRune.length > 0) {
			if (craftRune[0].isOnScreen()) {
				RSItem[] earthRune = Inventory.find("Earth rune");
				earthRune[0].click("Use");
				sleep(100, 150);
				if (DynamicClicking.clickRSObject(craftRune[0], "Use")) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return Player.getAnimation() == 791;
						}

					}, General.random(8000, 9000));
				}
			}
		}
	}

	public void antiban() {
		if (this.abc_util.shouldCheckTabs()) {
			println("[ABC2] Checking random tab");
			this.abc_util.checkTabs();
		}
		if (this.abc_util.shouldCheckXP()) {
			println("[ABC2] Checking XP");
			this.abc_util.checkXP();
		}
		if (this.abc_util.shouldExamineEntity()) {
			println("[ABC2] Examining random entity");
			this.abc_util.examineEntity();
		}
		if (this.abc_util.shouldMoveMouse()) {
			println("[ABC2] Moving mouse");
			this.abc_util.moveMouse();
		}
		if (this.abc_util.shouldPickupMouse()) {
			println("[ABC2] Picking up mouse");
			this.abc_util.pickupMouse();
		}
		if (this.abc_util.shouldRightClick()) {
			println("[ABC2] Random right click");
			this.abc_util.rightClick();
		}
		if (this.abc_util.shouldRotateCamera()) {
			println("[ABC2] Rotating camera");
			this.abc_util.rotateCamera();
		}
		if (this.abc_util.shouldLeaveGame()) {
			println("[ABC2] Moving mouse off screen");
			this.abc_util.leaveGame();
		}
	}

	@Override
	public void onPaint(Graphics g) {
		long timeRan = System.currentTimeMillis() - startTime;
		double multiplier = getRunningTime() / 3600000.0D;
		int expPerHour = (int) ((Skills.getXP(SKILLS.RUNECRAFTING) - startExp) / multiplier);
		g.setColor(Color.GREEN);
		g.drawString("OS Lava Runes", 400, 285);
		g.drawString("Status: " + getState(), 400, 300);
		g.drawString("Experience: " + (Skills.getXP(SKILLS.RUNECRAFTING) - startExp) + " (" + expPerHour + ")/hr", 400,
				315);
		g.drawString("Runtime: " + Timing.msToString(timeRan), 400, 330);
	}

	@Override
	public void paintMouse(Graphics arg0, Point arg1, Point arg2) {
		Graphics2D g = (Graphics2D) arg0;
		int x, y;
		x = (int) Mouse.getPos().getX();
		y = (int) Mouse.getPos().getY();
		g.setColor(Color.GREEN);
		g.drawLine(x - 5, y, x + 5, y);
		g.drawLine(x, y - 5, x, y + 5);
	}

}
