

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zzhou
 * Date: 3/12/13
 * Time: 8:54 PM
 * To change this template use File | Settings | File Templates.
 */


public class LRFeaturePredictor {

    private Map<Integer, List<Integer>> purchasedTokenList;
    private Map<Integer, List<Integer>> queryTokenList;
    private Map<Integer, List<Integer>> titleTokenList;
    private Map<Integer, List<Integer>> descriptionTokenList;
    private Map<Integer, List<Integer>> userGroup;
    private List<Map<Integer, List<Integer>>> tablesList;
    Map<String, Integer> featureMap;


    public void loadTables() {

        String query = "queryid_tokensid.txt";
        queryTokenList = TableLoader.loadQueryTable(query);
        String keywords = "purchasedkeywordid_tokensid.txt";
        purchasedTokenList = TableLoader.loadKeywordsTable(keywords);
        String title = "titleid_tokensid.txt";
        titleTokenList = TableLoader.loadTitleTable(title);
        String description = "descriptionid_tokensid.txt";
        descriptionTokenList = TableLoader.loadDescriptionTable(description);
        String user = "userid_profile.txt";
        userGroup = TableLoader.loadUserTable(user);
        tablesList = new ArrayList<Map<Integer, List<Integer>>>();
        tablesList.add(0, queryTokenList);
        tablesList.add(1, purchasedTokenList);
        tablesList.add(2, titleTokenList);
        tablesList.add(3, descriptionTokenList);
        tablesList.add(4, userGroup);

    }

