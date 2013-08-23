import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: zzhou
 * Date: 2/28/13
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSetGenerator {

    private Set<Long> userSet;

    private Set<Long> descriptionSet;

    private Set<Long> purchasedKeywordSet;

    private Set<Long> querySet;

    private Set<Long> titleSet;


    private Set<Long> testSet1;
    private Set<Long> testSet2;
    private Set<Long> testSet3;

    private Set<Long> trainUserSet;

    private Set<Long> trainAdvertiserSet;

    private Set<Long> existingUserSet;

    public DataSetGenerator() {
        trainUserSet = new HashSet<Long>();
        trainAdvertiserSet = new HashSet<Long>();

        userSet = new HashSet<Long>();
        descriptionSet = new HashSet<Long>();
        purchasedKeywordSet = new HashSet<Long>();
        querySet = new HashSet<Long>();
        titleSet = new HashSet<Long>();

        existingUserSet = new HashSet<Long>();

        testSet1 = new HashSet<Long>();
        testSet2 = new HashSet<Long>();
        testSet3 = new HashSet<Long>();

        loadUsers();

    }

    public void loadUsers() {

        String fileName = "/media/data4/zzhou/kddcup/track2/userid_profile.txt";

        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = new String();
            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Long userId = Long.valueOf(strArray[0]);
                existingUserSet.add(userId);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("existing users:" + existingUserSet.size());
    }

    public void sampleTraining(String fileName) {
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = new String();
            Random generator = new Random(19580427);
            String file = "/media/data4/zzhou/kddcup/project/training.txt";

            FileOutputStream fstream = new FileOutputStream(file);
            BufferedWriter fileOut = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Long userId = Long.valueOf(strArray[11]);
                Long advertiserId = Long.valueOf(strArray[4]);

                Long queryId = Long.valueOf(strArray[7]);
                Long keywordId = Long.valueOf(strArray[8]);
                Long titleId = Long.valueOf(strArray[9]);
                Long descriptionId = Long.valueOf(strArray[10]);

                if (generator.nextDouble() < 0.1 && existingUserSet.contains(userId)) {
                    trainUserSet.add(userId);
                    trainAdvertiserSet.add(advertiserId);

                    userSet.add(userId);
                    descriptionSet.add(descriptionId);
                    purchasedKeywordSet.add(keywordId);
                    querySet.add(queryId);
                    titleSet.add(titleId);
                    fileOut.write(readPerLine + "\n");
                }
            }
            bufferReader.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void procTesting(String fileName) {
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = new String();
            String file1 = "/media/data4/zzhou/kddcup/project/test/newTest.txt";
            String file2 = "/media/data4/zzhou/kddcup/project/test/newUserTest.txt";
            String file3 = "/media/data4/zzhou/kddcup/project/test/newAdvTest.txt";

            FileOutputStream fstream1 = new FileOutputStream(file1);
            BufferedWriter fileOut1 = new BufferedWriter(new OutputStreamWriter(fstream1, "UTF-8"));

            FileOutputStream fstream2 = new FileOutputStream(file2);
            BufferedWriter fileOut2 = new BufferedWriter(new OutputStreamWriter(fstream2, "UTF-8"));

            FileOutputStream fstream3 = new FileOutputStream(file3);
            BufferedWriter fileOut3 = new BufferedWriter(new OutputStreamWriter(fstream3, "UTF-8"));

            Long lineCounter = 0L;
            while ((readPerLine = bufferReader.readLine()) != null) {
                lineCounter++;
                String[] strArray = readPerLine.split("\t");
                Long userId = Long.valueOf(strArray[9]);
                Long advertiserId = Long.valueOf(strArray[2]);

                Long queryId = Long.valueOf(strArray[5]);
                Long keywordId = Long.valueOf(strArray[6]);
                Long titleId = Long.valueOf(strArray[7]);
                Long descriptionId = Long.valueOf(strArray[8]);
                if (existingUserSet.contains(userId)) {
                    if (!trainUserSet.contains(userId) && !trainAdvertiserSet.contains(advertiserId)) {
                        testSet1.add(lineCounter);
                        fileOut1.write(readPerLine + "\n");

                        userSet.add(userId);
                        descriptionSet.add(descriptionId);
                        purchasedKeywordSet.add(keywordId);
                        querySet.add(queryId);
                        titleSet.add(titleId);
                    }
                    if (!trainUserSet.contains(userId) && trainAdvertiserSet.contains(advertiserId) && testSet2.size() < 500000) {
                        testSet2.add(lineCounter);
                        fileOut2.write(readPerLine + "\n");

                        userSet.add(userId);
                        descriptionSet.add(descriptionId);
                        purchasedKeywordSet.add(keywordId);
                        querySet.add(queryId);
                        titleSet.add(titleId);
                    }
                    if (trainUserSet.contains(userId) && !trainAdvertiserSet.contains(advertiserId)) {
                        testSet3.add(lineCounter);
                        fileOut3.write(readPerLine + "\n");

                        userSet.add(userId);
                        descriptionSet.add(descriptionId);
                        purchasedKeywordSet.add(keywordId);
                        querySet.add(queryId);
                        titleSet.add(titleId);
                    }
                }
            }
            System.out.println("total new combination is " + testSet1.size());
            System.out.println("total new user is " + testSet2.size());
            System.out.println("total new advertiser is " + testSet3.size());

            bufferReader.close();
            fileOut1.close();
            fileOut2.close();
            fileOut3.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void procSolution(String fileName) {

        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = new String();
            String file1 = "/media/data4/zzhou/kddcup/project/solution/newSol.txt";
            String file2 = "/media/data4/zzhou/kddcup/project/solution/newUserSol.txt";
            String file3 = "/media/data4/zzhou/kddcup/project/solution/newAdvSol.txt";

            FileOutputStream fstream1 = new FileOutputStream(file1);
            BufferedWriter fileOut1 = new BufferedWriter(new OutputStreamWriter(fstream1, "UTF-8"));

            FileOutputStream fstream2 = new FileOutputStream(file2);
            BufferedWriter fileOut2 = new BufferedWriter(new OutputStreamWriter(fstream2, "UTF-8"));

            FileOutputStream fstream3 = new FileOutputStream(file3);
            BufferedWriter fileOut3 = new BufferedWriter(new OutputStreamWriter(fstream3, "UTF-8"));

            Long lineCounter = 0L;
            while ((readPerLine = bufferReader.readLine()) != null) {

                if (testSet1.contains(lineCounter))
                    fileOut1.write(readPerLine + "\n");

                if (testSet2.contains(lineCounter))
                    fileOut2.write(readPerLine + "\n");

                if (testSet3.contains(lineCounter))
                    fileOut3.write(readPerLine + "\n");

                lineCounter++;
            }

            bufferReader.close();
            fileOut1.close();
            fileOut2.close();
            fileOut3.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void procContent() {

        String[] fileName = new String[5];
        fileName[0] = "descriptionid_tokensid.txt";
        fileName[1] = "purchasedkeywordid_tokensid.txt";
        fileName[2] = "queryid_tokensid.txt";
        fileName[3] = "titleid_tokensid.txt";
        fileName[4] = "userid_profile.txt";

        String inputPath = "/media/data4/zzhou/kddcup/track2/";
        String outputPath = "/media/data4/zzhou/kddcup/project/";
        sampling(inputPath + fileName[0], outputPath + fileName[0], descriptionSet);
        sampling(inputPath + fileName[1], outputPath + fileName[1], purchasedKeywordSet);
        sampling(inputPath + fileName[2], outputPath + fileName[2], querySet);
        sampling(inputPath + fileName[3], outputPath + fileName[3], titleSet);
        sampling(inputPath + fileName[4], outputPath + fileName[4], userSet);

    }

    public void sampling(String in, String out, Set<Long> selectSet) {

        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
            String readPerLine = new String();
            FileOutputStream fstream = new FileOutputStream(out);
            BufferedWriter fout = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Long current = Long.valueOf(strArray[0]);
                if (selectSet.contains(current))
                    fout.write(readPerLine + "\n");

            }

            bufferReader.close();
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // DocumentClassifier example = new DocumentClassifier(args[0]);

        String dataPath = "/media/data4/zzhou/kddcup/track2/training.txt";
        String testPath = "/media/data4/zzhou/kddcup/track2/test.txt";
        String solPath = "/media/data4/zzhou/kddcup/csv/KDD_Track2_solution.csv";

        DataSetGenerator ex = new DataSetGenerator();
        ex.sampleTraining(dataPath);
        ex.procTesting(testPath);
        ex.procContent();
        ex.procSolution(solPath);

        System.out.println(ex.trainAdvertiserSet.size());
        System.out.println(ex.trainUserSet.size());

        System.out.println("query:" + ex.querySet.size());
        System.out.println("user:" + ex.userSet.size());
        System.out.println("keyword:" + ex.purchasedKeywordSet.size());
        System.out.println("description:" + ex.descriptionSet.size());
        System.out.println("title" + ex.titleSet.size());


    }
}
