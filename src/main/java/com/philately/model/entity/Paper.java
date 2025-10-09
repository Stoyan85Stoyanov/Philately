package com.philately.model.entity;

import com.philately.model.entity.enums.PaperName;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "papers")
public class Paper  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PaperName paperName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "paper")
    private Set<Stamp> stamps;

    public Paper(PaperName paperName, String description) {
        this.paperName = paperName;
        this.description = description;
    }
}
