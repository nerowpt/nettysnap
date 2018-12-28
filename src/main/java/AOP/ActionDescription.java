package AOP;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionDescription {
    boolean validate() default true;
    /**接口的操作名（唯一不重复）*/
    String actionName();
    /**操作描述*/
    String description();
}
