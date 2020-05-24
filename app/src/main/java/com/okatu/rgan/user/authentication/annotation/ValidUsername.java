package com.okatu.rgan.user.authentication.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@NotNull
@NotEmpty
@Size(min = 4, max = 24)
// trailing _ or - is acceptable, support chinese
@Pattern(regexp = "^[A-Za-z0-9\\u4e00-\\u9fff]+(?:[_-][A-Za-z0-9\\u4e00-\\u9fff]+)*[_-]?$")
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidUsername {
    String message() default "{com.okatu.rgan.user.authentication.annotation.ValidUsername.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
