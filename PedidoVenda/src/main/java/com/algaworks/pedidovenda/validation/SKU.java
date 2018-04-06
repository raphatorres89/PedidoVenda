package com.algaworks.pedidovenda.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "([A-Za-z]{2}\\d{4,18})?")
public @interface SKU {

	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "tem formato inválido (AA####)";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
