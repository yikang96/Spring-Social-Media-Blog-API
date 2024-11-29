package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplilcateUsernameException;
import com.example.exception.InvalidPasswordException;
import com.example.exception.InvalidUsernameException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account persistAccount(Account account){
        if (accountRepository.existsByUsername(account.getUsername())){
            throw new DuplilcateUsernameException("Username already exists.");
        }

        if(account.getUsername() == null || account.getUsername().isBlank()){
            throw new InvalidUsernameException("Username cannot be blank.");
        }

        if(account.getPassword().length() < 4){
            throw new InvalidPasswordException ("Password must be at least 4 characters long.");
        }
        return accountRepository.save(account);
    }

    public Account login(String username, String password){
        Account account = accountRepository.findByUsername(username)
            .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));;
        
        if(!account.getPassword().equals(password)) {
            throw new UnauthorizedException("Invalid username or password.");
        }

        return account;
    }
   
}
