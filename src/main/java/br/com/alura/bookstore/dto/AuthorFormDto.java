package br.com.alura.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class AuthorFormDto {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @PastOrPresent
    private LocalDate birthdate;

    @NotBlank
    @Size(max = 240)
    private String miniResume;
}
