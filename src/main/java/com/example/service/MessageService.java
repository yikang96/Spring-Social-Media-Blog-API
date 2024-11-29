package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Message persistMessage (Message message){
        if (message.getMessageText() == null || message.getMessageText().isBlank()){
            throw new IllegalArgumentException("Message text cannot be blank");
        }

        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }

        if(message.getPostedBy() == null || !accountRepository.existsById(message.getPostedBy())){
            throw new IllegalArgumentException("Invalid postedBy account.");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessagesById(int messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    public boolean deleteMessageById(int messageId){
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }

        return false;
    }

    public int updateMessageById(int messageId, String newMessageText){
        if(newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255 ){
            return -1;
        }
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if(messageOptional.isPresent()){
            Message message = messageOptional.get();
            message.setMessageText(newMessageText);
            messageRepository.save(message);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessagesByPostBy(int postedBy){
       return messageRepository.findMessagesByPostedBy(postedBy);
    }

}
