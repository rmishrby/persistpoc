package com.example.ReactiveHandsOn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineItem {
    private String offerId;
    private String startDate;
    private String endDate;
    private String productType;
}