    // function to process the training data to generate training dataset in liblinear format
    public void processLRTrainingV1() {
        // featureMap maps from the attribute to index of feature(starting from 1)
        featureMap = new HashMap<String, Integer>();
        try {
            String fileName = "training.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String fileOut = "1.txt";

            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Integer newClicks = Integer.valueOf(strArray[0]);
                Integer newImpression = Integer.valueOf(strArray[1]);


                Set<Integer> featureSet = new HashSet<Integer>();

                for (int i = 2; i < strArray.length; i++) {
                    String feature = String.valueOf(i - 2) + "_" + strArray[i];
                    Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
                    featureMap.put(feature, featureIndex);
                    featureSet.add(featureIndex);
                }

                List<Integer> featureList = new ArrayList<Integer>(featureSet);

                // feature index needs to be in sorted order in liblinear format
                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                String classLabel = (newClicks > 0) ? "+" : "-";
                bufferedWriter.write(classLabel + "1");

                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":1");

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // function to process the test data to generate test dataset in liblinear format

    public void processLRTestV1() {

        try {
            String fileName = "newAdvTest.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String fileOut = "1_test.txt";

            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");

                Set<Integer> featureSet = new HashSet<Integer>();

                for (int i = 0; i < strArray.length; i++) {
                    // use the column position as prefix identifier for ids belong to different attribute
                    String feature = String.valueOf(i) + "_" + strArray[i];
                    Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
                    featureMap.put(feature, featureIndex);
                    featureSet.add(featureIndex);
                }

                List<Integer> featureList = new ArrayList<Integer>(featureSet);

                // feature index needs to be in sorted order in liblinear format
                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                // output label does not matter in this case
                bufferedWriter.write("-1");

                // output the index of non zero feature
                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":1");

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void processLRTrainingV2() {

        featureMap = new HashMap<String, Integer>();
        try {
            String fileName = "training.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String fileOut = "1.txt";


            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Integer newClicks = Integer.valueOf(strArray[0]);
                Integer newImpression = Integer.valueOf(strArray[1]);

                /*
                Long adsplayID = Long.valueOf(strArray[2]);
                Long advertisingID = Long.valueOf(strArray[3]);
                Long advertiserId = Long.valueOf(strArray[4]);
                Integer depth = Integer.valueOf(strArray[5]);
                Integer position = Integer.valueOf(strArray[6]);
                        */

                Set<Integer> featureSet = new HashSet<Integer>();
                for (int i = 5; i <= 6; i++) {
                    String feature = String.valueOf(i - 2) + "_" + strArray[i];
                    Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
                    featureMap.put(feature, featureIndex);
                    featureSet.add(featureIndex);
                }

                /*

                Integer queryId = Integer.valueOf(strArray[7]);
                Integer keywordId = Integer.valueOf(strArray[8]);
                Integer titleId = Integer.valueOf(strArray[9]);
                Integer descriptionId = Integer.valueOf(strArray[10]);
                Long userId = Long.valueOf(strArray[11]);
                  */

                for (int i = 7; i <= 11; i++) {
                    if (i == 7 || i == 8 || i == 11) {
                        Integer idKey = Integer.valueOf(strArray[i]);
                        Map<Integer, List<Integer>> thisTable = tablesList.get(i - 7);
                        List<Integer> tokenList = thisTable.containsKey(idKey) ? thisTable.get(idKey) : new ArrayList<Integer>();
                        for (int j = 0; j < tokenList.size(); j++) {
                            String tokenFeature = String.valueOf(i - 2) + "_" + tokenList.get(j).toString();
                            Integer tokenFeatureIndex = featureMap.containsKey(tokenFeature) ? featureMap.get(tokenFeature) : featureMap.size() + 1;
                            featureMap.put(tokenFeature, tokenFeatureIndex);
                            featureSet.add(tokenFeatureIndex);
                        }
                    }
                }

                List<Integer> featureList = new ArrayList<Integer>(featureSet);

                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                if (newClicks > 0)
                    bufferedWriter.write("+1");
                else
                    bufferedWriter.write("-1");

                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":1");

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processLRTestV2() {

        try {
            String fileName = "newTest.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String fileOut = "1_test.txt";


            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {

                String[] strArray = readPerLine.split("\t");

                /*
                Long adsplayID = Long.valueOf(strArray[0]);
                Long advertisingID = Long.valueOf(strArray[1]);
                Long advertiserId = Long.valueOf(strArray[2]);
                Integer depth = Integer.valueOf(strArray[3]);
                Integer position = Integer.valueOf(strArray[4]);
                        */

                Set<Integer> featureSet = new HashSet<Integer>();
                for (int i = 3; i <= 4; i++) {
                    String feature = String.valueOf(i) + "_" + strArray[i];
                    Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
                    featureMap.put(feature, featureIndex);
                    featureSet.add(featureIndex);
                }

                /*

                Integer queryId = Integer.valueOf(strArray[5]);
                Integer keywordId = Integer.valueOf(strArray[6]);
                Integer titleId = Integer.valueOf(strArray[7]);
                Integer descriptionId = Integer.valueOf(strArray[8]);
                Long userId = Long.valueOf(strArray[9]);
                  */

                for (int i = 5; i <= 9; i++) {
                    if (i == 5 || i == 6 || i == 9) {
                        Integer idKey = Integer.valueOf(strArray[i]);
                        Map<Integer, List<Integer>> thisTable = tablesList.get(i - 5);
                        List<Integer> tokenList = thisTable.containsKey(idKey) ? thisTable.get(idKey) : new ArrayList<Integer>();
                        for (int j = 0; j < tokenList.size(); j++) {
                            String tokenFeature = String.valueOf(i) + "_" + tokenList.get(j).toString();
                            Integer tokenFeatureIndex = featureMap.containsKey(tokenFeature) ? featureMap.get(tokenFeature) : featureMap.size() + 1;
                            featureMap.put(tokenFeature, tokenFeatureIndex);
                            featureSet.add(tokenFeatureIndex);
                        }

                    }
                }

                List<Integer> featureList = new ArrayList<Integer>(featureSet);

                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                bufferedWriter.write("-1");

                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":1");

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static double cosineSimilarity(List<Integer> list1, List<Integer> list2) {
        Map<Integer, Double> wordMap1 = new HashMap<Integer, Double>();
        for (int i = 0; i < list1.size(); i++) {
            Integer word = list1.get(i);
            Double weight = wordMap1.containsKey(word) ? wordMap1.get(word) : 0;
            wordMap1.put(word, weight + 1);
        }

        Map<Integer, Double> wordMap2 = new HashMap<Integer, Double>();

        for (int i = 0; i < list2.size(); i++) {
            Integer word = list2.get(i);
            Double weight = wordMap2.containsKey(word) ? wordMap2.get(word) : 0;
            wordMap2.put(word, weight + 1);
        }

        Set<Integer> interSet = wordMap1.keySet();
        interSet.retainAll(wordMap2.keySet());

        double n = 0.0;
        for (Integer token : interSet)
            n = n + wordMap1.get(token) * wordMap2.get(token);

        double a = 0.0;
        for (Integer token : wordMap1.keySet())
            a = a + wordMap1.get(token) * wordMap1.get(token);

        double b = 0.0;
        for (Integer token : wordMap2.keySet())
            b = b + wordMap2.get(token) * wordMap2.get(token);
        if (n > 0)
            return n / Math.sqrt(a * b);
        else
            return 0.0;
    }

    public Integer getFeatureIndex(String featureKey) {
        Integer featureIndex = featureMap.containsKey(featureKey) ? featureMap.get(featureKey) : featureMap.size() + 1;
        featureMap.put(featureKey, featureIndex);
        return featureIndex;
    }

    public Integer getTokenOverLap(List<Integer> list1, List<Integer> list2) {
        Set<Integer> tokenSet = new HashSet<Integer>(list1);
        tokenSet.retainAll(list2);
        return tokenSet.size();
    }

    public void processLRTrainingV3() {

        featureMap = new HashMap<String, Integer>();
        try {
            String fileName = "training.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String fileOut = "1.txt";


            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Integer newClicks = Integer.valueOf(strArray[0]);
                Integer newImpression = Integer.valueOf(strArray[1]);

                /*
                Long adsplayID = Long.valueOf(strArray[2]);
                Long advertisingID = Long.valueOf(strArray[3]);
                Long advertiserId = Long.valueOf(strArray[4]);
                Integer depth = Integer.valueOf(strArray[5]);
                Integer position = Integer.valueOf(strArray[6]);
                        */

                Map<Integer, Double> featureSet = new HashMap<Integer, Double>();


                for (int i = 5; i <= 6; i++) {
                    String feature = String.valueOf(i - 2) + "_" + strArray[i];
                    Integer featureIndex = getFeatureIndex(feature);
                    featureSet.put(featureIndex, 1.0);
                }

                /*

                Integer queryId = Integer.valueOf(strArray[7]);
                Integer keywordId = Integer.valueOf(strArray[8]);
                Integer titleId = Integer.valueOf(strArray[9]);
                Integer descriptionId = Integer.valueOf(strArray[10]);
                Long userId = Long.valueOf(strArray[11]);
                  */

                for (int i = 7; i <= 8; i++) {
                    Integer idKey = Integer.valueOf(strArray[i]);
                    Map<Integer, List<Integer>> thisTable = tablesList.get(i - 7);
                    List<Integer> tokenList = thisTable.containsKey(idKey) ? thisTable.get(idKey) : new ArrayList<Integer>();
                    for (int j = 0; j < tokenList.size(); j++) {
                        String tokenFeature = String.valueOf(i - 2) + "_" + tokenList.get(j).toString();
                        Integer tokenFeatureIndex = getFeatureIndex(tokenFeature);
                        featureSet.put(tokenFeatureIndex, 1.0);
                    }
                }


//                Integer idKey1 = Integer.valueOf(strArray[7]);
//                Map<Integer, List<Integer>> thisTable = tablesList.get(0);
//                List<Integer> tokenList1 = thisTable.containsKey(idKey1) ? thisTable.get(idKey1) : new ArrayList<Integer>();
//
//                Integer idKey2 = Integer.valueOf(strArray[8]);
//                Map<Integer, List<Integer>> thatTable = tablesList.get(1);
//                List<Integer> tokenList2 = thatTable.containsKey(idKey2) ? thatTable.get(idKey2) : new ArrayList<Integer>();
//
//                double cosineScore = cosineSimilarity(tokenList1, tokenList2);
//
//                String feature = "cosine";
//                Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
//                featureMap.put(feature, featureIndex);
//                featureSet.put(featureIndex, (double) Math.round(cosineScore * 10000) / 10000);

//                for (int i = 7; i <= 10; i++)
//                    for (int j = i + 1; j <= 10; j++) {
//                        Integer idKey1 = Integer.valueOf(strArray[i]);
//                        Map<Integer, List<Integer>> thisTable = tablesList.get(i - 7);
//                        List<Integer> tokenList1 = thisTable.containsKey(idKey1) ? thisTable.get(idKey1) : new ArrayList<Integer>();
//
//                        Integer idKey2 = Integer.valueOf(strArray[j]);
//                        Map<Integer, List<Integer>> thatTable = tablesList.get(j - 7);
//                        List<Integer> tokenList2 = thatTable.containsKey(idKey2) ? thatTable.get(idKey2) : new ArrayList<Integer>();
//
//
//                        String feature = String.valueOf(i-2) + "_" + String.valueOf(j-2) + "_" + String.valueOf(getTokenOverLap(tokenList1, tokenList2));
//                        Integer featureIndex = getFeatureIndex(feature);
//                        featureSet.put(featureIndex, 1.0);
//                    }


                List<Integer> featureList = new ArrayList(featureSet.keySet());

                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                if (newClicks > 0)
                    bufferedWriter.write("+1");
                else
                    bufferedWriter.write("-1");

                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":" + featureSet.get(featureList.get(i)));

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
            System.out.println("the size of features: " + featureMap.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processLRTestV3() {

        try {
            String fileName = "newUserTest.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String fileOut = "1_test.txt";


            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {

                String[] strArray = readPerLine.split("\t");

                /*
                Long adsplayID = Long.valueOf(strArray[0]);
                Long advertisingID = Long.valueOf(strArray[1]);
                Long advertiserId = Long.valueOf(strArray[2]);
                Integer depth = Integer.valueOf(strArray[3]);
                Integer position = Integer.valueOf(strArray[4]);
                        */

                Map<Integer, Double> featureSet = new HashMap<Integer, Double>();
                for (int i = 3; i <= 4; i++) {
                    String feature = String.valueOf(i) + "_" + strArray[i];
                    Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
                    featureMap.put(feature, featureIndex);
                    featureSet.put(featureIndex, 1.0);
                }

                /*

                Integer queryId = Integer.valueOf(strArray[5]);
                Integer keywordId = Integer.valueOf(strArray[6]);
                Integer titleId = Integer.valueOf(strArray[7]);
                Integer descriptionId = Integer.valueOf(strArray[8]);
                Long userId = Long.valueOf(strArray[9]);
                  */

                for (int i = 5; i <= 6; i++) {
                    Integer idKey = Integer.valueOf(strArray[i]);
                    Map<Integer, List<Integer>> thisTable = tablesList.get(i - 5);
                    List<Integer> tokenList = thisTable.containsKey(idKey) ? thisTable.get(idKey) : new ArrayList<Integer>();
                    for (int j = 0; j < tokenList.size(); j++) {
                        String tokenFeature = String.valueOf(i) + "_" + tokenList.get(j).toString();
                        Integer tokenFeatureIndex = featureMap.containsKey(tokenFeature) ? featureMap.get(tokenFeature) : featureMap.size() + 1;
                        featureMap.put(tokenFeature, tokenFeatureIndex);
                        featureSet.put(tokenFeatureIndex, 1.0);
                    }
                }

//                Integer idKey1 = Integer.valueOf(strArray[7]);
//                Map<Integer, List<Integer>> thisTable = tablesList.get(0);
//                List<Integer> tokenList1 = thisTable.containsKey(idKey1) ? thisTable.get(idKey1) : new ArrayList<Integer>();
//
//                Integer idKey2 = Integer.valueOf(strArray[8]);
//                Map<Integer, List<Integer>> thatTable = tablesList.get(1);
//                List<Integer> tokenList2 = thatTable.containsKey(idKey2) ? thatTable.get(idKey2) : new ArrayList<Integer>();
//
//                double cosineScore = cosineSimilarity(tokenList1, tokenList2);
//
//                String feature = "cosine";
//                Integer featureIndex = featureMap.containsKey(feature) ? featureMap.get(feature) : featureMap.size() + 1;
//                featureMap.put(feature, featureIndex);
//                featureSet.put(featureIndex, (double) Math.round(cosineScore * 10000) / 10000);

//                for (int i = 5; i <= 8; i++)
//                    for (int j = i + 1; j <= 8; j++) {
//                        Integer idKey1 = Integer.valueOf(strArray[i]);
//                        Map<Integer, List<Integer>> thisTable = tablesList.get(i - 5);
//                        List<Integer> tokenList1 = thisTable.containsKey(idKey1) ? thisTable.get(idKey1) : new ArrayList<Integer>();
//
//                        Integer idKey2 = Integer.valueOf(strArray[j]);
//                        Map<Integer, List<Integer>> thatTable = tablesList.get(j - 5);
//                        List<Integer> tokenList2 = thatTable.containsKey(idKey2) ? thatTable.get(idKey2) : new ArrayList<Integer>();
//
//
//                        String feature = String.valueOf(i) + "_" + String.valueOf(j) + "_" + String.valueOf(getTokenOverLap(tokenList1, tokenList2));
//                        Integer featureIndex = getFeatureIndex(feature);
//                        featureSet.put(featureIndex, 1.0);
//                    }

                List<Integer> featureList = new ArrayList(featureSet.keySet());

                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                bufferedWriter.write("-1");


                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":" + featureSet.get(featureList.get(i)));


                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void processLRTraining() {

        featureMap = new HashMap<String, Integer>();
        try {
            String fileName = "training.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

            String fileOut = "1.txt";


            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                Integer newClicks = Integer.valueOf(strArray[0]);
                Integer newImpression = Integer.valueOf(strArray[1]);

                Integer depth = Integer.valueOf(strArray[5]);
                Integer position = Integer.valueOf(strArray[6]);

                Long userId = Long.valueOf(strArray[11]);
                Long advertiserId = Long.valueOf(strArray[4]);
                Integer queryId = Integer.valueOf(strArray[7]);
                Integer keywordId = Integer.valueOf(strArray[8]);
                Integer titleId = Integer.valueOf(strArray[9]);
                Integer descriptionId = Integer.valueOf(strArray[10]);

                if (newClicks > 0)
                    bufferedWriter.write("+1");
                else
                    bufferedWriter.write("-1");

                Set<Integer> featureSet = new HashSet<Integer>();

                List<Integer> tokenList = queryTokenList.containsKey(queryId) ? queryTokenList.get(queryId) : new ArrayList<Integer>();

                String depthFeature = "d_" + depth.toString();

                Integer depthFeatureIndex = featureMap.containsKey(depthFeature) ? featureMap.get(depthFeature) : featureMap.size() + 1;
                featureMap.put(depthFeature, depthFeatureIndex);
                featureSet.add(depthFeatureIndex);

                String positionFeature = "p_" + position.toString();
                Integer positionFeatureIndex = featureMap.containsKey(positionFeature) ? featureMap.get(positionFeature) : featureMap.size() + 1;
                featureMap.put(positionFeature, positionFeatureIndex);
                featureSet.add(positionFeatureIndex);

                for (int j = 0; j < tokenList.size(); j++) {
                    String tokenFeature = "t_" + tokenList.get(j).toString();
                    Integer tokenFeatureIndex = featureMap.containsKey(tokenFeature) ? featureMap.get(tokenFeature) : featureMap.size() + 1;
                    featureMap.put(tokenFeature, tokenFeatureIndex);
                    featureSet.add(tokenFeatureIndex);
                }

                List<Integer> featureList = new ArrayList<Integer>(featureSet);

                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":1");

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processLRTest() {

        try {
            String fileName = "newTest.txt";
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String fileOut = "1_test.txt";


            FileOutputStream fstream = new FileOutputStream(fileOut);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fstream, "UTF-8"));

            String readPerLine = null;

            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");

                Integer depth = Integer.valueOf(strArray[3]);
                Integer position = Integer.valueOf(strArray[4]);

                Long userId = Long.valueOf(strArray[9]);
                Long advertiserId = Long.valueOf(strArray[2]);
                Integer queryId = Integer.valueOf(strArray[5]);
                Integer keywordId = Integer.valueOf(strArray[6]);
                Integer titleId = Integer.valueOf(strArray[7]);
                Integer descriptionId = Integer.valueOf(strArray[8]);


                Set<Integer> featureSet = new HashSet<Integer>();

                List<Integer> tokenList = queryTokenList.containsKey(queryId) ? queryTokenList.get(queryId) : new ArrayList<Integer>();

                String depthFeature = "d_" + depth.toString();

                Integer depthFeatureIndex = featureMap.containsKey(depthFeature) ? featureMap.get(depthFeature) : featureMap.size() + 1;
                featureMap.put(depthFeature, depthFeatureIndex);
                featureSet.add(depthFeatureIndex);

                String positionFeature = "p_" + position.toString();
                Integer positionFeatureIndex = featureMap.containsKey(positionFeature) ? featureMap.get(positionFeature) : featureMap.size() + 1;
                featureMap.put(positionFeature, positionFeatureIndex);
                featureSet.add(positionFeatureIndex);

                for (int j = 0; j < tokenList.size(); j++) {
                    String tokenFeature = "t_" + tokenList.get(j).toString();
                    Integer tokenFeatureIndex = featureMap.containsKey(tokenFeature) ? featureMap.get(tokenFeature) : featureMap.size() + 1;
                    featureMap.put(tokenFeature, tokenFeatureIndex);
                    featureSet.add(tokenFeatureIndex);
                }

                List<Integer> featureList = new ArrayList<Integer>(featureSet);


                Collections.sort(featureList, new Comparator<Integer>() {
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                bufferedWriter.write("-1");

                for (int i = 0; i < featureList.size(); i++)
                    bufferedWriter.write(" " + featureList.get(i) + ":1");

                bufferedWriter.write("\n");

            }
            bufferedWriter.close();
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        LRFeaturePredictor ex = new LRFeaturePredictor();
 //       ex.loadTables();
//        ex.processLRTrainingV3();
//        ex.processLRTestV3();
    /*    List<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(2);
        List<Integer> b = new ArrayList<Integer>();
        b.add(2);
        b.add(3);
        System.out.println(cosineSimilarity(a, b));
      */
        //      ex.loadTables();
        //      ex.processLRTrainingV2();
        //      ex.processLRTestV2();
        ex.processLRTrainingV1();
        ex.processLRTestV1();
    }
}