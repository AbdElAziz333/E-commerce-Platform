package com.aziz.user_service.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Builder
@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
public class PendingUser {
    @Id
    private String id;

    @Field(name = "first_name")
    private String firstName;

    @Field(name = "last_name")
    private String lastName;

    @Field(name = "email")
    @Indexed(unique = true)
    private String email;

    @Field(name = "password")
    private String password;

    @Field(name = "phone_number")
    private String phoneNumber;

    @Indexed(expireAfter = "2m")
    @CreatedDate
    private String registeredAt;
}