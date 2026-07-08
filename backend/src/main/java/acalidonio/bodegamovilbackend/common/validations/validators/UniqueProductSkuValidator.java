package acalidonio.bodegamovilbackend.common.validations.validators;

import acalidonio.bodegamovilbackend.common.validations.annotations.UniqueProductSku;
import acalidonio.bodegamovilbackend.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueProductSkuValidator implements ConstraintValidator<UniqueProductSku, String> {
    private final ProductRepository productRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty())
            return true;

        return !productRepository.existsBySkuIgnoreCase(value);
    }
}