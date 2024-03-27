package com.jeeva.model;

import java.util.List;

import com.jeeva.model.roles.Admin;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cid;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor")
    private Admin instructor;

    @Column
    private String genre;

    @Column
    private String duration;

    @Column
    private String amount;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enroll> enrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Payment> payments;

}
