package com.example.bot.data_base.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations")
@Data
public class DbLocation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long user_id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "date_of_request")
    private LocalDate date;

    public void SetDate() {
        this.date = LocalDate.now();
    }

    public DbLocation(){}

    public DbLocation(long user_id, double latitude, double longitude) {
        this.user_id = user_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = LocalDate.now();
    }
}
