package com.example.usermanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(of = "id")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    private String passwordHash;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
} 