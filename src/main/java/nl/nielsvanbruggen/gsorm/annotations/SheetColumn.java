package nl.nielsvanbruggen.gsorm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SheetColumn {
    String value();
    boolean trim() default true;
    boolean caseInsensitive() default false;
}
