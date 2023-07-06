package com.example.fetch.service;


import com.example.fetch.model.Item;
import com.example.fetch.model.Receipt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReceiptProcessorServiceTest {


    @InjectMocks
    ReceiptProcessorService service;

    @Test
    public void processPoints(){
        Receipt receipt = Receipt.builder()
                .retailer("REI")
                .purchaseDate(LocalDate.of(2022, 1,1))
                .purchaseTime(LocalTime.of(15, 0))
                .items(asList(Item.builder().shortDescription("Mountain Dew 12PK").price(new BigDecimal("6.49")).build(),
                        Item.builder().shortDescription("Emils Cheese Pizza").price(new BigDecimal("12.25")).build(),
                        Item.builder().shortDescription("Knorr Creamy Chicken").price(new BigDecimal("1.26")).build(),
                        Item.builder().shortDescription("Doritos Nacho Cheese").price(new BigDecimal("3.00")).build(),
                        Item.builder().shortDescription("   Klarbrunn 12-PK 12 FL OZ  ").price(new BigDecimal("12.00")).build()))
                .total(new BigDecimal("35.00"))
                .build();
       int actual = service.processPoints(receipt);

       assertThat(actual).isEqualTo(113);

    }

    @Test
    public void processPoints_RetailerNameContainsSpecialChar(){
        Receipt receipt = Receipt.builder()
                .retailer("RE!")
                .purchaseDate(LocalDate.of(2022, 1,1))
                .purchaseTime(LocalTime.of(15, 0))
                .items(asList(Item.builder().shortDescription("Mountain Dew 12PK").price(new BigDecimal("6.49")).build(),
                        Item.builder().shortDescription("Emils Cheese Pizza").price(new BigDecimal("12.25")).build(),
                        Item.builder().shortDescription("Knorr Creamy Chicken").price(new BigDecimal("1.26")).build(),
                        Item.builder().shortDescription("Doritos Nacho Cheese").price(new BigDecimal("3.00")).build(),
                        Item.builder().shortDescription("   Klarbrunn 12-PK 12 FL OZ  ").price(new BigDecimal("12.00")).build()))
                .total(new BigDecimal("35.00"))
                .build();
        int actual = service.processPoints(receipt);

        assertThat(actual).isEqualTo(112);

    }

    @Test
    public void processPoints_whenTotalIsNotWholeNumber() {
        Receipt receipt = Receipt.builder()
                .retailer("REI")
                .purchaseDate(LocalDate.of(2022, 1, 1))
                .purchaseTime(LocalTime.of(15, 0))
                .items(asList(Item.builder().shortDescription("Mountain Dew 12PK").price(new BigDecimal("6.49")).build(),
                        Item.builder().shortDescription("Emils Cheese Pizza").price(new BigDecimal("12.25")).build(),
                        Item.builder().shortDescription("Knorr Creamy Chicken").price(new BigDecimal("1.26")).build(),
                        Item.builder().shortDescription("Doritos Nacho Cheese").price(new BigDecimal("3.00")).build(),
                        Item.builder().shortDescription("   Klarbrunn 12-PK 12 FL OZ  ").price(new BigDecimal("12.35")).build()))
                .total(new BigDecimal("35.35"))
                .build();
        int actual = service.processPoints(receipt);
        assertThat(actual).isEqualTo(38);
    }

    @Test
    public void processPoints_whenPurchaseDateAndTimeAreNotInRange() {
        Receipt receipt = Receipt.builder()
                .retailer("REI")
                .purchaseDate(LocalDate.of(2022, 1, 2))
                .purchaseTime(LocalTime.of(13, 0))
                .items(asList(Item.builder().shortDescription("Mountain Dew 12PK").price(new BigDecimal("6.49")).build(),
                        Item.builder().shortDescription("Emils Cheese Pizza").price(new BigDecimal("12.25")).build(),
                        Item.builder().shortDescription("Knorr Creamy Chicken").price(new BigDecimal("1.26")).build(),
                        Item.builder().shortDescription("Doritos Nacho Cheese").price(new BigDecimal("3.00")).build(),
                        Item.builder().shortDescription("   Klarbrunn 12-PK 12 FL OZ  ").price(new BigDecimal("12.35")).build()))
                .total(new BigDecimal("35.35"))
                .build();
        int actual = service.processPoints(receipt);
        assertThat(actual).isEqualTo(22);
    }

}