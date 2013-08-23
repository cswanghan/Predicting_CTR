import java.io.*;
import java.util.HashMap;
import java.util.Random;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zzhou
 * Date: 3/17/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataStats {

    private Map<Long, Integer> advCounts;
    private Map<Long, Integer> userCounts;

    public DataStats() {
        advCounts = new HashMap<Long, Integer>();
        userCounts = new HashMap<Long, Integer>();
    }

    public void countTraining(String fileName) {
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;

            String file1 = "advCount.txt";

            FileOutputStream fstream1 = new FileOutputStream(file1);
            BufferedWriter fileOut1 = new BufferedWriter(new OutputStreamWriter(fstream1, "UTF-8"));



            String file2 = "userCount.txt";

            FileOutputStream fstream2 = new FileOutputStream(file2);
            BufferedWriter fileOut2 = new BufferedWriter(new OutputStreamWriter(fstream2, "UTF-8"));

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Long userId = Long.valueOf(strArray[11]);
                Long advertiserId = Long.valueOf(strArray[4]);

                Long queryId = Long.valueOf(strArray[7]);
                Long keywordId = Long.valueOf(strArray[8]);
                Long titleId = Long.valueOf(strArray[9]);
                Long descriptionId = Long.valueOf(strArray[10]);

                int advCount = advCounts.containsKey(advertiserId)? advCounts.get(advertiserId):0;
                advCounts.put(advertiserId, advCount + 1);

                int userCount = userCounts.containsKey(userId) ? userCounts.get(userId):0;
                userCounts.put(userId, userCount + 1);

            }

            for(Long adv : advCounts.keySet())
                fileOut1.write(advCounts.get(adv)+"\n");


            for(Long user : userCounts.keySet())
                fileOut2.write(userCounts.get(user)+"\n");

            bufferReader.close();
            fileOut1.close();
            fileOut2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String dataPath = "training.txt";

        DataStats ex = new DataStats();
        ex.countTraining(dataPath);

    }
}