package com.casestudy.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateId;

    @NotBlank
    @Size(max = 50)
    private String cateName;

    private String color;

    public Category() {
    }

    public Category(Long cateId, @NotBlank @Size(max = 50) String cateName, String color) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.color = color;
    }

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long id) {
        this.cateId = id;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
