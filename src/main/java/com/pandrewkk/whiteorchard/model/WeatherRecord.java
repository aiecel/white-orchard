package com.pandrewkk.whiteorchard.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "WEATHER_RECORDS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private ZonedDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID", nullable = false)
    private Location location;

    private double temperature;

    private String condition;

    private double cloudness;

    private boolean isThunder;

    private double windSpeed;

    private String windDir;

    private double pressure;

    private double humidity;

    private String daytime;
}
