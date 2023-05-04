package bot.SupportBot.service;

import bot.SupportBot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    @Autowired
    private UpdateService updateService;
    @Autowired
    private UserService userService;

    public TelegramBot(BotConfig config){

        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "user registration"));
        listOfCommands.add(new BotCommand("/help", "help"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            System.out.print(e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasContact()){
            userService.saveUser(update.getMessage());
        }

        if(update.hasMessage() && update.getMessage().hasText()){

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case "/start":

                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case "/help":
                    sendMessage(chatId, "Помощь в работе с ботом", null);
                    break;
            }
            updateService.saveMessage(update.getMessage(), false);
        }
    }


    public void startCommandReceived(long chatId, String name){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow keyboardRow = new KeyboardRow();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardButton shareContactButton = new KeyboardButton();
        shareContactButton.setText("Share contact");
        shareContactButton.setRequestContact(true);
        keyboardRow.add(shareContactButton);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        sendMessage(chatId, "Отправьте свой номер телефона для регистрации", replyKeyboardMarkup);

    }

    public bot.SupportBot.entity.Update sendMessage(long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup){
        SendMessage message = new SendMessage();
        message.setChatId(valueOf(chatId));
        message.setText(text);

        if(replyKeyboardMarkup != null){
            message.setReplyMarkup(replyKeyboardMarkup);
        }


        try {
            Message msg = execute(message);
            bot.SupportBot.entity.Update update = updateService.saveMessage(msg, true);
            return update;
        } catch (TelegramApiException e){
            return null;
        }
    }


}
