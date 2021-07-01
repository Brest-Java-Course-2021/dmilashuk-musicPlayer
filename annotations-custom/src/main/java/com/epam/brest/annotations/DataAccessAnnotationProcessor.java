package com.epam.brest.annotations;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class DataAccessAnnotationProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        this.scanDataAccessAnnotation(bean);
        return bean;
    }

    protected void scanDataAccessAnnotation(Object bean) {
        this.configureFieldInjection(bean);
    }

    private void configureFieldInjection(Object bean) {
        Class<?> managedBeanClass = bean.getClass();
        ReflectionUtils.FieldCallback fieldCallback =
                new SQLInjectFieldCallback(bean);
        ReflectionUtils.doWithFields(managedBeanClass, fieldCallback);
    }
}
