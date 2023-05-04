package bot.SupportBot.entity;

import jakarta.persistence.*;

@Entity(name = "updates")
public class Update {

    @Id
    private long updateId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chatId", referencedColumnName = "chatId")
    private User user;
    private String text;
    private long date;
    private boolean isBot;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(long updateId) {
        this.updateId = updateId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean getIsBot() {
        return isBot;
    }

    public void setIsBot(boolean bot) {
        isBot = bot;
    }
}
