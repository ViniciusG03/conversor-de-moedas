import API.Moeda;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int chose;

        do {
            System.out.println("====================\n" +
                    "Qual moeda deseja converter?\n" +
                    "1. USD para BRL\n" +
                    "2. BOB para BRL\n" +
                    "3. CLP para BRL\n" +
                    "4. COP para BRL\n" +
                    "5. ARS para BRL\n" +
                    "6. Sair\n" +
                    "====================");
            chose = input.nextInt();

            switch (chose) {
                case 1:
                    String moeda = "USD";
                    conversao(moeda);
                    break;
                case 2:
                    moeda = "BOB";
                    conversao(moeda);
                    break;
                case 3:
                    moeda = "CLP";
                    conversao(moeda);
                    break;
                case 4:
                    moeda = "COP";
                    conversao(moeda);
                    break;
                case 5:
                    moeda = "ARS";
                    conversao(moeda);
                    break;
                case 6:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

        } while (chose != 6);
    }

    public static void conversao(String cambio) {
        String endereco = "https://v6.exchangerate-api.com/v6/e404ef88fffbbf710e2cf7f8/latest/" + cambio;

        //Criando o client do HTTP;
        HttpClient client = HttpClient.newHttpClient();

        //Criando a requisição;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .GET()
                .build();

        try {
            //Enviando a requisição e recebendo a resposta;
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Verificando se a resposta foi bem sucedida;
            if (response.statusCode() == 200) {
                //Convertendo a resposta para um objeto JSON;
                Gson gson = new Gson();

                //Convertendo o JSON para um objeto Java;
                JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

                //Criando um objeto Moeda com os valores do JSON;
                Moeda moeda = new Moeda(
                        jsonObject.get("conversion_rates").getAsJsonObject().get(cambio).getAsString(),
                        jsonObject.get("conversion_rates").getAsJsonObject().get("BRL").getAsString()
                );

                System.out.println("Taxa da conversão de uma unidade de " + cambio + " para uma unidade de BRL " + moeda.BRL());
            } else {
                System.out.println("Erro na requisição" + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
