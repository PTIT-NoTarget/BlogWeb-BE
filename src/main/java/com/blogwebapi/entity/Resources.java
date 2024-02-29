package com.blogwebapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Table(name = "Resources")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
