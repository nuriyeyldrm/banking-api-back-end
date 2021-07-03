package com.banking.api.resources;

import com.banking.api.domain.Account;
import com.banking.api.domain.enumeration.AccountStatusType;
import com.banking.api.domain.enumeration.AccountType;
import com.banking.api.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountResource {

    @Autowired
    AccountService accountService;

    @PostMapping(" ")
    public ResponseEntity<Account> addAccount(HttpServletRequest request,
                                              @RequestBody Map<String, Object> accountMap){
        int userId = (Integer) request.getAttribute("user_id");
        String description = (String) accountMap.get("description");
        Integer balance = (Integer) accountMap.get("balance");
        String accountType = (String) accountMap.get("account_type");
        String accountStatusType = (String) accountMap.get("account_status_type");
        Date date= new Date();
        long time = date.getTime();
        Timestamp created_date = new Timestamp(time);
        Account account = accountService.addAccount(userId, description, balance, accountType,
                accountStatusType, created_date);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }
}
