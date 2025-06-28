package com.iny.side.submission.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    @Column(name = "drug_name", nullable = false)
    private String drugName;

    @Column(name = "dosage", nullable = false)
    private String dosage;

    @Column(name = "frequency", nullable = false)
    private String frequency;

    @Column(name = "duration", nullable = false)
    private String duration;

    @Builder
    public Prescription(Long id, Submission submission, String drugName,
                       String dosage, String frequency, String duration) {
        this.id = id;
        this.submission = submission;
        this.drugName = drugName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
    }
}