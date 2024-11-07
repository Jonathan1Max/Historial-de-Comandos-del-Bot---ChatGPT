package umg.edu.gt.Telebot.GPT.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umg.edu.gt.Telebot.GPT.Model.BotCommand;

@Repository
public interface BotCommandRepository extends JpaRepository<BotCommand, Long> {

    BotCommand findByMessageIdAndChatId(Long messageId, Long chatId);

    BotCommand findByMessageId(Long messageId);
}
