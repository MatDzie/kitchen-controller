package com.mat.kitchencontroller.domain;

import jakarta.persistence.*;

@Entity
public class Setting {
    @Id
    @Column(name = "setting_id", nullable = false)
    private Integer id;

    @Column(name = "setting_name", unique = true)
    private String name;

    @Column(name = "setting_value")
    private String value;

    public Setting() {
    }

    public Setting(Integer id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public Setting(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
