import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class Baseline {
	final int DATA_ELEMENTS = 12;
	private HashMap<String, Integer> impressionCounts;
	private HashMap<String, Integer> clickCounts;
	
	HashMap<Integer, ArrayList<String>> titleMap = new HashMap<Integer, ArrayList<String>>();
	HashMap<Integer, ArrayList<String>> keywordMap = new HashMap<Integer, ArrayList<String>>();
	public Baseline() {
		impressionCounts = new HashMap<String, Integer>();
		clickCounts = new HashMap<String, Integer>();
	}
	
	public void testAdsTitle() throws FileNotFoundException {
		// populate appropriate HashMap
		parseFeature("titleid_tokensid", titleMap);
		scanFile("training.txt", "title");
		//dumpImpressions();
		//dumpClicks();
		computeAvgCTR();
	}
	// Scans the text file("training.txt") and for each line, toScan denotes
	// the token to be scanned and create list of tokens
	// currently only testing for first 10 lines, so CTR should be 0
	public void scanFile(String filename, String toScan) throws FileNotFoundException {
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int i = 0;
		// && i < 10
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			System.out.println(line);
			if (toScan == "title") {
				scanLineForTitle(line);
			}
			i++;
		}
	}
	
	//
	public void rescanForCTR(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int i = 0;
		// && i < 10
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			computeAvgCTR();
		}
	}
	
	//populates hashmap with id as key and an ArrayList of integers as the value
	public void parseFeature(String filename, HashMap map) throws FileNotFoundException {
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int i = 0;
		//&& i < 30
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			Scanner scan = new Scanner(line);
			int id = Integer.valueOf(scan.next());
			String tokens = scan.next();
			System.out.println(id + " : " + tokens);
			
			ArrayList<String> tokenList = parseSingleTokens(tokens);
			map.put(id, tokenList);
			i++;
		}
	}
	

	
	// parses lines such as "615|1545|75|31|1|138|1270|615|131"
	// into an ArrayList which is returned to be used for
	// the mapping, "|" acts as the delimiter
	public ArrayList<String> parseSingleTokens(String token) {
		char[] array = token.toCharArray();
		ArrayList<String> tokenList = new ArrayList<String>();
		String current = "";
		for (int i = 0; i < array.length; i++) {
			char c = array[i];
			if (c != '|') {
				current += c;
			}
			else if (c == '|') {
				tokenList.add(current);
				current = "";
			}
		}
		//adds the last item 
		tokenList.add(current);
		return tokenList;
	}
	
	// scans each line, extract content of each column
	public void scanLineForTitle(String line) {
		Scanner scanner = new Scanner(line);  
		int click = 0;
		int impression = 0;
		// traverse row items
		for (int i = 0; i < DATA_ELEMENTS; i++) {
			String key = scanner.next();
			// 	Click
			if (i == 0) {
				click = Integer.valueOf(key);
			}
			// Impression
			if (i == 1) {
				impression = Integer.valueOf(key);
			}
			//	TitleID
			if (i == 9) {
				//System.out.println(key);
				ArrayList<String> tokenList = titleMap.get(Integer.valueOf(key));
				if (tokenList == null) {
					continue;
				}
				System.out.println(key + " : " + tokenList.size());
				updateImpClickCounts(tokenList, click, impression);
				System.out.println(clickCounts.size() + ", " + impressionCounts.size());
			}
		}
	}
	
	public void updateImpClickCounts(ArrayList<String> tokenList, int click, int impression) {
		for (int i = 0; i < tokenList.size(); i++) {
			String key = tokenList.get(i);
			if (impressionCounts.containsKey(key)) {
				int newCount = impressionCounts.get(key);
				newCount += impression;
				impressionCounts.put(key, newCount);
			}
			else if (!impressionCounts.containsKey(key)) {
				impressionCounts.put(key, impression);
			}
			if (clickCounts.containsKey(key)) {
				int newCount = clickCounts.get(key);
				newCount += click;
				clickCounts.put(key, newCount);
			}
			else if (!clickCounts.containsKey(key)) {
				clickCounts.put(key, click);
			}
		}
	}
	
	public void computeAvgCTR() {
		Iterator iter = (Iterator) titleMap.keySet().iterator();
		double totalImpressions = 0.0;
		double totalClicks = 0.0;
		while (iter.hasNext()) {
			int key = (int) iter.next();
			ArrayList<String> tokenList = titleMap.get(key);
			int currentImp = 0;
			int currentClick = 0;
			for (int i = 0; i < tokenList.size(); i++) {
				//System.out.println(impressionCounts);
				//System.out.println(clickCounts);
				if (impressionCounts.containsKey(tokenList.get(i))) {
					currentImp = impressionCounts.get(tokenList.get(i));
				}
				if (clickCounts.containsKey(tokenList.get(i))) {
					currentClick = clickCounts.get(tokenList.get(i));
				}
				totalImpressions += currentImp;
				totalClicks += currentClick;
			}
			System.out.println(key + " CTR: " + (double)totalClicks/ totalImpressions);
		}
	}
	
	// for debugging prints impressionCounts
	public void dumpImpressions() {
		Iterator iter = impressionCounts.keySet().iterator();
		System.out.println("Impression Counts");
		while(iter.hasNext()) {
			String key = (String)iter.next();
			System.out.println(key + ", " + impressionCounts.get(key));
		}
	}
	// for debugging prints clickCounts
	public void dumpClicks() {
		Iterator iter = clickCounts.keySet().iterator();
		System.out.println("Click Counts");
		while(iter.hasNext()) {
			String key = (String)iter.next();
			System.out.println(key + ", " + clickCounts.get(key));
		}
	}
}
