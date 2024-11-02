import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConVerter {
    public static void main(String[] args) {
        HashMap<Integer, String> currencyCodes = new HashMap<>();
        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "CAD");
        currencyCodes.put(3, "EUR");
        currencyCodes.put(4, "HKD");
        currencyCodes.put(5, "INR");

        String fromCode, toCode;
        double amount;
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the Currency Converter");
        System.out.println("Currency converting FROM?");
        System.out.println("1:USD  2:CAD  3:EUR  4:HKD  5:INR");
        fromCode = currencyCodes.get(sc.nextInt());

        System.out.println("Currency converting TO?");
        System.out.println("1:USD  2:CAD  3:EUR  4:HKD  5:INR");
        toCode = currencyCodes.get(sc.nextInt());

        System.out.println("Amount you want to convert?");
        amount = sc.nextDouble();

        if (fromCode == null || toCode == null) {
            System.out.println("Invalid currency selection.");
        } else {
            try {
                double convertedAmount = sendHttpRequest(fromCode, toCode, amount);
                System.out.println("Converted Amount: " + convertedAmount + " " + toCode);
            } catch (IOException e) {
                System.out.println("Error occurred during conversion: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error parsing JSON: " + e.getMessage());
            }
        }
        sc.close();
        System.out.println("Thank you for using our Currency Converter!");
    }

    private static double sendHttpRequest(String fromCode, String toCode, double amount) throws IOException {
        String GET_URL = "https://api.exchangerate-api.com/v4/latest/" + fromCode;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("API Response: " + response.toString()); // Debug step

            JSONObject obj = new JSONObject(response.toString());
            double exchangeRate = obj.getJSONObject("rates").getDouble(toCode);
            return amount * exchangeRate;
        } else {
            throw new IOException("Failed to get exchange rate, HTTP response code: " + responseCode);
        }
    }
}
