package br.com.alura.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookFormDto {

    @NotBlank
    @Size(min = 10, max = 100)
    private String title;

    @NotNull
    @PastOrPresent
    private LocalDate publicationDate;

    @Min(100)
    private Integer pages;

    @Min(1)
    private Long authorId;
}
