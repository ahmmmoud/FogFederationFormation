import java.util.ArrayList;
import java.util.Random;

public class Service {
	Random rand = new Random();
	int id;
	static int _id;
	ArrayList<User> ul = new ArrayList<User>();
	FogNode fn;
	double threshold_thpt;
	double threshold_rt;
	int fed;

	public Service(double threshold_thpt, double threshold_rt) {
		this.threshold_thpt = threshold_thpt;
		this.threshold_rt = threshold_rt;
		this.id = _id++;
		this.fed = rand.nextInt(5);
                System.out.println(this.fed);
	}

	public void addUser(User u) {
		ul.add(u);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static int get_id() {
		return _id;
	}

	public static void set_id(int _id) {
		Service._id = _id;
	}

	public ArrayList<User> getUl() {
		return ul;
	}

	public void setUl(ArrayList<User> ul) {
		this.ul = ul;
	}

	public FogNode getFn() {
		return fn;
	}

	public void setFn(FogNode fn) {
		this.fn = fn;
	}

	public double getThreshold_thpt() {
		return threshold_thpt;
	}

	public void setThreshold_thpt(double threshold_thpt) {
		this.threshold_thpt = threshold_thpt;
	}

	public double getThreshold_rt() {
		return threshold_rt;
	}

	public void setThreshold_rt(double threshold_rt) {
		this.threshold_rt = threshold_rt;
	}

	public int getFed() {
		return fed;
	}

	public double evaluate() throws Exception {
//		put ratio alpha= 0.5 * nb of invoc above thresh of rt + 0.5 * nb of invoc below throughput 

		double tmp = 0;
		int nthpt = 0;
		int nrt = 0;
		for (int i = 0; i < ul.size(); i++) {
			double userThpt = Throughput.getThroughput(ul.get(i), this, GA.throughputs);
			double userRT = ResponseTime.getRT(ul.get(i), this, GA.responseTime);
			if (threshold_thpt > userThpt) {
				nthpt++;
			}
			if (threshold_rt < userRT) {
				nrt++;
			}
		}
		tmp = 0.5 * nthpt + 0.5 * nrt;
		return tmp;
	}
}
