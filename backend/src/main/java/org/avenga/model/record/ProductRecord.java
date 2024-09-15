package org.avenga.model.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductRecord(
        @JsonProperty(value = "id") Long id,
        @JsonProperty(value = "name") String name,

        @JsonProperty(value = "price") Double price,

         @JsonProperty(value = "quantity") Integer quantity,

         @JsonProperty(value = "isExist") boolean isExist
) {
}
