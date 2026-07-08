package acalidonio.bodegamovilbackend.common.validations.annotations;

import acalidonio.bodegamovilbackend.common.validations.validators.UniqueProductSkuValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueProductSkuValidator.class)
@Documented
public @interface UniqueProductSku {
    String message() default "El SKU del producto debe ser único.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
