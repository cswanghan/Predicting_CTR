

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zzhou
 * Date: 3/12/13
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableLoader {

    public static Map<Integer, List<Integer>> loadKeywordsTable(String fileName) {

        Map<Integer, List<Integer>> purchasedTokenList = new HashMap<Integer, List<Integer>>();
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;
            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                String[] tokens = strArray[1].split("\\|");
                List<Integer> tokenList = new ArrayList<Integer>();
                for (int i = 0; i < tokens.length; i++)
                    tokenList.add(i, Integer.valueOf(tokens[i]));
                Integer ids = Integer.valueOf(strArray[0]);
                purchasedTokenList.put(ids, tokenList);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded keywords table of " + purchasedTokenList.size());
        return purchasedTokenList;
    }

    public static Map<Integer, List<Integer>> loadQueryTable(String fileName) {
        Map<Integer, List<Integer>> queryTokenList = new HashMap<Integer, List<Integer>>();
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;
            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                String[] tokens = strArray[1].split("\\|");
                List<Integer> tokenList = new ArrayList<Integer>();
                for (int i = 0; i < tokens.length; i++)
                    tokenList.add(i, Integer.valueOf(tokens[i]));
                Integer ids = Integer.valueOf(strArray[0]);
                queryTokenList.put(ids, tokenList);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded query table of " + queryTokenList.size());
        return queryTokenList;
    }

    public static Map<Integer, List<Integer>> loadTitleTable(String fileName) {
        Map<Integer, List<Integer>> titleTokenList = new HashMap<Integer, List<Integer>>();
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;
            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                String[] tokens = strArray[1].split("\\|");
                List<Integer> tokenList = new ArrayList<Integer>();
                for (int i = 0; i < tokens.length; i++)
                    tokenList.add(i, Integer.valueOf(tokens[i]));
                Integer ids = Integer.valueOf(strArray[0]);
                titleTokenList.put(ids, tokenList);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded title table of " + titleTokenList.size());

        return titleTokenList;
    }

    public static Map<Integer, List<Integer>> loadDescriptionTable(String fileName) {
        Map<Integer, List<Integer>> descriptionTokenList = new HashMap<Integer, List<Integer>>();
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;
            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                String[] tokens = strArray[1].split("\\|");
                List<Integer> tokenList = new ArrayList<Integer>();
                for (int i = 0; i < tokens.length; i++)
                    tokenList.add(i, Integer.valueOf(tokens[i]));
                Integer ids = Integer.valueOf(strArray[0]);
                descriptionTokenList.put(ids, tokenList);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded description table of " + descriptionTokenList.size());

        return descriptionTokenList;
    }

    public static Map<Integer, List<Integer>> loadUserTable(String fileName) {
        Map<Integer, List<Integer>> userGroup = new HashMap<Integer, List<Integer>>();
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String readPerLine = null;
            while ((readPerLine = bufferReader.readLine()) != null) {
                String[] strArray = readPerLine.split("\t");
                if (strArray.length != 3)
                    System.out.println("checking" + readPerLine);
                List<Integer> tokenList = new ArrayList<Integer>();
                tokenList.add(0, Integer.valueOf(strArray[1]));
                tokenList.add(1, Integer.valueOf(strArray[2]));
                Integer ids = Integer.valueOf(strArray[0]);
                userGroup.put(ids, tokenList);
            }
            bufferReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("loaded user table of " + userGroup.size());
        return userGroup;
    }

}
