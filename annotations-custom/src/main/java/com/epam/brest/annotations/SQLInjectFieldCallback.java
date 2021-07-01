package com.epam.brest.annotations;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
        String path = field.getAnnotation(InjectSQL.class).value();
        String sql;
        ReflectionUtils.makeAccessible(field);
        try (Reader reader = new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)) {
            sql = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            LOGGER.warn("Unable to read the file {}", path);
            throw new IllegalArgumentException("Unable to read the file" + path, e);
        }
        field.set(bean, sql);
    }
}
