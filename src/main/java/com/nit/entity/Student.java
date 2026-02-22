package com.nit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String rollNumber;
    private String email;
    private String contactNumber;
    private String degree;
    private String stream;
    private String currentLocation;
    private String nativeLocation;

    private String imagePath;
    private String resumePath;
}
