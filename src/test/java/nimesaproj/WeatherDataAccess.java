package nimesaproj;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherDataAccess {

    private static final String API_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22";

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int option;
            do {
                displayMenu();
                option = Integer.parseInt(reader.readLine());
                switch (option) {
                    case 1:
                        getWeatherData(reader);
                        break;
                    case 2:
                        getWindSpeed(reader);
                        break;
                    case 3:
                        getPressure(reader);
                        break;
                    case 0:
                        System.out.println("Exiting program. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } while (option != 0);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Get weather");
        System.out.println("2. Get Wind Speed");
        System.out.println("3. Get Pressure");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void getWeatherData(BufferedReader reader) throws IOException {
        System.out.print("Enter the date: ");
        String date = reader.readLine();
        JSONArray jsonArray = fetchData();
        if (jsonArray != null) {
            JSONObject weatherData = findWeatherDataForDate(jsonArray, date);
            if (weatherData != null) {
                double temperature = weatherData.getJSONObject("main").getDouble("temp");
                System.out.println("Temperature on " + date + ": " + temperature + " °C");
            } else {
                System.out.println("Weather data not found for the given date.");
            }
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    private static void getWindSpeed(BufferedReader reader) throws IOException {
        System.out.print("Enter the date: ");
        String date = reader.readLine();
        JSONArray jsonArray = fetchData();
        if (jsonArray != null) {
            JSONObject weatherData = findWeatherDataForDate(jsonArray, date);
            if (weatherData != null) {
                double windSpeed = weatherData.getJSONObject("wind").getDouble("speed");
                System.out.println("Wind Speed on " + date + ": " + windSpeed + " m/s");
            } else {
                System.out.println("Weather data not found for the given date.");
            }
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    private static void getPressure(BufferedReader reader) throws IOException {
        System.out.print("Enter the date: ");
        String date = reader.readLine();
        JSONArray jsonArray = fetchData();
        if (jsonArray != null) {
            JSONObject weatherData = findWeatherDataForDate(jsonArray, date);
            if (weatherData != null) {
                double pressure = weatherData.getJSONObject("main").getDouble("pressure");
                System.out.println("Pressure on " + date + ": " + pressure + " hPa");
            } else {
                System.out.println("Weather data not found for the given date.");
            }
        } else {
            System.out.println("Failed to fetch weather data.");
        }
    }

    private static JSONArray fetchData() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONObject(response.toString()).getJSONArray("list");
        } else {
            System.out.println("Error occurred while fetching data. HTTP error code: " + responseCode);
            return null;
        }
    }

    private static JSONObject findWeatherDataForDate(JSONArray jsonArray, String date) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String itemDate = item.getString("dt_txt").split(" ")[0];
            if (itemDate.equals(date)) {
                return item;
            }
        }
        return null;
    }
}
