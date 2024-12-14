package com.sedlacek.quiz.service;

import com.sedlacek.quiz.entity.Country;
import com.sedlacek.quiz.model.Continent;

import java.util.List;

public interface CountryService {
    Country getCountryByFlagCode(String flagCode);

    Country getCountryByCountryName(String countryName);

    Country getCountryByCapitalName(String capitalName);

    List<Country> getCountriesByContinent(Continent continent);

    List<Country> getAllCountries();
}
