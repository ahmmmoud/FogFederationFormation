import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class GA {

	static ArrayList<CandidateSolution> population = new ArrayList<CandidateSolution>();
	static ArrayList<FogProvider> fp = new ArrayList<FogProvider>();
	static ArrayList<Service> services = new ArrayList<Service>();
	static ArrayList<FogNode> fn = new ArrayList<FogNode>();
	static ArrayList<User> users = new ArrayList<User>();
	static double[][] throughputs = new double[339][5825];
	static double[][] responseTime = new double[339][5825];

	static int GASIZE = 50;

	public static void main(String[] args) throws Exception {
		ArrayList<CandidateSolution> bestCS = new ArrayList<CandidateSolution>();
		ArrayList<Double> avgthpt = new ArrayList<Double>();
		ArrayList<Double> randomThpt = new ArrayList<Double>();

		ArrayList<Double> avgrt = new ArrayList<Double>();
		ArrayList<Double> randomrt = new ArrayList<Double>();

		ArrayList<Double> bestfitness = new ArrayList<Double>();
		ArrayList<Double> randomfitness = new ArrayList<Double>();
		ArrayList<Double> usrsabove = new ArrayList<Double>();
		ArrayList<Double> usrsbelow = new ArrayList<Double>();
		ArrayList<Double> usrsabove2 = new ArrayList<Double>();
		ArrayList<Double> usrsbelow2 = new ArrayList<Double>();
		Random rand = new Random();

		InitializeEnvironment();
		InitializeThroughput();
		InitializeRT();
                
                
		Initialize();
		Sort();
		bestCS.add(population.get(0));
		bestfitness.add(population.get(0).getFitness());
		
		avgthpt.add(population.get(0).getAvgThroughput());
		avgrt.add(population.get(0).getAvgRT());
		
		int index = 0;
		double fitavg = 0;
		for (int i = 0; i < 1000; i++) {
			ArrayList<Integer> rids = new ArrayList<Integer>();
			for (int i1 = 0; i1 < fn.size(); i1++) {
				rids.add(rand.nextInt(services.size()));
			}
			CandidateSolution csr = new CandidateSolution(rids, fn, services);
			fitavg += csr.getFitness();
		}
		randomfitness.add(fitavg / 1000);
		fitavg = 0;
		
		int usersabove;
		int usersbelow;
		int usersabove2;
		int usersbelow2;
		// random solution thpt
		int randomcs;
		int count;
		double avgtp;
		for (int i = 0; i < 200; i++) {
			avgtp = 0;
			count = 0;
			while (count < 10) {
				randomcs = rand.nextInt(51) + 1;
				avgtp += population.get(randomcs).getAvgThroughput();
				count++;
			}
			randomThpt.add(avgtp / 10);
		}

		// random solution RT
		int rndmcandsol;
		int countt;
		double avgrespt;
		for (int i = 0; i < 200; i++) {
			avgrespt = 0;
			countt = 0;
			while (countt < 10) {
				rndmcandsol = rand.nextInt(51) + 1;
				avgrespt += population.get(rndmcandsol).getAvgRT();
				countt++;
			}
			randomrt.add(avgrespt / 10);
		}

		while (index < 200) {
			RemoveWorstHalf();
			Breed();
			Mutate();
			Sort();
			bestCS.add(population.get(0));
			bestfitness.add(population.get(0).getFitness());
			avgthpt.add(population.get(0).getAvgThroughput());
			avgrt.add(population.get(0).getAvgRT());

			for (int i = 0; i < 1000; i++) {
				ArrayList<Integer> rids = new ArrayList<Integer>();
				for (int i1 = 0; i1 < fn.size(); i1++) {
					rids.add(rand.nextInt(services.size()));
				}
				CandidateSolution csr = new CandidateSolution(rids, fn, services);
				fitavg += csr.getFitness();
			}
			randomfitness.add(fitavg / 1000);
			fitavg = 0;
			// % of users whose thpt is below & above thresh
			usersabove = 0;
			usersbelow = 0;
			usersabove2 = 0;
			usersbelow2 = 0;
			population.get(0).decode();
			CandidateSolution last = population.get(0);
			last.decode();
			User user = null;
			for (int i = 0; i < services.size(); i++) {
				for (int j = 0; j < services.get(i).getUl().size(); j++) {
					user = services.get(i).getUl().get(j);
					boolean satisfied = false;
					boolean satisfied2 = false;
					for (int k = 0; k < fn.size(); k++) {
						
						if (services.get(i).getThreshold_thpt() < Throughput.getThroughput(user, user.getService(), throughputs)) {
							usersabove++;
							satisfied = true;
						}
						if (services.get(i).getThreshold_rt() > ResponseTime.getRT(user, user.getService(), responseTime)) {
							usersabove2++;
							satisfied2 = true;
						}
						if(satisfied || satisfied2) {
							break;
						}
					}
					if (!satisfied) {
						usersbelow++;
					}
					if(!satisfied2) {
						usersbelow2++;
					}
				}
			}
			double usersNb = 0.0;
			for (int i = 0; i < services.size(); i++) {
				usersNb += services.get(i).getUl().size();
			}
			usrsabove.add((double) usersabove / usersNb);
			usrsbelow.add((double) usersbelow / usersNb);
			usrsabove2.add((double) usersabove2 / usersNb);
			usrsbelow2.add((double) usersbelow2 / usersNb);
			
			index++;
		}

		// RESULTS
		System.out.println("Throughput");
		System.out.println("Generation\tGeneticApproach\t\tRandomSolution");
		for (int i = 0; i < 200; i++) {
			System.out.println(i + "\t" + avgthpt.get(i) + "\t" + randomThpt.get(i));
		}

		System.out.println("Response Time");
		System.out.println("Generation\tGeneticApproach\t\tRandomSolution");
		for (int i = 0; i < 200; i++) {
			System.out.println(i + "\t" + avgrt.get(i) + "\t" + randomrt.get(i));
		}

		System.out.println("Fitness");
		System.out.println("Generation\tGeneticApproach\t\tRandomSolution");
		for (int i = 0; i < 200; i++) {
			System.out.println(i + "\t" + bestfitness.get(i) + "\t" + randomfitness.get(i));
		}

		System.out.println();
		System.out.println("Users receiving thpt above threshold and below threshold");
		for (int i = 0; i < usrsabove.size(); i++) {
			System.out.println(i + "\t" + usrsabove.get(i) + "\t" + usrsbelow.get(i));
		}
		System.out.println();
		System.out.println("Users receiving rt above threshold and below threshold");
		for (int i = 0; i < usrsabove2.size(); i++) {
			System.out.println(i + "\t" + usrsabove2.get(i) + "\t" + usrsbelow2.get(i));
		}

	}

	private static void InitializeThroughput() throws IOException {
		double thpt = 0;
		int usrid = 0;
		int serviceid = 0;
		File myObj = new File("finalfile.csv");
		Scanner myReader = new Scanner(myObj);
		String d = myReader.nextLine();
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			String[] cols = data.split("\t");
			usrid = Integer.parseInt(cols[0]);
			serviceid = Integer.parseInt(cols[3]);
			thpt = Double.parseDouble(cols[6]);
			throughputs[usrid][serviceid] = thpt;
		}
		myReader.close();
	}

	private static void InitializeRT() throws IOException {
		double rt = 0;
		int uid = 0;
		int sid = 0;
		File myObj = new File("finalwithrt.csv");
		Scanner myRead = new Scanner(myObj);
		String d = myRead.nextLine();
		while (myRead.hasNextLine()) {
			String da = myRead.nextLine();
			String[] column = da.split("\t");
			uid = Integer.parseInt(column[0]);
			sid = Integer.parseInt(column[3]);
			rt = Double.parseDouble(column[6]);
			responseTime[uid][sid] = rt;
		}
		myRead.close();
	}

	private static void Initialize() throws Exception {
		Random rand = new Random();
		for (int i = 0; i < 52; i++) {
			ArrayList<Integer> ids = new ArrayList<Integer>();
			for (int j = 0; j < fn.size(); j++) {
				ids.add(rand.nextInt(services.size()));
			}

			CandidateSolution cs = new CandidateSolution(ids, fn, services);
			population.add(cs);
		}
	}

	private static void InitializeEnvironment() throws FileNotFoundException {

		double avg_thpt = 0.0;
		double avg_rt = 0.0;
		File myObj = new File("finalfile.csv");
		File obj = new File("finalwithrt.csv");
		Scanner myReader = new Scanner(myObj);
		Scanner s = new Scanner(obj);
		String d = myReader.nextLine();
		String k = s.nextLine();

		int x = 0;
		while (x++ < 400) {
			String data = myReader.nextLine();
			String[] cols = data.split("\t");
			String datart = s.nextLine();
			String[] colsrt = datart.split("\t");
			avg_thpt += Double.parseDouble(cols[6]);
			avg_rt += Double.parseDouble(colsrt[6]);
		}
		avg_thpt = avg_thpt / 400;
		avg_rt = avg_rt / 400;

		myReader.close();
		s.close();
		Random rd = new Random();
		for (int j = 0; j < 15; j++) {
			double thresh_thpt = avg_thpt * ((double) rd.nextInt(1100) - 250) / 1000;
			double thresh_rt = avg_rt * ((double) rd.nextInt(1700) - 250) / 1000; // generate rand thresh greater than avg
			Service s1 = new Service(thresh_thpt, thresh_rt);
			services.add(s1);
		}

		int i = 0;
		File myObj2 = new File("finalfile.csv");
		Scanner myReader2 = new Scanner(myObj2);
		String l1 = myReader2.nextLine();

//		File myObj22 = new File("final+rt.csv");
//		Scanner myReader22 = new Scanner(myObj22);
//		String l11 = myReader22.nextLine();

		while (i < 15) {
			String line = myReader2.nextLine();
			String[] cols = line.split("\t");
			float lng = Float.parseFloat(cols[5]);
			float lat = Float.parseFloat(cols[4]);
			int fpid = Integer.parseInt(cols[3]);
			FogProvider fp1 = new FogProvider(lng, lat, fpid);

			for (int j = 0; j < 2; j++) {// check l random number
				FogNode fn1 = new FogNode();
				fn.add(fn1);
				fp1.addFogNode(fn1);
				fp.add(fp1);
			}
			i++;
		}

		myReader2.close();

		File ff = new File("userlist.txt");// read from initial file
		Scanner read = new Scanner(ff);
		int j = 0;
		Random rand = new Random();
		String l2 = read.nextLine();
		String l3 = read.nextLine();
		while (j < 300) {
			String l = read.nextLine();
			String[] cols = l.split("\t");
			float ulat = Float.parseFloat(cols[5]);
			float ulng = Float.parseFloat(cols[6]);
			int randomServiceId = rand.nextInt(services.size());// get random from services array and assign user to
			Service ss = services.get(randomServiceId);// this service and assign the user to the users array inside
														// service object
			User u1 = new User(ss, ulat, ulng);
			ss.ul.add(u1);
			u1.setService(ss);
			users.add(u1);
			j++;
		}
		read.close();
	}

	private static void RemoveWorstHalf() {
		// System.out.println(population.size());
		// mnsheel l worst half
		int tmpSize = population.size() - 1;
		for (int i = population.size() / 2; i < tmpSize + 1; i++) {
			population.remove(population.size() - 1);
		}
	}

	private static void Mutate() throws Exception {
		// alter a random integer from worst 5 candidate solutions
		Random rand = new Random();
		for (int i = 2; i < population.size(); i++) {
			CandidateSolution cs = population.get(i);
			if (cs.wasBest) {
				continue;
			}
			int index = rand.nextInt(services.size());
			int mutateValue = services.get(index).id;
			int mutateIndex = rand.nextInt(cs.ids.size());
			cs.ids.set(mutateIndex, mutateValue);
		}
	}

