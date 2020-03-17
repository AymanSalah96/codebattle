package com.aymansalah.codebattle.models;

import com.aymansalah.codebattle.annotation.UniqueEmail;
import com.aymansalah.codebattle.annotation.UniqueUsername;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, message = "Username size must be at least 3 characters")
    @Size(max = 128, message = "Username size cannot exceed 128 characters")
    @UniqueUsername(message = "This username already exists")
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password size must be at least 8 characters")
    @Size(max = 100, message = "Password size cannot exceed 100 characters")
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Enter a valid email")
    @Size(max = 128, message = "Email size cannot exceed 128 characters")
    @UniqueEmail(message = "This email already exists")
    private String email;

    @Column(name = "first_name", nullable = false)
    @NotNull(message = "First name cannot be null")
    @NotBlank(message = "First name cannot be empty")
    @Size(max = 128, message = "First name size cannot exceed 128 characters")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotNull(message = "Last name cannot be null")
    @NotBlank(message = "Last name cannot be empty")
    @Size(max = 128, message = "Last name size cannot exceed 128 characters")
    private String lastName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "city")
    @Size(max = 128, message = "City size cannot exceed 128 characters")
    private String city;

    @Column(name = "country_code")
    @Size(max = 2, message = "Country size cannot exceed two characters")
    private String countryCode;

    @Column(name = "institute")
    @Size(max = 128, message = "Institute size cannot exceed 128 characters")
    private String institute;

    @Column(name = "rating", columnDefinition = "integer default 0")
    private int rating;

    @Column(name = "global_rank", columnDefinition = "integer default 0")
    private int globalRank;

    @Column(name = "local_rank", columnDefinition = "integer default 0")
    private int localRank;

    @Column(name = "solved_count", columnDefinition = "integer default 0")
    private int solvedCount;
}
