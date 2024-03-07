package com.librarymanagementsystem.business.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "book")
public class BookDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author", nullable = false, length = 30)
    private String author;
    @Column(name = "title", nullable = false, length = 50)
    private String title;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = "description")
    private String description;
}
