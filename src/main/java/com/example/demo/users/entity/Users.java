package com.example.demo.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Data
@Table(name = "users")
public class Users  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  private String name;
  private String mobile;
  private String address;

//  @NotNull
//  @ColumnDefault("CURRENT_TIMESTAMP")
//  @Column(name = "created_at", nullable = false)
//  private Instant createdAt;
//
//  @NotNull
//  @ColumnDefault("CURRENT_TIMESTAMP")
//  @Column(name = "updated_at", nullable = false)
//  private Instant updatedAt;
//
//  @Column(name = "deleted_at")
//  private Instant deletedAt;
//
//  @PrePersist
//  public void prePersist() {
//    this.createdAt = Instant.now();
//    this.updatedAt = Instant.now();
//  }
//
//  @PreUpdate
//  public void preUpdate() {
//    this.updatedAt = Instant.now();
//  }
//
//  @PreRemove
//  public void preRemove() {
//    this.deletedAt = Instant.now();
//  }
}