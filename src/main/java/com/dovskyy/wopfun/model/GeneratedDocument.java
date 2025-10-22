package com.dovskyy.wopfun.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "generated_document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(name = "doc_type", nullable = false)
    private String docType; // "WOPFU" or "IPET"

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @Column(name = "generated_by")
    private String generatedBy;

    @Column(name = "ai_strengths", columnDefinition = "TEXT")
    private String aiStrengths; // Mocne strony

    @Column(name = "ai_recommendations", columnDefinition = "TEXT")
    private String aiRecommendations; // Zalecenia (stored as JSON array text)

    @Column(name = "ai_goals", columnDefinition = "TEXT")
    private String aiGoals; // Cele szczegółowe (stored as JSON array text)

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "status", nullable = false)
    private String status = "DRAFT"; // DRAFT, APPROVED

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        if (status == null) {
            status = "DRAFT";
        }
    }
}
