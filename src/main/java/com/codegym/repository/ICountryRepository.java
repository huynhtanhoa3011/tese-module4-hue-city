package com.codegym.repository;

import com.codegym.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICountryRepository extends JpaRepository<Country, Long> {
}
