package com.banking.api.resources;

import com.banking.api.domain.Account;
import com.banking.api.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountResource {

    @Autowired
    AccountService accountService;

    @GetMapping(" ")
    public ResponseEntity<List<Account>> getAllAccounts(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        List<Account> accounts = accountService.fetchAllAccounts(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(HttpServletRequest request,
                                                    @PathVariable("id") Long id){
        Long userId = (Long) request.getAttribute("id");
        Account account = accountService.fetchAccountById(userId, id);
        return  new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping(" ")
    public ResponseEntity<Account> addAccount(HttpServletRequest request,
                                              @RequestBody Map<String, Object> accountMap){
        Long userId = (Long) request.getAttribute("id");
        String description = (String) accountMap.get("description");
        Double balance = (Double) accountMap.get("balance");
        String accountType = (String) accountMap.get("accountType");
        String accountStatusType = (String) accountMap.get("accountStatusType");
        Date date= new Date();
        long time = date.getTime();
        Timestamp created_date = new Timestamp(time);
        Account account = accountService.addAccount(userId, description, balance, accountType,
                accountStatusType, created_date);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> updateAccount(HttpServletRequest request,
                                                               @PathVariable("id") Long id,
                                                               @RequestBody Account account){
        Long userId = (Long) request.getAttribute("id");
        accountService.updateAccount(userId, id, account);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAccount(HttpServletRequest request,
                                                               @PathVariable("id") Long id){
        Long userId = (Long) request.getAttribute("id");
        accountService.removeAccount(userId, id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
