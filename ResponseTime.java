
public class ResponseTime {
//	put ratio alpha= 0.5 * nb of invoc above thresh of rt + 0.5 * nb of invoc below throughput 
//			(rt less than thresh+thpt below thresh)/2
	
	public static double getRT(User u, Service s, double[][] rt) throws Exception {
		double max = 0.0;
		// loop over invocations array
		for (int j = 0; j < GA.fp.size(); j++) {
			for (int i = 0; i < GA.fp.get(j).fg.size(); i++) {
				if (GA.fp.get(j).fg.get(i).service == s && rt[u.usrId][GA.fp.get(j).id] > max) {
						max = rt[u.usrId][GA.fp.get(j).id];
				}
			}
		}
		return max;
	}

}
