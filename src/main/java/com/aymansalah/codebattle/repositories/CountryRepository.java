package com.aymansalah.codebattle.repositories;

import com.aymansalah.codebattle.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
    Country findByCode(String code);
}
