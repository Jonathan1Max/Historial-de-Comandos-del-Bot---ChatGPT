package umg.edu.gt.Telebot.GPT.Model;

import java.util.List;

public class ChatGPTResponse {
    private List<Choice> choices;

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}