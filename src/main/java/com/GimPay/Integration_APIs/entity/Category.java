package com.GimPay.Integration_APIs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    public Category() {}

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Category c = new Category();
        public Builder name(String v) { c.name = v; return this; }
        public Builder description(String v) { c.description = v; return this; }
        public Category build() { return c; }
    }
}