package com.pandrewkk.whiteorchard.repository;

import com.pandrewkk.whiteorchard.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByNameIgnoreCase(String name);
}
