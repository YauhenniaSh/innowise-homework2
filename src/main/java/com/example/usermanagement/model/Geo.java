package com.example.usermanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Geo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String lat;
    private String lng;
} 