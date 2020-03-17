package com.aymansalah.codebattle.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "auth_user_group")
public class AuthGroup {
    @Id
    @Column(name = "auth_user_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "auth_group")
    private String authGroup;

    public AuthGroup() {}

    public AuthGroup(String username, String authGroup) {
        this.username = username;
        this.authGroup = authGroup;
    }
}
