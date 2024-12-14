package com.sedlacek.quiz.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@MappedSuperclass
@Getter
public abstract class EntityBase implements Serializable {
    private static final ModelMapper mapper = new ModelMapper();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private final OffsetDateTime createdDate = OffsetDateTime.now();


    public static <S, T> T convert(S source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

    public static <S, T> List<T> convertAll(Collection<S> source, Class<T> targetClass) {
        List<T> collection = new ArrayList<>();
        for (Object s : source) {
            collection.add(mapper.map(s, targetClass));
        }
        return collection;
    }
}
