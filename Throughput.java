
public class Throughput {
	// static ArrayList<Invocation> invocations= new ArrayList<Invocation>();
	// initialize throughput should fill up the invocations array
	// get throughput should return the value from the array according to the params
	public static double getThroughput(User u, Service s, double[][] throughput) throws Exception {
		double max = 0.0;
		// loop over invocations array
		for (int j = 0; j < GA.fp.size(); j++) {
			for (int i = 0; i < GA.fp.get(j).fg.size(); i++) {
				if (GA.fp.get(j).fg.get(i).service == s && throughput[u.usrId][GA.fp.get(j).id] > max) {
						max = throughput[u.usrId][GA.fp.get(j).id];
				}
			}
		}
		return max;
	}
}
