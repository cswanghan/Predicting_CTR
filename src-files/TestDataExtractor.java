import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestDataExtractor {

	File file;

	int totalLines = 0;

	//users
	int male = 0;
	int female = 0;
	int unknown = 0;
	// index 0 holds number of people ages 0-12
	int[] ages = new int[6];
	int[] advertisers = new int[7000000];
	//ArrayList<Integer> advertisers = new ArrayList<Integer>();
	public TestDataExtractor() {
//		for (int i = 0; i < advertisers.length; i++) {
//			
//		}
	}


	public void parseFile(String filename) throws IOException {
		file = new File(filename);
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			totalLines++;
			parseLine(line, filename);
		}
		//writeUsers();
		writeTraining();
	}

	public void parseLine(String line, String filename) throws IOException {
		if (filename.equals("userid_profile.txt")) {
			parseUsers(line);
		}
		else if (filename.equals("training.txt")) {
			parseTraining(line);
		}
		
	}
	
	public void parseTraining(String line) {
		Scanner scan = new Scanner(line);
		String token = scan.next();
		token = scan.next();
		token = scan.next();
		token = scan.next();
		// the 5th token is the AdvertiserID
		token = scan.next();
		int index = Integer.valueOf(token);
		int updatedVal = advertisers[index];
		updatedVal++;
		advertisers[index] = updatedVal;
		//System.out.println(index + " : " + advertisers[index] + " " + updatedVal);
	}
	
	public void parseUsers(String line) throws IOException {
		Scanner scan = new Scanner(line);
		String id = scan.next();
		String gender = scan.next();
		if (gender.equals("1")) {
			male++;
		}
		else if (gender.equals("2")) {
			female++;
		}
		else {
			unknown++;
		}
		String ageid = scan.next();
		if (ageid.equals("1")) {
			ages[0]++;
		}
		else if (ageid.equals("2")) {
			ages[1]++;
		}
		else if (ageid.equals("3")) {
			ages[2]++;
		}
		else if (ageid.equals("4")) {
			ages[3]++;
		}
		else if (ageid.equals("5")) {
			ages[4]++;
		}
		else if (ageid.equals("6")) {
			ages[5]++;
		}
	}

	public void writeUsers() throws IOException {
		FileWriter output = null;
		output = new FileWriter("User_Results.txt");
		BufferedWriter writer = new BufferedWriter(output);
		
		writer.write("Number of female users : " + female + "\n");
		writer.write("Number of male users : " + male + "\n");
		writer.write("Number of users ages (0, 12]: " + ages[0] + "\n");
		writer.write("Number of users ages (12, 18]: " + ages[1] + "\n");
		writer.write("Number of users ages (18, 24]: " + ages[2] + "\n");
		writer.write("Number of users ages (24, 30]: " + ages[3] + "\n");
		writer.write("Number of users ages (30,  40]: " + ages[4] + "\n");
		writer.write("Number of users ages 40+: " + ages[5] + "\n");
		
		writer.close();
		output.close();

	}
	
	public void writeTraining() throws IOException {
		FileWriter output = null;
		output = new FileWriter("Training_Results.txt");
		BufferedWriter writer = new BufferedWriter(output);
		writer.write("Number of sessions : " + totalLines + "\n");
		writer.write("\n");
		int totalAdvs = 0;
		for (int i = 0; i < advertisers.length; i++) {
			if (advertisers[i] != 0) {
				writer.write("AdvertiserID : " + i + " has appeared " + advertisers[i] + " times" + "\n");
				totalAdvs++;
			}
		}
		writer.write("\n");
		writer.write("Total number of Advertisers : " + totalAdvs + "\n");
		writer.close();
		output.close();
	}
}