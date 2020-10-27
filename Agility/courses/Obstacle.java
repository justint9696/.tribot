package scripts.Agility.courses;

import org.tribot.api.Timing;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;

import scripts.Agility.utils.Node;

public abstract class Obstacle implements Node {

	private int id;
	private String action;
	private String name;

	public Obstacle(int id, String action, String name) {
		this.id = id;
		this.action = action;
		this.name = name;
	}

	public abstract boolean validate();

	public void interact() {
		RSObject[] objects = Objects.find(20, Filters.Objects.idEquals(this.id)
				.and(Filters.Objects.actionsEquals(this.action).and(Filters.Objects.nameEquals(this.name))));
		if (objects.length > 0) {
			RSObject obstacle = objects[0];
			if (Player.getPosition().distanceTo(obstacle) > 5 && !Player.isMoving()) {
				Walking.blindWalkTo(obstacle);
			} else {
				if (obstacle.isClickable()) {
					if (obstacle.hover()) {
						if (obstacle.click(this.action)) {
							if (Timing.waitCondition(() -> Player.isMoving(), 3000)) {
								if (Timing.waitCondition(() -> Player.getAnimation() != -1, 6000)) {
									Timing.waitCondition(() -> Player.getAnimation() == -1, 6000);
								}
							}
						}
					}
				} else {
					obstacle.adjustCameraTo();
				}
			}
		} else
			return;
	}

	public RSObject getObstacle() {
		RSObject[] obstacle = Objects.find(20, Filters.Objects.idEquals(this.id)
				.and(Filters.Objects.actionsEquals(this.action).and(Filters.Objects.nameEquals(this.name))));
		return obstacle.length == 0 ? null : obstacle[0];
	}
}
