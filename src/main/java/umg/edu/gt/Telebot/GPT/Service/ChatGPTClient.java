package umg.edu.gt.Telebot.GPT.Service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatGPTClient {

    private final String API_KEY = "";
    private final RestTemplate restTemplate;

    public ChatGPTClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getChatGPTResponse(String userMessage) {
        String apiUrl = "apigpt";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", List.of(
            Map.of("role", "user", "content", userMessage)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                Map<String, Object> choice = ((List<Map<String, Object>>) responseBody.get("choices")).get(0);
                Map<String, Object> message = (Map<String, Object>) choice.get("message");
                return (String) message.get("content");
            } else {
                return "No se recibió respuesta válida de ChatGPT.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Hubo un problema al contactar a ChatGPT";
        }
    }
}