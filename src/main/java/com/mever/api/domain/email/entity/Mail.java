package com.mever.api.domain.email.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mail")
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day", nullable = false, unique = true)
    private Long day;
    String title;
    String email;
    String content;
    String address;

}