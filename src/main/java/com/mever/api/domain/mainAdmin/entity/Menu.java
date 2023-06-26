package com.mever.api.domain.mainAdmin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, unique = true)
    int seq;
    @Column(name = "menu_index",nullable = true)
    String menuIndex;
    @Column(name = "url",nullable = true)
    String url;
    @Column(name = "menu_name",nullable = true)
    String menuName;
    @Column(name = "use_yn",nullable = true)
    String useYn;
    @Column(name = "music",nullable = true)
    String music;
    @Column(name = "matterLink",nullable = true)
    String matterLink;
    @Column(name = "order_num",nullable = true)
    String orderNum;
    @Column(name = "insert_date",nullable = true)
    String insertDate;
    @Column(name = "map",nullable = true)
    String map;
    @Column(name = "site_category",nullable = true)
    String siteCategory;
}

