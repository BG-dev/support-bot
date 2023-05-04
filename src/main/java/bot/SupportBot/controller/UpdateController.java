package bot.SupportBot.controller;

import bot.SupportBot.dto.UpdateDTO;
import bot.SupportBot.entity.Update;
import bot.SupportBot.service.TelegramBot;
import bot.SupportBot.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class UpdateController {

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private UpdateService updateService;

    @PostMapping
    public ResponseEntity<Update> create(@RequestBody UpdateDTO dto){
        return  new ResponseEntity<>(telegramBot.sendMessage(dto.getChatId(), dto.getText(), null), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Update>> getAll(){
        return new ResponseEntity<>(updateService.getAll(), HttpStatus.OK);
    }
}
