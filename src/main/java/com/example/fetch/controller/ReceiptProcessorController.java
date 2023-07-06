package com.example.fetch.controller;

import com.example.fetch.model.PointsResponse;
import com.example.fetch.model.Receipt;
import com.example.fetch.model.ReceiptResponse;
import com.example.fetch.service.ReceiptProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class ReceiptProcessorController {

    private final ReceiptProcessorService receiptProcessorService;


    @Autowired
    public ReceiptProcessorController(ReceiptProcessorService receiptProcessorService) {
        this.receiptProcessorService = receiptProcessorService;
    }

    @PostMapping("receipts/process")
    public ReceiptResponse saveReceipt(@RequestBody Receipt receipt) {
        return receiptProcessorService.saveReceipt(receipt);
    }

    @GetMapping("receipts/{id}") // was using this endpoint for testing and decided to leave it
    public Receipt getReceipt(@PathVariable Long id){
        return receiptProcessorService.retrieveReceipt(id);
    }

    @GetMapping("receipts/{id}/points")
    public PointsResponse getPoints(@PathVariable Long id){
        return receiptProcessorService.getPointsById(id);
    }

}
