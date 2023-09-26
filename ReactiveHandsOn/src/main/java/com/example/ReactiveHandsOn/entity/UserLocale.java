package com.example.ReactiveHandsOn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLocale {
    private String languageCode;
    private String countryCode;
}
