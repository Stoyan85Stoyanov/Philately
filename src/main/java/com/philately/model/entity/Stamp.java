package com.philately.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stamps")
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @Length(min = 5, max = 20)
    private String name;

    @Column(nullable = false)
    @Length(min = 3, max = 25)
    private String description;

    @Positive
    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    @Length(max = 150)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stamp stamp = (Stamp) o;
        return Objects.equals(id, stamp.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
