package umg.edu.gt.Telebot.GPT.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umg.edu.gt.Telebot.GPT.Model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
