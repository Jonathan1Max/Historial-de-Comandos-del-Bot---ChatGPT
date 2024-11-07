package umg.edu.gt.Telebot.GPT.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<BotCommand> botCommands;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public Message(Long id, String text) {
        this.id = id;
        this.text = text;
    }
}