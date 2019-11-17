package scripts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;

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
	public ABCUtil ABCUtil = new ABCUtil();
	public DPathNavigator DPath = new DPathNavigator();
	public boolean GUI_COMPLETE = false;
	public boolean staminaSupport = false;

	@Override
	public void run() {
		GUI GUI = new GUI();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenW = (screenSize.width) / 2;
		int screenH = (screenSize.height) / 2;

		GUI.setVisible(true);

		GUI.setLocation((screenW / 2), (screenH / 2));

		while (!GUI_COMPLETE)
			sleep(100);

		GUI.setVisible(false);

		Mouse.setSpeed(General.random(125, 150));

		println("OS Lava Runecrafter started.");

		while (true) {
			switch (getState()) {
			case USING_STAMINA:
				antiban();
				stamina();
				break;
			case WALKING_TO_BANK:
				antiban();
				walkToBank();
				break;
			case BANKING:
				antiban();
				bank();
				break;
			case TELEPORTING_TO_DUEL_ARENA:
				antiban();
				teleport("Duel Arena");
				break;
			case WALKING_TO_RUINS:
				antiban();
				walkToRuins();
				break;
			case ENTERING_RUINS:
				antiban();
				enterRuins();
				break;
			case WALKING_TO_ALTAR:
				antiban();
				walkToAltar();
				break;
			case CRAFTING_RUNES:
				antiban();
				craftRunes();
				break;
			case TELEPORTING_TO_CASTLE_WARS:
				antiban();
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
		ENTERING_RUINS, WALKING_TO_ALTAR, CRAFTING_RUNES, TELEPORTING_TO_CASTLE_WARS, IDLE;
	}

	private State getState() {
		if (staminaSupport && Game.getRunEnergy() < 30) {
			return State.USING_STAMINA;
		} else if (isAtBank()) {
			if (Inventory.find("Pure essence").length == 0)
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
			RSObject[] ruins = Objects.findNearest(5, "Mysterious ruins");
			if (ruins != null && ruins.length > 0)
				return State.ENTERING_RUINS;
			else {
				RSObject[] altar = Objects.findNearest(5, "Altar");
				if (altar != null && altar.length > 0) {
					if (Inventory.find("Pure essence").length > 0)
						return State.CRAFTING_RUNES;
					else
						return State.TELEPORTING_TO_CASTLE_WARS;
				}
			}
		}
		return State.IDLE;
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
		else {
			Banking.depositAllExcept(bankItems);
			withdrawRing();
			withdrawStamina();
			withdrawRunes();
			withdrawTalisman();
			withdrawEssence();
			Banking.close();
		}
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
			Banking.withdraw(0, "Earth rune");
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
			Banking.withdraw(0, "Pure essence");
			Timing.waitCondition(new Condition() {
				@Override
				public boolean active() {
					sleep(100);
					return Inventory.find("Pure essence").length > 0;
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
	}

	private void walkToBank() {
		if (isAtCastleWars()) {
			if (Player.getPosition().distanceTo(bankChest) > 3) {
				Timing.waitCondition(() -> WebWalking.walkTo(bankChest) && bankChest.adjustCameraTo(), 3000);
			}
		}
	}

	private void walkToRuins() {
		if (Player.getPosition().distanceTo(mysteriousRuins) > 3) {
			Timing.waitCondition(() -> WebWalking.walkTo(mysteriousRuins) && mysteriousRuins.adjustCameraTo(), 8000);
		}
	}

	private void walkToAltar() {
		RSTile altar = new RSTile(fireAltar.getX() + General.random(-1, 1), fireAltar.getY() + General.random(-1, 1));
		if (Player.getPosition().distanceTo(altar) > 3) {
			Inventory.open();
			Timing.waitCondition(() -> WebWalking.walkTo(altar) && altar.adjustCameraTo(), 8000);
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
	}

	public void antiban() {
		if (this.ABCUtil.shouldCheckTabs()) {
			println("[ABC2] Checking random tab");
			this.ABCUtil.checkTabs();
		}
		if (this.ABCUtil.shouldCheckXP()) {
			println("[ABC2] Checking XP");
			this.ABCUtil.checkXP();
		}
		if (this.ABCUtil.shouldExamineEntity()) {
			println("[ABC2] Examining random entity");
			this.ABCUtil.examineEntity();
		}
		if (this.ABCUtil.shouldMoveMouse()) {
			println("[ABC2] Moving mouse");
			this.ABCUtil.moveMouse();
		}
		if (this.ABCUtil.shouldPickupMouse()) {
			println("[ABC2] Picking up mouse");
			this.ABCUtil.pickupMouse();
		}
		if (this.ABCUtil.shouldRightClick()) {
			println("[ABC2] Random right click");
			this.ABCUtil.rightClick();
		}
		if (this.ABCUtil.shouldRotateCamera()) {
			println("[ABC2] Rotating camera");
			this.ABCUtil.rotateCamera();
		}
		if (this.ABCUtil.shouldLeaveGame()) {
			println("[ABC2] Moving mouse off screen");
			this.ABCUtil.leaveGame();
		}
	}

	protected boolean performHumanActions() {
		ABCUtil.moveMouse();
		ABCUtil.examineEntity();
		ABCUtil.rightClick();
		return true;
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

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
							layout.createSequentialGroup()
									.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(jLabel1).addGap(110, 110, 110))
					.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(useStaminas)
							.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
									javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(startButton).addContainerGap()));
			layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(18, 18, 18)
							.addComponent(useStaminas)
							.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
							layout.createSequentialGroup()
									.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(startButton).addContainerGap()));

			pack();
		}// </editor-fold>

		private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
			GUI_COMPLETE = true;

			staminaSupport = useStaminas.isSelected();
		}

		// Variables declaration - do not modify
		private javax.swing.JLabel jLabel1;
		private javax.swing.JButton startButton;
		private javax.swing.JCheckBox useStaminas;
		// End of variables declaration
	}

}
