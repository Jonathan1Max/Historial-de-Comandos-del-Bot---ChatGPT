package umg.edu.gt.Telebot.GPT.Service;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TelegramPollingService {

    @Autowired
    private BotService botService;

    private final String BOT_TOKEN = "7822338733:AAH3RJF87rr4QmkqRvjpIlUiZYhHS8zBTaQ";
    private final String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN;

    @PostConstruct
    public void startPolling() {
        new Thread(() -> {
            int offset = 0;
            while (true) {
                try {
                    String url = TELEGRAM_API_URL + "/getUpdates?timeout=60&offset=" + offset;
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
                    Map<String, Object> body = response.getBody();

                    if (body != null && (Boolean) body.get("ok")) {
                        List<Map<String, Object>> result = (List<Map<String, Object>>) body.get("result");
                        for (Map<String, Object> update : result) {
                            offset = ((Number) update.get("update_id")).intValue() + 1;
                            botService.handleUpdate(update);
                        }
                    }

                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }
}
