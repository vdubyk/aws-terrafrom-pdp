package org.avenga.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

public record NewProductRecord(
        @JsonProperty(value = "name") String name,
        @Min(value = 0, message = "Price must not be negative")
        @JsonProperty(value = "price") Double price,

        @Min(value = 0, message = "Quantity must not be negative")
        @JsonProperty(value = "quantity") Integer quantity
) {
}