//	private static void Mutate() {
//		// alter a random integer from worst 5 candidate solutions
//		Random rand = new Random();
//		for (int i = 0; i < population.size() / 2; i++) {
//			CandidateSolution cs = population.get(i);
//			int index = rand.nextInt(services.size());
//			int mutateValue = services.get(index).id;
//			int mutateIndex = rand.nextInt(cs.ids.size());
//			cs.ids.set(mutateIndex, mutateValue);
//		}
//	}

	private static void Breed() throws Exception {
		// take two solutions and crossover the two
		ArrayList<CandidateSolution> newPopulation = new ArrayList<CandidateSolution>();
		// initialize an empty tmp array of CS
		for (int i = 0; i < population.size() - 1; i += 2) {
			CandidateSolution candidateS1 = population.get(i);
			CandidateSolution candidateS2 = population.get(i + 1);
			ArrayList<Integer> A = new ArrayList<>();
			ArrayList<Integer> B = new ArrayList<>();
			for (int z = 0; z < candidateS1.ids.size(); z++) {
				if (z < candidateS1.ids.size() / 2) {
					A.add(candidateS1.ids.get(z));
					B.add(candidateS2.ids.get(z));
				} else {

					B.add(candidateS1.ids.get(z));
					A.add(candidateS2.ids.get(z));
				}
			}
			CandidateSolution CSA = new CandidateSolution(A, fn, services);
			CandidateSolution CSB = new CandidateSolution(B, fn, services);
			// add the CSA and CSB to the new population + l CS1 nad CS2
			newPopulation.add(CSA);
			newPopulation.add(CSB);
			newPopulation.add(candidateS2);
			newPopulation.add(candidateS1);
		}
		population = newPopulation;
	}

	private static void Sort() {
		// according to the throughput ascending
		// sort objects
		Collections.sort(population, new Comparator<CandidateSolution>() {
			int a = 0;

			public int compare(CandidateSolution o1, CandidateSolution o2) {
				if (o2.getFitness() != o1.getFitness()) {
					a = o1.getFitness().compareTo(o2.getFitness());
				} else {
					if (o1.wasBest)
						return -1;
				}
				return a;
			}
		});
		for (CandidateSolution cs : population) {
			cs.wasBest = false;
		}
		population.get(0).wasBest = true;
	}

//	private static void Sort() {
//		// according to the throughput ascending
//		// sort objects
//		Collections.sort(population, new Comparator<CandidateSolution>() {
//			int a = 0;
//
//			public int compare(CandidateSolution o1, CandidateSolution o2) {
//				try {
//
//					a = o1.getFitness().compareTo(o2.getFitness());
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				return a;
//			}
//		});
//
//	}

}
