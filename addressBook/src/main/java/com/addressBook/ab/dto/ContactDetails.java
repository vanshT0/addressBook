package com.addressBook.ab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactDetails {
    private String id;
    private String name;
    private String phone;
    private String email;
}
