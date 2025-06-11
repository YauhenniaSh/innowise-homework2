package com.example.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = {"address", "company"})
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
} 