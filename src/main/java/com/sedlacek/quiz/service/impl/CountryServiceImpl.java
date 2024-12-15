package com.sedlacek.quiz.service.impl;

import com.sedlacek.quiz.entity.Country;
import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.repository.CountryRepository;
import com.sedlacek.quiz.service.CountryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;


    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }


    @Override
    public Country getCountryByFlagCode(String flagCode) {
        return countryRepository.getCountryByFlagCode(flagCode);
    }

    @Override
    public Country getCountryByCountryName(String countryName) {
        return countryRepository.getCountryByCountryName(countryName);
    }

    @Override
    public Country getCountryByCapitalName(String capitalName) {
        return countryRepository.getCountryByCapitalName(capitalName);
    }

    @Override
    public List<Country> getCountriesByContinent(Continent continent) {
        return countryRepository.getCountriesByContinent(continent);
    }

    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }
}
