package com.mever.api.domain.email.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "send_history")
public class SendHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long seq;
    @Column()
    String title;
    @Column()
    String email;
    @Column()
    String content;
    @Column()
    String phone;
    @Column()
    String type;
    @Column()
    String date;

}