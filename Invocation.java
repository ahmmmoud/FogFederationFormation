
public class Invocation {
	int userID;
	int fogProviderID;
	double throughput;
	double rt;
	public double getRt() {
		return rt;
	}
	public void setRt(double rt) {
		this.rt = rt;
	}
	public Invocation() {
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getFogProviderID() {
		return fogProviderID;
	}
	public void setFogProviderID(int serviceID) {
		this.fogProviderID = serviceID;
	}
	public double getThroughput() {
		return throughput;
	}
	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}
	
	
}
