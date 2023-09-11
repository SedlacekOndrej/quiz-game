package com.sedlacek.quiz.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.modelmapper.ModelMapper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.OffsetDateTime;

@MappedSuperclass
@Getter
public abstract class EntityBase implements Serializable {

    private static final ModelMapper mapper = new ModelMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final OffsetDateTime createdDate = OffsetDateTime.now();


    public static <T, E> E convert(T source, Class<E> destinationClass) {
        return mapper.map(source, destinationClass);
    }

    public static void update(Object source, Object target) throws IllegalAccessException {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target objects must not be null.");
        }

        Class<?> sourceClass = source.getClass();

        Field[] fields = sourceClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Object fieldValue = field.get(source);

            if (fieldValue != null) {
                field.set(target, fieldValue);
            }
        }
    }

}
