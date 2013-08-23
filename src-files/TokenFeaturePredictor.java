

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zzhou
 * Date: 3/8/13
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokenFeaturePredictor {
    private Map<Integer, List<Integer>> purchasedTokenList;
    private Map<Integer, List<Integer>> queryTokenList;
    private Map<Integer, List<Integer>> titleTokenList;
    private Map<Integer, List<Integer>> descriptionTokenList;
    private Map<Integer, List<Integer>> userGroup;

    public void loadTables() {
        String keywords = "purchasedkeywordid_tokensid.txt";
        purchasedTokenList = TableLoader.loadKeywordsTable(keywords);
        String query = "queryid_tokensid.txt";
        queryTokenList = TableLoader.loadQueryTable(query);
        String title = "titleid_tokensid.txt";
        titleTokenList = TableLoader.loadTitleTable(title);
        String description = "descriptionid_tokensid.txt";
        descriptionTokenList = TableLoader.loadDescriptionTable(description);
        String user = "userid_profile.txt";
        userGroup = TableLoader.loadUserTable(user);
    }

    public void tokenCTRFeature() {

        Map<Integer, Double> ImpressCounts = new HashMap<Integer, Double>();

        Map<Integer, Double> ClickCounts = new HashMap<Integer, Double>();

        int select = 10;

        try {
            String fileName = "training.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Integer newClicks = Integer.valueOf(strArray[0]);
                Integer newImpression = Integer.valueOf(strArray[1]);


//                Long userId = Long.valueOf(strArray[11]);
//                Long advertiserId = Long.valueOf(strArray[4]);
//                Integer queryId = Integer.valueOf(strArray[7]);
//                Integer keywordId = Integer.valueOf(strArray[8]);
//                Integer titleId = Integer.valueOf(strArray[9]);
//                Integer descriptionId = Integer.valueOf(strArray[10]);

                Integer queryId = Integer.valueOf(strArray[select]);
                //           List<Integer> tokenList = queryTokenList.containsKey(queryId) ? queryTokenList.get(queryId) : new ArrayList<Integer>();

                List<Integer> tokenList = descriptionTokenList.containsKey(queryId) ? descriptionTokenList.get(queryId) : new ArrayList<Integer>();


                for (int j = 0; j < tokenList.size(); j++) {
                    Integer token = tokenList.get(j);
                    Double currentImp = ImpressCounts.containsKey(token) ? ImpressCounts.get(token) : 0;
                    Double currentClicks = ClickCounts.containsKey(token) ? ClickCounts.get(token) : 0;
                    ImpressCounts.put(token, currentImp + newImpression * 1.0 / tokenList.size());
                    ClickCounts.put(token, currentClicks + newClicks * 1.0 / tokenList.size());
                }

            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            String fileName = "newTest.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;

            String fileOut = "result.txt";

            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");

                Integer queryId = Integer.valueOf(strArray[select - 2]);
                //            List<Integer> tokenList = queryTokenList.containsKey(queryId) ? queryTokenList.get(queryId) : new ArrayList<Integer>();
                List<Integer> tokenList = descriptionTokenList.containsKey(queryId) ? descriptionTokenList.get(queryId) : new ArrayList<Integer>();

                Double sumImp = 0.0;
                Double sumClick = 0.0;
                for (int j = 0; j < tokenList.size(); j++) {
                    Integer token = tokenList.get(j);
                    sumImp += ImpressCounts.containsKey(token) ? ImpressCounts.get(token) : 0.0;
                    sumClick += ClickCounts.containsKey(token) ? ClickCounts.get(token) : 0.0;
                }
                if (sumImp > 0)
                    bufferedWriter.write(sumClick / sumImp + "\n");
                else
                    bufferedWriter.write("0\n");

            }
            bufferReader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        TokenFeaturePredictor ex = new TokenFeaturePredictor();
        ex.loadTables();
        ex.tokenCTRFeature();
    }
}
