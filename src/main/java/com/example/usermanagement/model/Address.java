package com.example.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "geo")
@EqualsAndHashCode(of = "id")
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