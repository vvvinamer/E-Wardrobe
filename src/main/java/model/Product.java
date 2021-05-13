package model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class Product extends FirestoreDocument {
    String brandName;

    @DateTimeFormat
    String buyDate;

    String color;

    @NonNull
    String category;

    String source;

    String season;

    Long ratings;

    String occasion;

    @NonNull
    String ownerId;

    String description;

    @NonNull
    Long price;

    public Product() {
        super();
    }

}
