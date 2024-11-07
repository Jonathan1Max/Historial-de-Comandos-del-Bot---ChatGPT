/*package umg.edu.gt.Telebot.GPT.Service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandHandler {
    
    private final Map<String, String> commands;
    
    public CommandHandler() {
        commands = new HashMap<>();
        commands.put("/start", "¡Bienvenido! ¿Cómo te llamas?");
        commands.put("/help", "Estos son los comandos disponibles: /start, /help, /settings");
        
    }
    
    public String handleCommand(String command){
        return commands.getOrDefault(command, "Comando no reconocido.");
    }
}*/
