package com.booking.room.model;

import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

//Instead of using javax we need to use jakarta
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Component
@Table(name="rooms")
public class Room {
    @Id
    @Min(value= 1, message="Id should not be less that one")
    @Max(value= 10000, message="Id should not be greater than 10000")
    private Integer id;
    @Size(min = 5, max = 200, message
            = "About name of room must be between 5 and 200 characters")
    private String name;
    private String description;
    @Email(message = "Email should be valid")
    private String email;
    @NotNull
    private Boolean reservation;
}
