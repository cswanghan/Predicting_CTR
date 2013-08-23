import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class FirstModel {

	private HashMap<String, Integer> impressionCounts;
	private HashMap<String, Integer> clickCounts;

	HashMap<Integer, ArrayList<String>> keywordMap = new HashMap<Integer, ArrayList<String>>();
	HashMap<Integer, ArrayList<String>> adIDToKeyword = new HashMap<Integer, ArrayList<String>>();
	
	HashMap<Integer, ArrayList<String>> keywordToAd = new HashMap<Integer, ArrayList<String>>();
	HashMap<Integer, ArrayList<Double>> keywordToAdsCTR = new HashMap<Integer, ArrayList<Double>>();
	
	
	double ctr = 0.0;
	int totalLines = 0;
	double avgCTR = 0.0; 
	
	public FirstModel() {
		impressionCounts = new HashMap<String, Integer>();
		clickCounts = new HashMap<String, Integer>();
	}

	public void scanFile(String filename) throws IOException {
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int i = 0;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			//System.out.println(line);
			scanLineForKeywordID(line);
			totalLines++;
			System.out.println(totalLines);
		}
		avgCTR = (double)ctr / (double)totalLines;
		System.out.println(avgCTR);
	}
	
	public void scanNewTest() throws FileNotFoundException {
		File file = new File("newUserTest.txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			scanNewTestLine(line);
		}
	}
	public void scanNewTestLine(String line) {
		Scanner scanner = new Scanner(line);

		int adID = 0;

		for (int i = 0; i < 10; i++) {
			if (!scanner.hasNext()) {
				continue;
			}
			String key = scanner.next();
			//adid
			if (i == 1) {
				adID = Integer.valueOf(key);
			}
			// KeywordID
			if (i == 6) {
				// System.out.println(key);
				ArrayList<String> tokenList = keywordToAd.get(Integer
						.valueOf(key));
				if (tokenList == null) {
					ArrayList<String> ads = new ArrayList<String>();
					ads.add(String.valueOf(adID));
					keywordToAd.put(Integer.valueOf(key), ads);
				}
				else {
					tokenList.add(String.valueOf(adID));
					keywordToAd.put(Integer.valueOf(key), tokenList);
				}
			}
		}
	}
	
	public void write() throws IOException {
		FileWriter output = null;
		output = new FileWriter("FMResults.txt");
		BufferedWriter writer = new BufferedWriter(output);
		Iterator iter = keywordToAd.keySet().iterator();
		while(iter.hasNext()) {
			int keyword = (int) iter.next();
			double ctr = computeTermCTR(keyword);
			if (String.valueOf(ctr).contains("E")) {
				continue;
			}
			writer.write(ctr + "\n");
		}
		writer.write("\n");

		writer.close();
		output.close();
	}

	public void rescanForCTR(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			computeAvgCTR(line);
		}
	}

	public void parseKeyword() throws FileNotFoundException {
		File file = new File("purchasedkeywordid_tokensid.txt");
		Scanner scanner = new Scanner(file);
		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Scanner scan = new Scanner(line);
			int id = Integer.valueOf(scan.next());
			String tokens = scan.next();
			System.out.println(id + " : " + tokens);

			ArrayList<String> tokenList = parseAdSingleTokens(tokens);
			keywordMap.put(id, tokenList);
			i++;
		}
	}

	// training.txt
	public void mapAdToKeyword() throws FileNotFoundException {
		File file = new File("training.txt");
		Scanner scanner = new Scanner(file);
		Integer adID = 0;
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Scanner scan = new Scanner(line);
			for (int i = 0; i < 12; i++) {
				String key = scan.next();
				if (i == 3) {
					adID = Integer.valueOf(key);
					
				}
				if (i == 8) {
					// this not unique
					ArrayList<String> keywords = keywordMap.get(Integer.valueOf(key));
					adIDToKeyword.put(adID, keywords);
					System.out.println(adID + " " + key + " " + keywords); 
				}
			}
		}

	}
	public void scanLineForKeywordID(String line) {
		Scanner scanner = new Scanner(line);
		int click = 0;
		int impression = 0;
		int adID = 0;
		// traverse row items
		double adCTR = 0.0;
		for (int i = 0; i < 12; i++) {
			if (!scanner.hasNext()) {
				continue;
			}
			String key = scanner.next();
			// Click
			if (i == 0) {
				click = Integer.valueOf(key);
			}
			// Impression
			if (i == 1) {
				impression = Integer.valueOf(key);
				double result = (double) click / (double) impression;
				ctr += result;
				adCTR = result;
			}
			if (i == 3) {
				adID = Integer.valueOf(key);
			}
			// KeywordID
			if (i == 7) {
				ArrayList<String> tokenList = keywordToAd.get(Integer
						.valueOf(key));
				ArrayList<Double> tokenList2 = keywordToAdsCTR.get(Integer
						.valueOf(key));
				if (tokenList == null) {
					ArrayList<String> ads = new ArrayList<String>();
					ads.add(String.valueOf(adID));
					keywordToAd.put(Integer.valueOf(key), ads);
					ArrayList<Double> ctrs = new ArrayList<Double>();
					ctrs.add(adCTR);
					keywordToAdsCTR.put(Integer.valueOf(key), ctrs);
				}
				else {
					tokenList.add(String.valueOf(adID));
					tokenList2.add(adCTR);
					keywordToAd.put(Integer.valueOf(key), tokenList);
					keywordToAdsCTR.put(Integer.valueOf(key), tokenList2);
				}
			}
		}
	}

	public ArrayList<String> parseAdSingleTokens(String token) {
		char[] array = token.toCharArray();
		ArrayList<String> tokenList = new ArrayList<String>();
		String current = "";
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			if (c != '|') {
				current += c;
			} else if (c == '|') {
				tokenList.add(current);
				current = "";
			}
		}
		tokenList.add(current);
		return tokenList;
	}

	
	public void computeAvgCTR(String line) {
		Iterator iter = (Iterator) keywordMap.keySet().iterator();
		double totalImpressions = 0.0;
		double totalClicks = 0.0;
		while (iter.hasNext()) {
			int key = (int) iter.next();
			ArrayList<String> tokenList = keywordMap.get(key);
			for (int i = 0; i < tokenList.size(); i++) {
				int currentImp = impressionCounts.containsKey(tokenList.get(i)) ? impressionCounts
						.get(i) : 0;
				int currentClick = clickCounts.containsKey(tokenList.get(i)) ? clickCounts
						.get(i) : 0;
				totalImpressions += currentImp;
				totalClicks += currentClick;
			}
			System.out.println("CTR: " + (double) totalClicks
					/ totalImpressions);
		}
	}
	
	public int computeNTerm(int keyword) {
		ArrayList ads = keywordToAd.get(keyword);
		return ads.size();
	}
	
	public double ctrTerm(int keyword) {
		double ctr = 0.0;
		//if (keywordTo)
		ArrayList<Double> ads = keywordToAdsCTR.get(keyword);
		for (int i = 0; i < ads.size(); i++) {
			ctr += ads.get(i);
		}
		return (double) ctr / ads.size();
	}
	
	public double computeTermCTR(int keyword) {
		double termCTR = 0.0;
		double nTerm = computeNTerm(keyword);
		double ctrTerm = ctrTerm(keyword);
		double numerator = (double) avgCTR + (double)(nTerm * ctrTerm);
		double denominator = 1 + nTerm;
		double result = (double) numerator / (double) denominator;
		//double ctr = (double) 1 / (double)(1 + Math.pow(Math.E, -result));
		return result;
	}

	public void dumpKeywordMap() {
		Iterator iter = keywordToAd.keySet().iterator();
		System.out.println("Keyword to adID");
		int i = 0;
		while(iter.hasNext()) {
			i++;
			int key = (int)iter.next();
			System.out.println(key + ", " + keywordToAd.get(key));
		}
	}
	public static void main(String[] args) throws IOException {
		TestDataExtractor dt = new TestDataExtractor();
		//dt.parseFile("training.txt");
		FirstModel fm = new FirstModel();
		//this maps KeywordID to all AdID
		
		fm.scanFile("training.txt");
		//fm.scanNewTest();
		fm.write();
		//fm.dumpKeywordMap();
	}
}
