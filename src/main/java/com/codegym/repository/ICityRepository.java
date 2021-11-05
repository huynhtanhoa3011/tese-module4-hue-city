package com.codegym.repository;

import com.codegym.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICityRepository extends JpaRepository<City, Long> {
    Boolean existsByName(String name);
}
