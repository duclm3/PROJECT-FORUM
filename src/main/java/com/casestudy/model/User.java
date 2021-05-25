package com.casestudy.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

//    @NotBlank
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

//    @Column(nullable = true)
    private String avatar = "U.svg";

    @Column(nullable = true)
    private String level = "New member";

    @Email
    @NotBlank
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    private LocalDateTime dateCreate = LocalDateTime.now();

    public User() {
    }

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public User(Long id, String username, String password, String fullName, String avatar, String level, Set<Role> roles, LocalDateTime dateCreate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.level = level;
        this.roles = roles;
        this.dateCreate = dateCreate;
    }

    public User(Long id, String username, String password, String fullName, String avatar, String level, LocalDateTime dateCreate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.avatar = avatar;
        this.level = level;
        this.dateCreate = dateCreate;
    }

    public User(String username, String password, String fullName, String fileName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.avatar = fileName;
    }

    public User(String username, String password, String fullName, String email, String fileName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.avatar = fileName;
    }

    public User(Long id, String username, String password, String fullName, String fileName, String level, LocalDateTime dateCreate, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.avatar = fileName;
        this.level = level;
        this.dateCreate = dateCreate;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public LocalDateTime getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getSimpleDate(){
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formatDateTime = this.dateCreate.format(format);
            return formatDateTime;
        }catch (Exception e){
            return null;
        }
    }
}