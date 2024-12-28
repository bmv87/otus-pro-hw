package ru.otus.pro.hw.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "student_products")
public class StudentProdact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "StudentProdact{" +
                "id=" + id +
                ", student=" + student.getName() +
                ", product=" + product.getTitle() +
                ", createdAt=" + createdAt +
                '}';
    }
}
