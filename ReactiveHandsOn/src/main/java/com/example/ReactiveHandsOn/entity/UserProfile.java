package com.example.ReactiveHandsOn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

     @Id
     private String altCustId;
     private Boolean isMember;
     private UserLocale userLocale;

}
