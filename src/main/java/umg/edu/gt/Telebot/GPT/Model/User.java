package umg.edu.gt.Telebot.GPT.Model;

public class User {
    
    private Long chatId;
    private String name;
    private String languagePreference;
    
    public User(Long chatId, String name) {
        this.chatId = chatId;
        this.name = name;
        this.languagePreference = "es";
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }
    
}
