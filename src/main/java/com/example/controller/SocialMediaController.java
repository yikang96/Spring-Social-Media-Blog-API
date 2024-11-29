package com.example.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.*;
import com.example.service.AccountService;
import com.example.service.MessageService;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Account account){
       try {
        Account registerAccount = accountService.persistAccount(account);
        return ResponseEntity.ok(registerAccount);
       } catch(DuplilcateUsernameException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
       }catch (InvalidUsernameException | InvalidPasswordException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
       }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginDetails){
        try{
            String username = loginDetails.get("username");
            String password = loginDetails.get("password");

            Account account = accountService.login(username, password);
            return ResponseEntity.ok(account);
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
    

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            Message addMessage = messageService.persistMessage(message);
            return ResponseEntity.ok(addMessage);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
   }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId){
        Message message = messageService.getMessagesById(messageId);
        if (message == null){
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok(message);
    }
    
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId){
        boolean isDeleted = messageService.deleteMessageById(messageId);

        if(isDeleted) {
            return ResponseEntity.ok(1);
        }

        return ResponseEntity.ok().build();
    }


    
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessageText(@PathVariable int messageId, @RequestBody Map<String, String> request) {
        String newMessageText = request.get("messageText");
        int rowsUpdated = messageService.updateMessageById(messageId, newMessageText);

        if(rowsUpdated > 0) {
            return ResponseEntity.ok(rowsUpdated);
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Message not found or no changes made."));
        }

    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByPostedBy(@PathVariable int accountId){
        List<Message> messages = messageService.getMessagesByPostBy(accountId);
        return ResponseEntity.ok(messages);
    }

}
