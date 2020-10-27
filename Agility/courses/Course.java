package scripts.Agility.courses;

import java.util.Arrays;
import java.util.List;

import scripts.Agility.utils.Node;
import scripts.Agility.nodes.*;

public enum Course {
	GNOME(
			new scripts.Agility.courses.gnome.Log(),
			new scripts.Agility.courses.gnome.NetOne(),
			new scripts.Agility.courses.gnome.BranchOne(),
			new scripts.Agility.courses.gnome.Rope(),
			new scripts.Agility.courses.gnome.BranchTwo(),
			new scripts.Agility.courses.gnome.NetTwo(),
			new scripts.Agility.courses.gnome.Pipe(),
			new Run()
			),
	DRAYNOR(
			new scripts.Agility.courses.draynor.WallOne(),
			new scripts.Agility.courses.draynor.RopeOne(),
			new scripts.Agility.courses.draynor.RopeTwo(),
			new scripts.Agility.courses.draynor.WallTwo(),
			new scripts.Agility.courses.draynor.WallThree(),
			new scripts.Agility.courses.draynor.Gap(),
			new scripts.Agility.courses.draynor.Crate(),
			new Run(),
			new Heal()
			);
	
	List<Node> nodes;
	
	Course(Node... nodes) {
		this.nodes = Arrays.asList(nodes);
	}
	
	public List<Node> getNodeSet() {
		return this.nodes;
	}
}
