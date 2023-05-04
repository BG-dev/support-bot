package bot.SupportBot.service;

import bot.SupportBot.entity.Update;
import bot.SupportBot.entity.User;
import bot.SupportBot.repository.UpdateRepository;
import bot.SupportBot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(Message msg){

        if(userRepository.findById(msg.getChatId()).isEmpty()){
            long chatId = msg.getChatId();
            Chat chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setPhoneNumber(msg.getContact().getPhoneNumber());
            user.setUsername(chat.getUserName());

            userRepository.save(user);
        }
    }

}
