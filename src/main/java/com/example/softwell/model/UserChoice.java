package com.example.softwell.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "userChoise")
public class UserChoice {

    @Id
    private String id;
    private String userId;
    private String selectedOption;
    private LocalDateTime selectedData;

}
