package com.librarymanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Schema(description = "Model of Book")
public class Book {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "The unique id of the Book", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Author cannot be null")
    @Size(max = 30, message = "Author length must not exceed 30 characters")
    @Schema(description = "Author of the Book", example = "Author Name")
    private String author;

    @NotNull(message = "Title cannot be null")
    @Size(max = 50, message = "Title length must not exceed 50 characters")
    @Schema(description = "Title of the Book", example = "Book Title")
    private String title;

    @NotNull(message = "Price cannot be null")
    @Schema(description = "Price of the Book", example = "19.99")
    private Double price;

    @Schema(description = "Description of the Book", example = "Description for Book")
    private String description;
}
