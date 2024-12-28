package ru.otus.pro.hw.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    // @CollectionTable(
    //         name = "student_products",
    //         joinColumns =@JoinColumn(name="product_id")
    // )
    @OneToMany(mappedBy = "product")
    private List<StudentProdact> students;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
