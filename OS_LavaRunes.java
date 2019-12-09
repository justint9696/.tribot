package scripts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.interfaces.Positionable;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.util.abc.ABCUtil;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Equipment.SLOTS;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Walking;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;
import org.tribot.api2007.util.DPathNavigator;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MousePainting;
import org.tribot.script.interfaces.Painting;

@ScriptManifest(authors = {
		"MaxedNinja123" }, category = "Runecrafting", name = "OS Lava Runecrafter", description = "Runecrafts lava runes. Ring of dueling support and stamina poition support. ABC2 features implemented.")
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
	public int[] essencePouches = new int[] { 5514, 5512, 5510, 5509 };
	public int startExp = Skills.getXP(SKILLS.RUNECRAFTING);
	public int startLevel = Skills.getCurrentLevel(SKILLS.RUNECRAFTING);

	public ABCUtil ABCUtil = new ABCUtil();

	public DPathNavigator DPath = new DPathNavigator();

	public Font font = new Font("Orator Std", Font.BOLD, 14);

	public boolean GUI_COMPLETE = false;
	public boolean staminaSupport;
	public boolean mouseKeys;
	public boolean fullPouches = false;

	@Override
	public void run() {
		if (onStart()) {
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
				case WITHDRAWING_RING:
					withdrawRing();
					break;
				case WITHDRAWING_STAMINA:
					withdrawStamina();
					break;
				case WITHDRAWING_RUNES:
					withdrawRunes();
					break;
				case WITHDRAWING_TALISMAN:
					withdrawTalisman();
					break;
				case WITHDRAWING_ESSENCE:
					withdrawEssence();
					break;
				case FILLING_POUCHES:
					fillPouches();
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
				case EMPTYING_POUCHES:
					emptyPouches();
					break;
				case TELEPORTING_TO_CASTLE_WARS:
					teleport("Castle Wars");
					break;
				default:
					break;
				}
				sleep(300, 350);
			}
		}
	}

	private boolean onStart() {
		GUI GUI = new GUI();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenW = (screenSize.width) / 2;
		int screenH = (screenSize.height) / 2;

		GUI.setVisible(true);

		GUI.setLocation((screenW / 2), (screenH / 2));

		while (!GUI_COMPLETE)
			sleep(100);

		GUI.setVisible(false);

		Mouse.setSpeed(General.random(100, 115));

		Camera.setRotationMethod(Camera.ROTATION_METHOD.DEFAULT);

		println("OS Lava Runecrafter started.");

		return true;
	}

	private enum State {
		USING_STAMINA, WALKING_TO_BANK, BANKING, WITHDRAWING_STAMINA, WITHDRAWING_RUNES, WITHDRAWING_RING,
		WITHDRAWING_TALISMAN, WITHDRAWING_ESSENCE, TELEPORTING_TO_DUEL_ARENA, WALKING_TO_RUINS, ENTERING_RUINS,
		WALKING_TO_ALTAR, CRAFTING_RUNES, EMPTYING_POUCHES, FILLING_POUCHES, TELEPORTING_TO_CASTLE_WARS, IDLE;
	}

	private State getState() {
		if (staminaSupport && Game.getRunEnergy() < 30) {
			return State.USING_STAMINA;
		} else if (isAtBank()) {
			if (hasSupplies() && Inventory.isFull()) {
				return State.TELEPORTING_TO_DUEL_ARENA;
			} else {
				if (!fullPouches && Inventory.find("Pure essence").length > 0) {
					return State.FILLING_POUCHES;
				} else if (!Banking.isBankScreenOpen()) {
					return State.BANKING;
				} else {
					if (staminaSupport && Inventory.find(stamina).length == 0) {
						return State.WITHDRAWING_STAMINA;
					} else if (Inventory.find("Earth rune").length == 0) {
						return State.WITHDRAWING_RUNES;
					} else if (!Equipment.isEquipped(ring) || isLastCharge()) {
						return State.WITHDRAWING_RING;
					} else if (Inventory.find("Earth talisman").length == 0) {
						return State.WITHDRAWING_TALISMAN;
					} else if (Inventory.find("Pure essence").length == 0 || !Inventory.isFull()) {
						return State.WITHDRAWING_ESSENCE;
					}
				}
			}
		} else if (isAtDuelArena()) {
			return State.WALKING_TO_RUINS;
		} else if (isAtCastleWars()) {
			return State.WALKING_TO_BANK;
		} else if (isAtFireAltar()) {
			return State.WALKING_TO_ALTAR;
		} else {
			RSObject[] ruins = Objects.findNearest(15, "Mysterious ruins");
			if (ruins != null && ruins.length > 0) {
				if (ruins[0].isOnScreen())
					return State.ENTERING_RUINS;
			} else {
				RSObject[] altar = Objects.findNearest(5, "Altar");
				if (altar != null && altar.length > 0) {
					if (altar[0].isOnScreen()) {
						if (Inventory.find("Pure essence").length > 0)
							return State.CRAFTING_RUNES;
						else if (fullPouches)
							return State.EMPTYING_POUCHES;
						else
							return State.TELEPORTING_TO_CASTLE_WARS;
					}
				}
			}
		}
		return State.IDLE;
	}

	private boolean hasSupplies() {
		boolean hasRing = Equipment.isEquipped(ring) && !isLastCharge();
		boolean hasRunes = Inventory.find("Earth rune").length > 0;
		boolean hasStamina = (!staminaSupport) || (staminaSupport && Inventory.find(stamina).length > 0);
		boolean hasTalisman = Inventory.find("Earth talisman").length > 0;
		boolean hasEssence = Inventory.find("Pure essence").length > 0;
		return (fullPouches && hasRing && hasRunes && hasStamina && hasTalisman && hasEssence);
	}

	private boolean isAtBank() {
		return Player.getPosition().distanceTo(bankChest) < 5;
	}

	private boolean isAtDuelArena() {
		return Player.getPosition().distanceTo(duelArena) < 5;
	}

	private boolean isAtCastleWars() {
		return Player.getPosition().distanceTo(castleWars) < 5;
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
			if (staminaPotion[0].click("Drink")) {
				if (Timing.waitCondition(() -> Player.getAnimation() == 829, 3000)) {
					Timing.waitCondition(() -> Player.getAnimation() == -1, 3000);
				}
			}

	}

	private void bank() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();

		if (mouseKeys) {
			RSItem[] items = Inventory
					.find(Filters.Items
							.nameNotContains(
									"Earth rune")
							.combine(Filters.Items.nameNotContains("Stamina ").combine(
									Filters.Items.nameNotContains(" pouch")
											.combine(Filters.Items.nameNotContains("Pure essence")
													.combine(Filters.Items.nameNotContains(" talisman"), false), false),
									false), false));
			if (items.length > 0) {
				for (RSItem item : items) {
					if (item.hover()) {
						Mouse.click(3);
						Point point = new Point((int) Mouse.getPos().getX(), (int) Mouse.getPos().getY() + 100);
						Mouse.hop(point);
						Mouse.click(1);
					}
					sleep(300, 350);
				}
			}
		} else
			Banking.depositAllExcept(bankItems);
	}

	private void withdrawRing() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		if (isLastCharge() || !Equipment.isEquipped(ring)) {
			RSItem[] ringOfDueling1 = Banking.find(ring);
			if (ringOfDueling1 != null && ringOfDueling1.length > 0) {
				Banking.withdraw(1, ring);
				Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						sleep(100);
						return Inventory.find(ring).length > 0;
					}
				}, General.random(8000, 9000));
			} else {
				Banking.close();
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
							sleep(100);
							return Equipment.isEquipped(ring);
						}

					}, General.random(8000, 9000));
			}
			Banking.openBank();
		}
	}

	private void withdrawStamina() {
		if (staminaSupport) {
			if (Inventory.find(stamina).length < 1) {
				RSItem[] staminaPotion = Banking.find(stamina);
				if (staminaPotion != null && staminaPotion.length > 0) {
					Banking.withdraw(1, stamina);
					Timing.waitCondition(() -> Inventory.find(stamina).length > 0, 3000);
				}
			}
		}
	}

	private void withdrawRunes() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		RSItem[] earthRunes = Banking.find("Earth rune");
		if (earthRunes.length == 0 && Inventory.find("Earth rune").length == 0) {
			Banking.close();
			Login.logout();
			println("No earth runes in bank.");
			stopScript();
		} else {
			RSItem[] item = Banking.find("Earth rune");
			if (item.length > 0) {
				if (item[0].hover()) {
					if (mouseKeys) {
						Mouse.click(3);
						Point point = new Point((int) Mouse.getPos().getX(), (int) Mouse.getPos().getY() + 100);
						Mouse.hop(point);
						Mouse.click(1);
					} else {
						Banking.withdraw(0, "Earth rune");
					}
					Timing.waitCondition(() -> Inventory.find("Earth rune").length > 0, 3000);
				}
			}
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					sleep(100);
					return Inventory.find("Earth rune").length > 0;
				}

			}, General.random(8000, 9000));
		}
	}

	private void withdrawTalisman() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		RSItem[] earthTalisman = Banking.find("Earth talisman");
		if (earthTalisman.length == 0 && Inventory.find("Earth talisman").length == 0) {
			Banking.close();
			Login.logout();
			println("No earth talisman in bank");
			stopScript();
		} else {
			int numPouches = Inventory.find(essencePouches).length;
			if (numPouches > 0)
				Banking.withdraw(numPouches, "Earth talisman");
			else
				Banking.withdraw(1, "Earth talisman");
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					sleep(100);
					return Inventory.find("Earth talisman").length > 0;
				}

			}, General.random(8000, 9000));
		}
	}

	private void withdrawEssence() {
		if (!Banking.isBankScreenOpen())
			Banking.openBank();
		RSItem[] essence = Banking.find("Pure essence");
		if (essence.length == 0 && Inventory.find("Pure essence").length == 0) {
			Banking.close();
			Login.logout();
			println("No essence in bank");
			stopScript();
		} else {
			RSItem[] item = Banking.find("Pure essence");
			if (item.length > 0) {
				if (item[0].hover()) {
					if (mouseKeys) {
						Mouse.click(3);
						Point point = new Point((int) Mouse.getPos().getX(), (int) Mouse.getPos().getY() + 100);
						Mouse.hop(point);
						Mouse.click(1);
					} else {
						Banking.withdraw(0, "Pure essence");
					}
				}
			}
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					sleep(100);
					return Inventory.find("Pure essence").length > 0 && Banking.close();
				}
			}, General.random(8000, 9000));
		}
	}

	private void fillPouches() {
		RSItem[] pouches = Inventory.find(essencePouches);
		if (pouches.length > 0) {
			for (RSItem pouch : pouches) {
				if (Clicking.click("Fill", pouch)) {
					sleep(500, 550);
				}
			}
			fullPouches = true;
		} else {
			fullPouches = true;
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
						sleep(100);
						return Player.getAnimation() == 714;
					}

				}, General.random(8000, 9000));
				Timing.waitCondition(new Condition() {
					@Override
					public boolean active() {
						sleep(100);
						return Player.getAnimation() == -1;
					}

				}, General.random(8000, 9000));
			}
		}
		sleep(400, 450);
	}

	private void walkToBank() {
		if (isAtCastleWars()) {
			if (Player.getPosition().distanceTo(bankChest) > 3) {
				Walking.clickTileMM(bankChest, 1);
				Timing.waitCondition(() -> !Player.isMoving(), 3000);
			}
		}
	}

	private void walkToRuins() {
		if (Player.getPosition().distanceTo(mysteriousRuins) > 3) {
			Walking.clickTileMM(new RSTile(3311, 3249, 0), 1);
			if (Camera.getCameraRotation() < 300 || Camera.getCameraRotation() > 360) {
				Camera.setCameraRotation(General.random(300, 360));
			}
			if (Camera.getCameraAngle() > 30) {
				Camera.setCameraAngle(General.random(20, 40));
			}
		}
	}

	private void walkToAltar() {
		RSTile altar = new RSTile(fireAltar.getX() + General.random(-1, 1), fireAltar.getY() + General.random(-1, 1));
		if (Player.getPosition().distanceTo(altar) > 3) {
			Timing.waitCondition(() -> Walking.clickTileMM(altar, 1) && Player.isMoving() && Inventory.open(), 8000);
			if (Camera.getCameraRotation() < 200 || Camera.getCameraRotation() > 300) {
				Camera.setCameraRotation(General.random(200, 300));
			}
			if (Camera.getCameraAngle() < 90) {
				Camera.setCameraAngle(General.random(90, 100));
			}
			Timing.waitCondition(
					() -> Objects.find(20, 34764)[0].isOnScreen() && Player.getPosition().distanceTo(altar) < 5, 8000);
		}
	}

	private void enterRuins() {
		RSObject[] ruins = Objects.findNearest(15, "Mysterious ruins");
		if (ruins != null && ruins.length > 0) {
			if (ruins[0].isOnScreen()) {
				if (ruins[0].isClickable()) {
					if (ruins[0].click("Enter"))
						Timing.waitCondition(() -> Player.getPosition().distanceTo(fireAltar) < 20, 3000);
				}
			}
		}
	}

	private void craftRunes() {
		RSObject[] craftRune = Objects.find(15, 34764);
		if (craftRune != null && craftRune.length > 0) {
			if (craftRune[0].isOnScreen() && craftRune[0].isClickable()) {
				RSItem[] earthRune = Inventory.find("Earth rune");
				earthRune[0].click("Use");
				sleep(100, 150);
				if (craftRune[0].click("Use")) {
					Timing.waitCondition(new Condition() {
						@Override
						public boolean active() {
							return Player.getAnimation() == 791;
						}

					}, General.random(8000, 9000));
				}
			}
		}
		checkXP();
	}

	private void emptyPouches() {
		RSItem[] pouches = Inventory.find(essencePouches);
		if (pouches.length > 0) {
			for (RSItem pouch : pouches) {
				if (Clicking.click("Empty", pouch)) {
					sleep(500, 550);
				}
			}
			fullPouches = false;
		} else {
			fullPouches = false;
		}
	}

	public void checkXP() {
		switch (General.random(0, 10)) {
		case 0:
			println("[ABC2] Checking XP");
			if (GameTab.open(TABS.STATS)) {
				Skills.hover(SKILLS.RUNECRAFTING);
				sleep(this.ABCUtil.generateReactionTime());
			}
			break;
		}
	}

	public void antiban() {
		switch (General.random(1, 500)) {
		case 25:
			println("[ABC2] Moving mouse off screen");
			Mouse.leaveGame();
			sleep(this.ABCUtil.generateReactionTime());
			break;
		case 50:
			long waitTime = this.ABCUtil.generateReactionTime();
			println("[ABC2] Sleeping for " + waitTime);
			sleep(waitTime);
		}
	}

	@Override
	public void onPaint(Graphics g) {
		long timeRan = System.currentTimeMillis() - startTime;
		double multiplier = getRunningTime() / 3600000.0D;
		int expPerHour = (int) ((Skills.getXP(SKILLS.RUNECRAFTING) - startExp) / multiplier);
		int gainedLevels = Skills.getCurrentLevel(SKILLS.RUNECRAFTING) - startLevel;

		g.setColor(new Color(0, 0, 0, .8f));
		g.drawRect(5, 345, 505, 130);
		g.fillRect(5, 345, 505, 130);

		Font font = new Font("Verdana", Font.BOLD, 14);
		g.setFont(font);

		g.setColor(Color.WHITE);
		g.drawString("OS Lava Runecrafter", 15, 365);
		g.drawString("Runtime: " + Timing.msToString(System.currentTimeMillis() - startTime), 15, 395);
		g.drawString("State: " + getState(), 15, 410);
		g.drawString("XP Gained: " + (Skills.getXP(SKILLS.RUNECRAFTING) - startExp) + " (" + expPerHour + "/hr)", 15,
				425);
		g.drawString("Level: " + Skills.getCurrentLevel(SKILLS.RUNECRAFTING) + " (Gained: " + gainedLevels + ")", 15,
				440);
	}

	@Override
	public void paintMouse(Graphics arg0, Point arg1, Point arg2) {
		Graphics2D g = (Graphics2D) arg0;
		int x, y;
		x = (int) Mouse.getPos().getX();
		y = (int) Mouse.getPos().getY();
		g.setColor(Color.WHITE);
		g.drawLine(x - 5, y, x + 5, y);
		g.drawLine(x, y - 5, x, y + 5);
	}

	public class GUI extends javax.swing.JFrame {

		/**
		 * Creates new form GUI
		 */
		public GUI() {
			initComponents();
		}

		/**
		 * This method is called from within the constructor to initialize the form.
		 * WARNING: Do NOT modify this code. The content of this method is always
		 * regenerated by the Form Editor.
		 */
		@SuppressWarnings("unchecked")
		// <editor-fold defaultstate="collapsed" desc="Generated Code">
		private void initComponents() {

			jLabel1 = new javax.swing.JLabel();
			useStaminas = new javax.swing.JCheckBox();
			startButton = new javax.swing.JButton();
			useMouseKeys = new javax.swing.JCheckBox();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

			jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
			jLabel1.setText("OS Lava Runes");
			jLabel1.setToolTipText("");

			useStaminas.setSelected(true);
			useStaminas.setText("Use Staminas");

			startButton.setText("Start");
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					startButtonActionPerformed(evt);
				}
			});

			useMouseKeys.setText("Use MouseKeys");

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
							layout.createSequentialGroup()
									.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabel1).addGap(110, 110, 110))
					.addGroup(layout.createSequentialGroup().addContainerGap()
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addComponent(useStaminas).addComponent(useMouseKeys))
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
									javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(startButton).addContainerGap()));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup().addContainerGap()
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(layout.createSequentialGroup().addComponent(jLabel1).addGap(18, 18, 18)
											.addComponent(useStaminas)
											.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(useMouseKeys).addGap(0, 2, Short.MAX_VALUE))
									.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
											.addGap(0, 0, Short.MAX_VALUE).addComponent(startButton)))
							.addContainerGap()));

			pack();
		}// </editor-fold>

		private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
			mouseKeys = useMouseKeys.isSelected();
			staminaSupport = useStaminas.isSelected();

			GUI_COMPLETE = true;
		}

		// Variables declaration - do not modify
		private javax.swing.JLabel jLabel1;
		private javax.swing.JButton startButton;
		private javax.swing.JCheckBox useMouseKeys;
		private javax.swing.JCheckBox useStaminas;
		// End of variables declaration
	}

}
