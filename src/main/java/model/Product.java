package model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.google.firebase.database.annotations.NotNull;
import dao.FirebaseService;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Product {
    String brandName;

    String buyDate;

    String color;
    
    String category;

    String source;

    String season;

    Long ratings;

    String occasion;

    String ownerId;

    String description;

    Long price;

    public Product() {
        super();
    }

}
