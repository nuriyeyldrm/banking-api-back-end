package com.banking.api.resources;

import com.banking.api.domain.Transfer;
import com.banking.api.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfers")
public class TransferResource {

    @Autowired
    TransferService transferService;

    @GetMapping(" ")
    public ResponseEntity<List<Transfer>> getAllTransfers(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        List<Transfer> transfers = transferService.fetchAllTransfers(userId);
        return new ResponseEntity<>(transfers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transfer> getTransferById(HttpServletRequest request,
                                                  @PathVariable("id") Long id){
        Long userId = (Long) request.getAttribute("id");
        Transfer transfer = transferService.fetchTransferById(userId, id);
        return  new ResponseEntity<>(transfer, HttpStatus.OK);
    }

    @PostMapping(" ")
    public ResponseEntity<Transfer> createTransfer(HttpServletRequest request,
                                                @RequestBody Map<String, Object> transferMap){
        Long userId = (Long) request.getAttribute("id");
        Long fromAccountId = ((Number) transferMap.get("fromAccountId")).longValue();
        Long toAccountId = ((Number) transferMap.get("toAccountId")).longValue();
        Double transactionAmount = (Double) transferMap.get("transactionAmount");
        String currencyCode = (String) transferMap.get("currencyCode");
        String description = (String) transferMap.get("description");
        Date date= new Date();
        long time = date.getTime();
        Timestamp transactionDate = new Timestamp(time);
        Transfer transfer = transferService.addTransfer(fromAccountId, toAccountId, userId, transactionAmount,
                currencyCode, transactionDate, description);
        return new ResponseEntity<>(transfer, HttpStatus.CREATED);
    }
}
