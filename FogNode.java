
public class FogNode {
	Service service;
	static int _id;
	int id;
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public static int get_id() {
		return _id;
	}

	public static void set_id(int _id) {
		FogNode._id = _id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public FogProvider getFp() {
		return fp;
	}

	public void setFp(FogProvider fp) {
		this.fp = fp;
	}

	FogProvider fp;
	public FogNode() {
		
		this.id=_id++;
		
	}
	
	public void assignService(Service service) {
		this.service=service;
		service.fn=this;
	}
	
	
}
