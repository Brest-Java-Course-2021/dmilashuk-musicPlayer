package com.epam.brest.annotations;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

public class SQLInjectFieldCallback implements ReflectionUtils.FieldCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(SQLInjectFieldCallback.class);

    private final Object bean;

    public SQLInjectFieldCallback(Object bean) {
        this.bean = bean;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (!field.isAnnotationPresent(InjectSQL.class)){
            return;
        }
        String path = field.getAnnotation(InjectSQL.class).path();
        String sql;
        ReflectionUtils.makeAccessible(field);
        try {
            sql = FileUtils.readFileToString(new ClassPathResource(path).getFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.warn("Unable to read the file {}", path);
            throw new IllegalArgumentException("Unable to read the file" + path, e);
        }
        field.set(bean,sql);
    }

}
