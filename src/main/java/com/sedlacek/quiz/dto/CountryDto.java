package com.sedlacek.quiz.dto;

import com.sedlacek.quiz.model.Continent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto implements Serializable {
    private String flagCode;
    private String countryName;
    private String capitalName;
    private Continent continent;
}
