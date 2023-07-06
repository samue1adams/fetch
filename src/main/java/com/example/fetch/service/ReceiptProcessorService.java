package com.example.fetch.service;

import com.example.fetch.model.Item;
import com.example.fetch.model.PointsResponse;
import com.example.fetch.model.Receipt;
import com.example.fetch.model.ReceiptResponse;
import com.example.fetch.repository.ReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReceiptProcessorService {

    private final ReceiptRepository repository;

    @Autowired
    public ReceiptProcessorService(ReceiptRepository repository) {
        this.repository = repository;
    }

    public ReceiptResponse saveReceipt(Receipt receipt) {
        if (receipt != null) {

            receipt.setTotal(getTotal(receipt.getItems()));

            int points = processPoints(receipt);

            receipt.setPoints(points);

            repository.save(receipt);

            return ReceiptResponse.builder().id(receipt.getId()).build();
        }

       return null;
    }

    public Receipt retrieveReceipt(Long id) {
        Optional<Receipt> receipt = repository.findById(id);
        return receipt.orElse(null);
    }

    public PointsResponse getPointsById(Long id){
        Optional<Receipt> optionalReceipt = repository.findById(id);
        return optionalReceipt.map(receipt -> PointsResponse.builder().points(receipt.getPoints()).build()).orElse(null);
    }

    public int processPoints(Receipt receipt) {
        int points = 0;

        points += getAlphanumericPoints(receipt.getRetailer()); // points for length of retailer name (no special chars)

        int pairs = receipt.getItems().size() / 2; //int rounds down to nearest whole number.
        points += pairs * 5; // points for every 2 items

        for (Item item : receipt.getItems()) { // points for description is a multiple of 3
            String trimmedItemDescription = item.getShortDescription().trim();
            if (trimmedItemDescription.length() % 3 == 0) {
                double addedPoints = Math.ceil(trimmedItemDescription.length() * 0.2);
                points += addedPoints;
            }
        }

        if (receipt.getTotal().remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) { // points if total ends in 00
            points += 50;
        }
        if (receipt.getTotal().remainder(new BigDecimal("0.25")).compareTo(BigDecimal.ZERO) == 0) { // points if multiple of .25
            points += 25;
        }

        if (receipt.getPurchaseDate().getDayOfMonth() % 2 != 0) { //points if purchase date is odd
            points += 6;
        }

        if (receipt.getPurchaseTime().isAfter(LocalTime.of(14, 0, 0)) && receipt.getPurchaseTime().isBefore(LocalTime.of(16, 0, 0))) { // points if purchase between 2 and 4 pm
            points += 10;
        }
        return points;
    }


    private BigDecimal getTotal(List<Item> items) {
        return items.stream()
                .map(Item::getPrice)
                .collect(Collectors.toList())
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int getAlphanumericPoints(String retailer){
        List<Character> lettersAndDigits = new ArrayList<>();
        for (char c : retailer.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                lettersAndDigits.add(c);
            }
        }
        return lettersAndDigits.size();
    }

}
