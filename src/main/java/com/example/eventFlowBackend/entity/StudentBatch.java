package com.example.eventFlowBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "student_batches")
public class StudentBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uID", referencedColumnName = "uID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "bID", referencedColumnName = "bID", nullable = false)
    private Batch batch;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();


}

