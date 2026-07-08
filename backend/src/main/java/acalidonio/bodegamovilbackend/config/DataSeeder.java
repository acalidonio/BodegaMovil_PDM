package acalidonio.bodegamovilbackend.config;

import acalidonio.bodegamovilbackend.domain.entities.*;
import acalidonio.bodegamovilbackend.repository.ShiftTemplateRepository;
import acalidonio.bodegamovilbackend.repository.UserRepository;
import acalidonio.bodegamovilbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShiftTemplateRepository shiftTemplateRepository;

    @Override
    public void run(String @NonNull ... args) {
        if (userRepository.count() > 0) {
            return;
        }

        ShiftTemplate mondayShift = ShiftTemplate.builder()
                        .name("Lunes - Día Completo")
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build();
        ShiftTemplate tuesdayShift = ShiftTemplate.builder()
                        .name("Martes - Día Completo")
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build();
        ShiftTemplate wednesdayShift = ShiftTemplate.builder()
                        .name("Miércoles - Día Completo")
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build();
        ShiftTemplate thursdayShift = ShiftTemplate.builder()
                        .name("Jueves - Día Completo")
                        .dayOfWeek(DayOfWeek.THURSDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build();
        ShiftTemplate fridayShift = ShiftTemplate.builder()
                        .name("Viernes - Día Completo")
                        .dayOfWeek(DayOfWeek.FRIDAY)
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(16, 0))
                        .build();

        ShiftTemplate mondayHalfShift = ShiftTemplate.builder()
                        .name("Lunes - Medio Tiempo")
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .startTime(LocalTime.of(8, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();
        ShiftTemplate tuesdayHalfShift = ShiftTemplate.builder()
                        .name("Martes - Medio Tiempo")
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .startTime(LocalTime.of(8, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();
        ShiftTemplate wednesdayHalfShift = ShiftTemplate.builder()
                        .name("Miércoles - Medio Tiempo")
                        .dayOfWeek(DayOfWeek.WEDNESDAY)
                        .startTime(LocalTime.of(8, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();
        ShiftTemplate thursdayHalfShift = ShiftTemplate.builder()
                        .name("Jueves - Medio Tiempo")
                        .dayOfWeek(DayOfWeek.THURSDAY)
                        .startTime(LocalTime.of(8, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();
        ShiftTemplate fridayHalfShift = ShiftTemplate.builder()
                        .name("Viernes - Medio Tiempo")
                        .dayOfWeek(DayOfWeek.FRIDAY)
                        .startTime(LocalTime.of(8, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();
        ShiftTemplate sundayHalfShift = ShiftTemplate.builder()
                        .name("Domingo - Medio Tiempo")
                        .dayOfWeek(DayOfWeek.SUNDAY)
                        .startTime(LocalTime.of(8, 30))
                        .endTime(LocalTime.of(12, 30))
                        .build();

        shiftTemplateRepository.saveAll(List.of(mondayShift, tuesdayShift, wednesdayShift, thursdayShift, fridayShift, mondayHalfShift, tuesdayHalfShift, wednesdayHalfShift,  thursdayHalfShift, fridayHalfShift, sundayHalfShift));

        String hash = BCrypt.hashpw("password123", BCrypt.gensalt());

        User admin = new User("EMP001", "André C", "AC", Role.ADMIN, hash, new HashSet<>(), "https://i.pravatar.cc/150?u=TESTEMP001");
        User employee = new User("EMP002", "Eduardo C", "EC", Role.EMPLOYEE, hash, new HashSet<>(), null);

        admin.setShiftTemplates(Set.of(mondayShift, tuesdayShift, wednesdayShift, thursdayShift, fridayShift, sundayHalfShift));
        employee.setShiftTemplates(Set.of(mondayHalfShift, tuesdayHalfShift, wednesdayHalfShift,  thursdayHalfShift, fridayHalfShift));

        userRepository.saveAll(List.of(admin, employee));

        List<Product> initialProducts = new ArrayList<>(List.of(
                    new Product("BRG-6204",
                            "Rodamiento Bolas 6204",
                            "Rodamiento de acero inoxidable para motores eléctricos",
                            ProductCategory.RODAMIENTOS,
                            StockStatus.AVAILABLE,
                            "Estante A - Nivel 2",
                            150,
                            Date.valueOf("2026-06-20"),
                            "20mm",
                            "47mm",
                            "14mm",
                            "105g",
                            "Acero Inoxidable",
                            true,
                            null
                    ),
                    new Product("BRG-6205",
                            "Rodamiento Bolas 6205",
                            "Rodamiento de alta velocidad",
                            ProductCategory.RODAMIENTOS,
                            StockStatus.LOW_STOCK,
                            "Estante A - Nivel 3",
                            4,
                            Date.valueOf("2026-06-15"),
                            "25mm",
                            "52mm",
                            "15mm",
                            "130g",
                            "Acero al Carbono",
                            true,
                            "https://cdn.pixabay.com/photo/2016/03/04/15/27/ball-bearings-1236203_1280.png"
                    ),
                    new Product("BLT-M10X50",
                            "Perno Hexagonal M10x50",
                            "Perno de acero",
                            ProductCategory.PERNOS_TUERCAS,
                            StockStatus.OUT_OF_STOCK,
                            "Pasillo 3 - Caja 12",
                            0,
                            Date.valueOf("2026-06-25"),
                            null,
                            null,
                            "10mm",
                            "40g",
                            "Acero Inoxidable",
                            true,
                            "https://images.unsplash.com/photo-1704732060817-b62414c6004c"
                    ),
                    new Product("FLG-2IN-150",
                            "Brida 2 pulgadas",
                            "Brida ciega clase 150",
                            ProductCategory.BRIDAS,
                            StockStatus.AVAILABLE,
                            "Patio Exterior - Lote B",
                            45,
                            Date.valueOf("2026-06-01"),
                            "50mm",
                            "150mm",
                            "20mm",
                            "2kg",
                            "Hierro Fundido",
                            true,
                            "https://images.pexels.com/photos/12951626/pexels-photo-12951626.jpeg"
                    )
            ));

        for (int i = 1; i <= 30; i++) {
            initialProducts.add(new Product(
                    "GEN-" + i + "-" + (int)(Math.random() * 10000),
                    "Producto Generado " + i,
                    null,
                    ProductCategory.values()[(int)(Math.random() * ProductCategory.values().length)],
                    StockStatus.AVAILABLE,
                    "Bodega Central",
                    (int)(Math.random() * 100),
                    new Date(System.currentTimeMillis()),
                    null,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null
            ));
        }

        productRepository.saveAll(initialProducts);
        System.out.println("Seeder corrió correctamente");
    }
}

