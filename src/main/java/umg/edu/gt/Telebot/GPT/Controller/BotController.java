package umg.edu.gt.Telebot.GPT.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umg.edu.gt.Telebot.GPT.Service.BotService;
import umg.edu.gt.Telebot.GPT.Service.BotCommandService;
import java.util.Map;

@RestController
public class BotController {

    @Autowired
    private BotService botService;

    @Autowired
    private BotCommandService botCommandService;

    @PostMapping("/telegram")
    public void handleTelegramUpdate(@RequestBody Map<String, Object> update) {
        System.out.println("Actualización recibida de Telegram: " + update);

        try {
            String command = extractCommandFromUpdate(update);
            Long messageId = extractMessageIdFromUpdate(update);
            Long chatId = extractChatIdFromUpdate(update);
            String messageText = extractMessageTextFromUpdate(update);
            String additionalInfo = "Información adicional aquí";

            if (command != null) {
                if (botCommandService.getCommandByMessageId(messageId) == null) {
                    botCommandService.saveBotCommand(command, chatId, messageId, messageText, additionalInfo);
                }
            }

            botService.handleUpdate(update);
        } catch (Exception e) {
            System.err.println("Error al procesar la actualización: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
        private String extractMessageTextFromUpdate(Map<String, Object> update) {
        Map<String, Object> message = (Map<String, Object>) update.get("message");
        return (String) message.get("text");
        }

        private String extractCommandFromUpdate(Map<String, Object> update) {
            return (String) update.get("message.command");
        }

        private Long extractMessageIdFromUpdate(Map<String, Object> update) {
            return ((Number) ((Map<String, Object>) update.get("message")).get("message_id")).longValue();
        }

        private Long extractChatIdFromUpdate(Map<String, Object> update) {
            
            return ((Number) ((Map<String, Object>) ((Map<String, Object>) update.get("message")).get("chat")).get("id")).longValue();
        }
        
        
}