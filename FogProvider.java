import java.util.ArrayList;

public class FogProvider {
	float lng;
	float lat;
	int id;
	ArrayList <FogNode> fg = new ArrayList<FogNode>();
	
	public FogProvider(float lng, float lat, int id) {
		this.lat=lat;		
		this.lng=lng;
		this.id=id;
	}
	
	public void addFogNode(FogNode fn) {
		
		fg.add(fn);
		fn.fp=this;
                System.out.println("1");
	}
	
	public ArrayList<FogNode> getFg() {
		return fg;
	}

	public void setFg(ArrayList<FogNode> fg) {
		this.fg = fg;
	}

	public boolean hasAvailableFogNode() {
		
		for(int i=0; i<fg.size(); i++) {
			if(fg.get(i)==null)
				return true;
		}
		return false;
	}
	
	public FogNode getAvailableFogNode() {
		for(int i=0; i<fg.size(); i++) {
			if(fg.get(i)==null)
				return fg.get(i);
		}
		return null;
	}
}
