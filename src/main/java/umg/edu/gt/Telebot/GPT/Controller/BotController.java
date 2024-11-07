package umg.edu.gt.Telebot.GPT.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umg.edu.gt.Telebot.GPT.Service.BotService;
import java.util.Map;

@RestController
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping("/telegram")
    public void handleTelegramUpdate(@RequestBody Map<String, Object> update) {
        System.out.println("Actualización recibida de Telegram: " + update);

        try {
            botService.handleUpdate(update);
        } catch (Exception e) {
            System.err.println("Error al procesar la actualización: " + e.getMessage());
            e.printStackTrace();
        }
    }
}