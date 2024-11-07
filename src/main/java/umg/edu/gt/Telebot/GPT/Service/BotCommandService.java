package umg.edu.gt.Telebot.GPT.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import umg.edu.gt.Telebot.GPT.Model.BotCommand;
import umg.edu.gt.Telebot.GPT.Model.Message;
import umg.edu.gt.Telebot.GPT.Repository.BotCommandRepository;
import umg.edu.gt.Telebot.GPT.Repository.MessageRepository;

@Service
public class BotCommandService {

    private final BotCommandRepository botCommandRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public BotCommandService(BotCommandRepository botCommandRepository, MessageRepository messageRepository) {
        this.botCommandRepository = botCommandRepository;
        this.messageRepository = messageRepository;
    }

    public void saveBotCommand(String command, Long chatId, Long messageId, String responseMessage, String additionalInfo) {
    Message message = messageRepository.findById(messageId).orElse(null);
    
    if (message == null) {
        message = new Message(responseMessage);
        messageRepository.save(message);
    }

    BotCommand existingCommand = botCommandRepository.findByMessageIdAndChatId(messageId, chatId);

    if (existingCommand == null) {
        BotCommand botCommand = new BotCommand(command, message, chatId, responseMessage, additionalInfo);
        botCommandRepository.save(botCommand);
    } else {
        existingCommand.setCommand(command);
        existingCommand.setMessage(message);
        existingCommand.setChatId(chatId);
        existingCommand.setResponseMessage(responseMessage);
        existingCommand.setAdditionalInfo(additionalInfo);
        botCommandRepository.save(existingCommand);
    }
}

    public BotCommand getCommandByMessageId(Long messageId) {
        return botCommandRepository.findByMessageId(messageId);
    }
}