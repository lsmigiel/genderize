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
                "Paweł Zasada\n" +
                        "Oleg Koladko\n" +
                        "Bogdan Szeremeta\n" +
                        "Oleksandra Ohirenko\n" +
                        "Olga Kravchenko\n" +
                        "Dominik Pisarski\n" +
                        "Bartek Łabuda\n" +
                        "Alina Staszewska\n" +
                        "Aleksandra Krupińska\n" +
                        "Michał Banaszczyk\n" +
                        "Gosia Wojciechowska\n" +
                        "Nicat Balayev\n" +
                        "Martyna Dobek\n" +
                        "Stanisław Piotrowski\n" +
                        "Estera Zaręba\n" +
                        "Andrey Petrenko\n" +
                        "Martyna Filipek\n" +
                        "Kamil Emil Kawa\n" +
                        "Iwona Hołomek\n" +
                        "Ewelina Seroczyńska\n" +
                        "Monika Kozieł\n" +
                        "Daniel Romański\n" +
                        "Mateusz Cichocki\n" +
                        "Marcin Pietraszek\n" +
                        "Jakub Adrian\n" +
                        "Agnieszka Karczewska\n" +
                        "Daniel Binkowski\n" +
                        "Marcin Orzechowski\n" +
                        "Marta Chodkowska\n" +
                        "Jakub Palacz\n" +
                        "Oskar Furmańczuk\n" +
                        "Maciek Mikulski\n" +
                        "Szymon Tracz\n" +
                        "Dariusz Dybka\n" +
                        "Adam Kacprzycki\n" +
                        "Joanna Bąkała\n" +
                        "Magdalena Kotynia\n" +
                        "Anna Matuszczak\n" +
                        "Wiktor Targański\n" +
                        "Karol Satławski\n" +
                        "Agnieszka Jarosz\n" +
                        "Kasia Karsznia\n" +
                        "Hindych Daria\n" +
                        "Piotr Szwedo\n" +
                        "Wiktor Krawczyk\n" +
                        "Mariusz Wachowicz\n" +
                        "Natalia Dutkiewicz\n" +
                        "Krzysztof Dulęba\n" +
                        "Zuza Majewska\n" +
                        "Łukasz Biedrowski\n" +
                        "Mateusz Wójcik\n" +
                        "Kacper Palacz\n" +
                        "Justyna Marzec\n" +
                        "Malik Amaan\n" +
                        "Kamil Hajduk\n" +
                        "Szymon Jakub Kuźnicki\n" +
                        "Maria Porczyńska-Walczak\n" +
                        "Kamil Paszczuk\n" +
                        "Inga Śmigielska\n" +
                        "Konrad Wojtysiak\n" +
                        "Paweł Krzewiński\n" +
                        "Michal Pawlak\n" +
                        "Tymek Lewkowicz\n" +
                        "Beata Wysocka\n" +
                        "Wioletta Oleszek\n" +
                        "Klaudia Ochojska\n" +
                        "Paweł Kuć\n" +
                        "Przemysław Bus\n" +
                        "Joanna Zawistowska\n" +
                        "Michasia Purgat\n" +
                        "Michał Głażewski\n" +
                        "Ewa Tomaszkiewicz\n" +
                        "Ewelina Robaczyńska\n" +
                        "Mikołaj Figurski\n" +
                        "Dagmara Mazur\n" +
                        "Czeski Michał Stachowicz\n" +
                        "Mateusz Jarosiński\n" +
                        "Miłosz Bielecki\n" +
                        "Magda Szczerbińska\n" +
                        "Artur Lekan\n" +
                        "Klaudia Stochaj\n" +
                        "Mikołaj Czarkowski\n" +
                        "Piotr Okniński\n" +
                        "Mateusz Przybyszewski\n" +
                        "Marta Łaniewska\n" +
                        "Mateusz Cichocki\n" +
                        "Kamil Warda\n" +
                        "Maciej Biedrzycki\n" +
                        "Maciej Jakub Miliszewski\n" +
                        "Justyna Berus\n" +
                        "Wiktoria Kolec\n" +
                        "Karolina Kozicka\n" +
                        "Paulina Pola Samulnik\n" +
                        "Adam Janicki\n" +
                        "Karol Bączek\n" +
                        "Cezary Baranowski\n" +
                        "Magdalena Doch\n" +
                        "Ewelina Łuczyńska\n" +
                        "Bartosz Orzech\n" +
                        "Ana Sattari\n" +
                        "Joanna Tracz\n" +
                        "Iza Krawczyk\n" +
                        "Kürşat Kölemen\n" +
                        "Grześ Ogólnieznany";

        String[] lines = names.split("\\r?\\n");

        return Arrays.asList(lines)
                .stream()
                .map(fullname -> fullname.substring(0, fullname.indexOf(" ")))
                .collect(Collectors.toList());
    }
}