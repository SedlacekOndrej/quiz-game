package com.sedlacek.quiz.controller;

import com.sedlacek.quiz.dto.CountryDto;
import com.sedlacek.quiz.entity.Country;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.model.Continent;
import com.sedlacek.quiz.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@SuppressWarnings("unused")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/flagCode/{flagCode}")
    public ResponseEntity<CountryDto> getCountryByFlagCode(@PathVariable(value = "flagCode") String flagCode) {
        Country country = countryService.getCountryByFlagCode(flagCode);
        CountryDto countryDto = EntityBase.convert(country, CountryDto.class);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping("/countryName/{countryName}")
    public ResponseEntity<CountryDto> getCountryByCountryName(@PathVariable(value = "countryName") String countryName) {
        Country country = countryService.getCountryByCountryName(countryName);
        CountryDto countryDto = EntityBase.convert(country, CountryDto.class);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping("/capitalName/{capitalName}")
    public ResponseEntity<CountryDto> getCountryByCapitalName(@PathVariable(value = "capitalName") String capitalName) {
        Country country = countryService.getCountryByCapitalName(capitalName);
        CountryDto countryDto = EntityBase.convert(country, CountryDto.class);
        return ResponseEntity.ok(countryDto);
    }

    @GetMapping("/continent/{continent}")
    public ResponseEntity<List<CountryDto>> getCountriesByContinent(@PathVariable(value = "continent") Continent continent) {
        List<Country> countries = countryService.getCountriesByContinent(continent);
        List<CountryDto> countriesDto = EntityBase.convertAll(countries, CountryDto.class);
        return ResponseEntity.ok(countriesDto);
    }

    @GetMapping
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        List<Country> countries = countryService.getAllCountries();
        List<CountryDto> countriesDto = EntityBase.convertAll(countries, CountryDto.class);
        return ResponseEntity.ok(countriesDto);
    }
}
