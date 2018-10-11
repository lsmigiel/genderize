import DTO.GenderJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {

        List<String> fullList = getNamesAsList();

        List<List<String>> listOfTranchesUpToTen = new ArrayList<>();
        int nameCountInTranche = 0;
        int trancheCount = 0;

        for (String name : fullList) {

            if (nameCountInTranche == 0) {
                listOfTranchesUpToTen.add(new ArrayList<>());
            }

            listOfTranchesUpToTen.get(trancheCount).add(name);
            nameCountInTranche++;
            if(nameCountInTranche == 10) {
                trancheCount++;
                nameCountInTranche = 0;
            }
        }

        List<GenderJson> genderJsonList = new ArrayList<>();

        for (List<String> trancheUpToTen : listOfTranchesUpToTen) {
            genderJsonList.addAll(getMaxTenParamsFromApi(trancheUpToTen));
            try {
                Thread.sleep(200);
            }
            catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        printResults(genderJsonList, fullList.size());

    }

    private static void printResults(List<GenderJson> genderJsonList, int totalCount) {
        int femaleCount = 0;
        int maleCount = 0;
        int unknownCount = 0;

        for (GenderJson genderJson : genderJsonList) {
            if ("female".equals(genderJson.getGender())) {
                femaleCount++;
            }
            else if ("male".equals(genderJson.getGender())) {
                maleCount++;
            }
            else {
                unknownCount++;
                System.out.println(genderJson.getName());
            }
        }

        System.out.println("Total: " + totalCount + "; \n" +
                "Female: " + femaleCount + " (" + (femaleCount*100.0)/totalCount + "%); \n" +
                "Male: " + maleCount + " (" + (maleCount*100.0)/totalCount + "%); \n" +
                "Unknown: " + unknownCount + " (" + (unknownCount*100.0)/totalCount + "%); \n" );
    }

    private static List<GenderJson> getMaxTenParamsFromApi(List<String> names) {

        try {

            URL url = buildUrl(names);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Http response: " + conn.getResponseCode());
            }

            InputStreamReader input = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(input);

            Type targetClassType = new TypeToken<ArrayList<GenderJson>>() { }.getType();
            List<GenderJson> genderJsonList = new Gson().fromJson(reader, targetClassType);

            conn.disconnect();

            return genderJsonList;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static URL buildUrl(List<String> names) {


        StringBuilder tempUrl = new StringBuilder("https://api.genderize.io/?");

        int index = 0;
        for(String name : names) {
            if(index != 0) {
                tempUrl.append("&");
            }
            tempUrl.append("name[" + Integer.toString(index) + "]=" + name);
            index++;
        }

        URL url = null;

        try {
            url = new URL(tempUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static List<String> getNamesAsList() {
        String names =
                "";

        String[] lines = names.split("\\r?\\n");

        return Arrays.asList(lines)
                .stream()
                .map(fullname -> fullname.substring(0, fullname.indexOf(" ")))
                .collect(Collectors.toList());
    }
}