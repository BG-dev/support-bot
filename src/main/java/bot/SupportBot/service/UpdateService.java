package bot.SupportBot.service;

import bot.SupportBot.entity.Update;
import bot.SupportBot.entity.User;
import bot.SupportBot.repository.UpdateRepository;
import bot.SupportBot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;


@Service
public class UpdateService {

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Update> getAll(){
       return (List<Update>) updateRepository.findAll();
    }

    public Update saveMessage(Message msg, boolean isBot){
        Update update = new Update();
        User user = userRepository.findById(msg.getChatId()).orElseThrow();

        update.setUpdateId(msg.getMessageId());
        update.setText(msg.getText());
        update.setUser(user);
        update.setDate(msg.getDate());
        update.setIsBot(isBot);

        updateRepository.save(update);

        return update;
    }

}
