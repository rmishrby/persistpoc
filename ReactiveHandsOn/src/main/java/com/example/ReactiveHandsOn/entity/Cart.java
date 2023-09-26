package com.example.ReactiveHandsOn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Cart {

    private UserProfile userProfile;
    private List<LineItem> lineItem;


}
