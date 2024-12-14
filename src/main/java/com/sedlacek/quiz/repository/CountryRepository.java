package com.sedlacek.quiz.repository;

import com.sedlacek.quiz.entity.Country;
import com.sedlacek.quiz.model.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CountryRepository extends JpaRepository<Country, String> {
    Country getCountryByFlagCode(String flagCode);

    Country getCountryByCountryName(String countryName);

    Country getCountryByCapitalName(String capitalName);

    List<Country> getCountriesByContinent(Continent continent);
}
