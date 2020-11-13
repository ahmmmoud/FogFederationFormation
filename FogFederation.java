import java.util.ArrayList;

public class FogFederation {
	ArrayList <FogNode> fg = new ArrayList<FogNode>();
	int id;
	
	public FogFederation(int id) {
		this.id=id;
	}
	
	public void addFogNode(FogNode fn) {
		
		fg.add(fn);
	}

	public ArrayList<FogNode> getFg() {
		return fg;
	}

	public void setFg(ArrayList<FogNode> fg) {
		this.fg = fg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
