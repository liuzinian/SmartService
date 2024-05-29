package com.liu.forever.pojo.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述
 *
 * @author 刘子年
 * @date 2023/07/21
 */
@Target({ElementType.FIELD}) // 注解应用的地方（例如：类、方法、字段等）
@Retention(RetentionPolicy.RUNTIME) // 注解的生命周期（例如：源代码、类文件、运行时）
public @interface Describe {
    String value() default "";
}