package umg.edu.gt.Telebot.GPT.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umg.edu.gt.Telebot.GPT.Model.Client;
import umg.edu.gt.Telebot.GPT.Repository.ClientRepository;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import umg.edu.gt.Telebot.GPT.Repository.RequestRepository;

@Service
public class BotService {

    private final String BOT_TOKEN = "7822338733:AAH3RJF87rr4QmkqRvjpIlUiZYhHS8zBTaQ";
    private final String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";

    private Map<Long, Boolean> askingName = new HashMap<>();
    private Map<Long, String> userNames = new HashMap<>();

    private final ChatGPTClient chatGPTClient;
    private final ClientRepository clientRepository;
    private final RequestRepository requestRepository;
    private final BotCommandService botCommandService;
    private final RestTemplate restTemplate;
    
    @Autowired
    public BotService(ChatGPTClient chatGPTClient, ClientRepository clientRepository, 
                      RequestRepository requestRepository, BotCommandService botCommandService, 
                      RestTemplate restTemplate) {
        this.chatGPTClient = chatGPTClient;
        this.clientRepository = clientRepository;
        this.requestRepository = requestRepository;
        this.botCommandService = botCommandService;
        this.restTemplate = restTemplate;
    }

    public void sendTelegramMessage(Long chatId, String message) {
        try {
            String url = TELEGRAM_API_URL + "?chat_id=" + chatId + "&text=" + message;
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            sendTelegramMessage(chatId, "Hubo un problema al intentar enviarte el mensaje. Intenta de nuevo más tarde.");
        }
    }

    public void setUserName(Long chatId, String name) {
        userNames.put(chatId, name);
    }

    public String getUserName(Long chatId) {
        return userNames.getOrDefault(chatId, "Aún no me has dicho tu nombre.");
    }

    public void setAskingName(Long chatId, boolean asking) {
        askingName.put(chatId, asking);
    }

    public boolean isAskingName(Long chatId) {
        return askingName.getOrDefault(chatId, false);
    }

    public void handleUpdate(Map<String, Object> update) throws SQLException {
        if (update.containsKey("message")) {
            Map<String, Object> message = (Map<String, Object>) update.get("message");
            Map<String, Object> chat = (Map<String, Object>) message.get("chat");
            long chatId = ((Number) chat.get("id")).longValue();
            String text = (String) message.get("text");
            Long messageId = ((Number) message.get("message_id")).longValue();

            if (text.startsWith("/")) {
                handleCommand(chatId, text, messageId);
            } else {
                Client client = clientRepository.getById(chatId);
                if (client != null) {
                    String response = processUserMessage(text);
                    sendTelegramMessage(chatId, response);
                } else {
                    if (text.equalsIgnoreCase("/start")) {
                        sendTelegramMessage(chatId, "¡Bienvenido! ¿Cómo te llamas?");
                        setAskingName(chatId, true);
                    } else if (isAskingName(chatId)) {
                        setUserName(chatId, text);
                        clientRepository.add(text, chatId);

                        Client newClient = clientRepository.getById(chatId);
                        if (newClient != null) {
                            sendTelegramMessage(chatId, "¡Hola " + newClient.getName() + ", en qué te puedo ayudar hoy?");
                        } else {
                            sendTelegramMessage(chatId, "¡Gracias! Tu nombre ha sido guardado.");
                        }

                        setAskingName(chatId, false);
                    } else {
                        String response = processUserMessage(text);
                        sendTelegramMessage(chatId, response);
                    }
                }
            }
        } else {
            System.out.println("La actualización no contiene un mensaje válido.");
        }
    }

        private void handleCommand(Long chatId, String command, Long messageId) {
        String responseMessage = "";
        String additionalInfo = "";

        if (command.equalsIgnoreCase("/help")) {
            responseMessage = "Este es el comando /help. ¿En qué puedo ayudarte?";
        } else if (command.equalsIgnoreCase("/info")) {
            responseMessage = "Este es el comando /info. Aquí va la información sobre el bot.";
        }

        sendTelegramMessage(chatId, responseMessage);

        botCommandService.saveBotCommand(command, chatId, messageId, responseMessage, additionalInfo);
    }

    public String processUserMessage(String userMessage) {
        String storedResponse = requestRepository.getResponseByQuestion(userMessage);
        if (storedResponse != null) {
            return storedResponse;  
        }

        String response = chatGPTClient.getChatGPTResponse(userMessage);
        if (response != null) {
            requestRepository.saveRequest(userMessage, response);
            return response;
        }

        return "Lo siento, no pude procesar tu mensaje.";
    }
}