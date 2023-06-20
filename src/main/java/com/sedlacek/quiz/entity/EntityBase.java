package com.sedlacek.quiz.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.modelmapper.ModelMapper;


import java.io.Serializable;

@MappedSuperclass
@Getter
public abstract class EntityBase implements Serializable {

    private static final ModelMapper mapper = new ModelMapper();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public static <T, E> E convert(T source, Class<E> destinationClass) {
        return mapper.map(source, destinationClass);
    }

}
