package com.sedlacek.quiz.entity;

import com.sedlacek.quiz.model.Continent;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
public class Country implements Serializable {
    @Id
    private String flagCode;
    private String countryName;
    private String capitalName;
    private Continent continent;
}
