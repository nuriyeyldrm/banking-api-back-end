package com.banking.api.resources;

import com.banking.api.domain.Transaction;
import com.banking.api.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories/{category_id}/transactions")
public class TransactionResource {

    @Autowired
    TransactionService transactionService;

    @GetMapping(" ")
    public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest request,
                                                                @PathVariable("category_id") Integer categoryId){
        int userId = (Integer) request.getAttribute("user_id");
        List<Transaction> transactions = transactionService.fetchAllTransactions(userId, categoryId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @GetMapping("{transaction_id}")
    public ResponseEntity<Transaction> getTransactionById(HttpServletRequest request,
                                                          @PathVariable("category_id") Integer categoryId,
                                                          @PathVariable("transaction_id") Integer transactionId){
        int userId = (Integer)  request.getAttribute("user_id");
        Transaction transaction = transactionService.fetchTransactionById(userId, categoryId, transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping(" ")
    public ResponseEntity<Transaction> addTransaction(HttpServletRequest request,
                                                      @PathVariable("category_id") Integer categoryId,
                                                      @RequestBody Map<String, Object> transactionMap){
        int userId = (Integer) request.getAttribute("user_id");
        Double amount = Double.valueOf(transactionMap.get("amount").toString());
        String note = (String) transactionMap.get("note");
        Long transactionDate = (Long) transactionMap.get("transaction_date");
        Transaction transaction = transactionService.addTransaction(userId, categoryId, amount, note, transactionDate);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PutMapping("/{transaction_id}")
    public ResponseEntity<Map<String, Boolean>> updateTransaction(HttpServletRequest request,
                                                                  @PathVariable("category_id") Integer categoryId,
                                                                  @PathVariable("transaction_id") Integer transactionId,
                                                                  @RequestBody Transaction transaction){
        int userId = (Integer) request.getAttribute("user_id");
        transactionService.updateTransaction(userId, categoryId, transactionId, transaction);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{transaction_id}")
    public ResponseEntity<Map<String, Boolean>> deleteTransaction(HttpServletRequest request,
                                                                  @PathVariable("category_id") Integer categoryId,
                                                                  @PathVariable("transaction_id") Integer transactionId){
        int userId = (Integer) request.getAttribute("user_id");
        transactionService.removeTransaction(userId, categoryId, transactionId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
