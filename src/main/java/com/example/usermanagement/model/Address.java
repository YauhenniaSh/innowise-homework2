package com.example.usermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geo_id", referencedColumnName = "id")
    private Geo geo;
} 