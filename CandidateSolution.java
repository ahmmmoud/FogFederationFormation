import java.util.ArrayList;

public class CandidateSolution {
	ArrayList<Service> s;
	ArrayList<Integer> ids;
	ArrayList<FogNode> fn;
	Double fitness;
	double avgThroughput = 0;
	double avgRT = 0;
	boolean wasBest=false;
	// double avgthresh=0;

	public CandidateSolution(ArrayList<Integer> ids, ArrayList<FogNode> fn, ArrayList<Service> services)
			throws Exception {
		this.ids = ids;
		this.fn = fn;
		this.s = services;
		decode();
		fitness = calculateFitness(ids);
		avgThroughput = calculateAverageThroughput2(ids);
		avgRT=calculateAverageRT2(ids);
	}

	public void decode() throws Exception {
		for (FogNode f : fn) {
			f.service = null;
		}
		for (int i = 0; i < ids.size(); i++) {
			fn.get(i).assignService(getServiceById(ids.get(i)));
		}
	}

//	public void decode() throws Exception {
//		for (int i = 0; i < ids.size(); i++) {
//			fn.get(i).assignService(getServiceById(ids.get(i)));
//		}
//	}

//	public double calculateAverageThreshold(ArrayList<Integer> ids) throws Exception {
//		//avgthresh = 0.0;
//		double tmp = 0.0;
//		int numOfUsers = 0;
//		for (int i = 0; i < s.size(); i++) {
//			if (ids.contains(s.get(i).id)) {
//				for (int j = 0; j < s.get(i).ul.size(); j++) {
//					tmp += s.get(i).getThreshold();
//				}
//			}
//		}
//
//		for (int i = 0; i < GA.services.size(); i++) {
//			numOfUsers += GA.services.get(i).ul.size();
//		}
//		avgthresh = tmp / numOfUsers;
//		return avgthresh;
//	}

	public double calculateAverageThroughput2(ArrayList<Integer> ids) throws Exception {
		avgThroughput = 0.0;
		double tmp = 0.0;
		int numOfUsers = 0;
		for (int i = 0; i < s.size(); i++) { // 1 3 3 4
			if (ids.contains(s.get(i).id)) {
				for (int j = 0; j < s.get(i).ul.size(); j++) {
					tmp += Throughput.getThroughput(s.get(i).ul.get(j), s.get(i), GA.throughputs);
				}
			}
		}

		for (int i = 0; i < GA.services.size(); i++) {
			numOfUsers += GA.services.get(i).ul.size();
		}
		avgThroughput = tmp / numOfUsers;
		return avgThroughput;
	}
	
	public double calculateAverageRT2(ArrayList<Integer> ids) throws Exception {
		avgRT = 0.0;
		double tmp = 0.0;
		int numOfUsers = 0;
		for (int i = 0; i < s.size(); i++) { // 1 3 3 4
			if (ids.contains(s.get(i).id)) {
				for (int j = 0; j < s.get(i).ul.size(); j++) {
					tmp += ResponseTime.getRT(s.get(i).ul.get(j), s.get(i), GA.responseTime);
				}
			}
		}

		for (int i = 0; i < GA.services.size(); i++) {
			numOfUsers += GA.services.get(i).ul.size();
		}
		avgRT = tmp / numOfUsers;
		return avgRT;
	}

//	public double getAvgthresh() {
//		return avgthresh;
//	}
//
//	public void setAvgthresh(double avgthresh) {
//		this.avgthresh = avgthresh;
//	}

	public double getAvgThroughput() {
		return avgThroughput;
	}

	public void setAvgThroughput(double avgThroughput) {
		this.avgThroughput = avgThroughput;
	}

	public ArrayList<Service> getS() {
		return s;
	}

	public void setS(ArrayList<Service> s) {
		this.s = s;
	}

	public ArrayList<Integer> getIds() {
		// System.out.println("size " + ids.size());
		return ids;
	}

	public void setIds(ArrayList<Integer> ids) {
		this.ids = ids;
	}

	public ArrayList<FogNode> getFn() {
		return fn;
	}

	public void setFn(ArrayList<FogNode> fn) {
		this.fn = fn;
	}

	public Double getFitness() {
		return fitness;
	}

	public void setFitness(Double fitness) {
		this.fitness = fitness;
	}

	public Double calculateFitness(ArrayList<Integer> ids) throws Exception {
		fitness = 0.0; 
		for (int i = 0; i < s.size(); i++) {
			fitness += s.get(i).evaluate();
		}
		return fitness;
	}

	public Service getServiceById(int id) throws Exception {
		for (int i = 0; i < s.size(); i++) {
			if (id == s.get(i).id) {
				return s.get(i);
			}
		}
		throw new Exception("Id not found");
	}

	public double getAvgRT() {
		return avgRT;
	}

	public void setAvgRT(double avgRT) {
		this.avgRT = avgRT;
	}
}
