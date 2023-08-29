import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Погодное приложение");

        Label cityLabel = new Label("Введите название города:");
        TextField cityField = new TextField();
        Button getWeatherButton = new Button("Получить погоду");
        Label resultLabel = new Label();

        getWeatherButton.setOnAction(e -> {
            String city = cityField.getText();
            String apiKey = "18dac8561ae34125f7054c68d5e8fb2a"; // Замените YOUR_API_KEY на ваш API ключ
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

            try {
                // Создаем объект URL и устанавливаем соединение
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Устанавливаем метод запроса
                connection.setRequestMethod("GET");

                // Получаем ответ от сервера
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Если ответ успешный, читаем данные
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    // Преобразуем ответ в объект JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());

                    // Извлекаем данные о погоде из JSON
                    JSONObject main = jsonResponse.getJSONObject("main");
                    double temperatureKelvin = main.getDouble("temp");
                    double temperatureCelsius = kelvinToCelsius(temperatureKelvin);

                    resultLabel.setText("Температура в " + city + ": " + temperatureCelsius + "°C");
                } else {
                    resultLabel.setText("Ошибка при получении данных о погоде. Код ошибки: " + responseCode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                resultLabel.setText("Ошибка: " + ex.getMessage());
            }
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(cityLabel, cityField, getWeatherButton, resultLabel);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
