package com.pandrewkk.whiteorchard.repository;

import com.pandrewkk.whiteorchard.model.Location;
import com.pandrewkk.whiteorchard.model.WeatherRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRecordRepository extends JpaRepository<WeatherRecord, Long> {
    List<WeatherRecord> findAllByLocationOrderByDateTimeDesc(Location location);
}
