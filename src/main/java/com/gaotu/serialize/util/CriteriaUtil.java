package com.gaotu.serialize.util;

import com.gaotu.serialize.model.Country;
import com.gaotu.serialize.function.EntityFieldFuntion;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 构造Criteria工具类
 *
 * @param <S>
 */
public class CriteriaUtil<S> {

    private S entity;

    private List<Field> fields;

    public CriteriaUtil(S entity) {
        this.entity = entity;
        Class<?> clazz = entity.getClass();
        this.fields = getFieldList(clazz);
    }

    public static <S> CriteriaUtil<S> of(S entity) {
        return new CriteriaUtil<>(entity);
    }

    public  <T, R> CriteriaUtil<S> and(boolean flag, EntityFieldFuntion<T, R> entityFieldFuntion, Consumer<R> consumer){
        return and(val -> flag, getValue(entityFieldFuntion), consumer);
    }

    public <E> CriteriaUtil<S> and(Predicate<E> predicate, E value, Consumer<E> consumer) {
        if (predicate.test(value)) {
            consumer.accept(value);
        }
        return this;
    }

    public <T, R> CriteriaUtil<S> andNotNull(EntityFieldFuntion<T, R> entityFieldFuntion, Consumer<R> consumer) {
        String fieldName = EntityFieldFuntion.getFieldName(entityFieldFuntion);
        return and(Optional.ofNullable(fieldName).isPresent(), entityFieldFuntion, consumer);
    }

//    public CriteriaUtil<S> when(){
//
//    }

    public void test() {
        Country country = new Country();
        CriteriaUtil.of(new Country()).and(true, Country::getName, country::setName);
    }

    private final <T, R> R getValue(EntityFieldFuntion<T, R> fieldFunc) {
        R value = null;
        String fieldName = EntityFieldFuntion.getFieldName(fieldFunc);
        Field field = fields.stream()
                .filter(f -> fieldName.equals(f.getName()))
                .findFirst()
                .orElse(null);
        if (Optional.ofNullable(field).isPresent()) {
//            FieldAccessor fieldAccessor = reflectionFactory.newFieldAccessor(field, true);
//            value = (R1) fieldAccessor.get(source);
//            if (Optional.ofNullable(value).isPresent()) {
//                if (value instanceof Collection) {
//                    value = ((Collection) value).size() > 0 ? value : null;
//                }
//                if (value instanceof Map) {
//                    value = ((Map) value).size() > 0 ? value : null;
//                }
//            }
            field.setAccessible(true);
            try {
                value = (R)field.get(entity);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return value;
    }


    /**
     * 获取类属性及父类属性
     *
     * @param clazz
     * @return
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fieldAll = Arrays.stream(fields)
                .filter(CriteriaUtil::includeField).collect(Collectors.toList());
        getParentField(clazz, fieldAll);
        return fieldAll;
    }

    /**
     * 查询所有父类属性
     *
     * @param clazz
     * @param fieldAll
     */
    private static void getParentField(Class<?> clazz, List<Field> fieldAll) {
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && !superClazz.isAssignableFrom(Object.class)) {
            Field[] superFields = superClazz.getDeclaredFields();
            fieldAll.addAll(Arrays.stream(superFields)
                    .filter(CriteriaUtil::includeField)
                    .collect(Collectors.toList()));
            getParentField(superClazz, fieldAll);
        }
    }

    private static Boolean includeField(Field field) {
        // 排除 static 属性和 transient 属性
        return !Modifier.isStatic(field.getModifiers())
                && !Modifier.isTransient(field.getModifiers());
    }

}
