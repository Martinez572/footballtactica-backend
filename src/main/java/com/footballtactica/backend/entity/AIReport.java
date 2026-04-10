package com.footballtactica.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reportes_ia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIReport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id")
    private UUID userId;

    @Column(name = "jugador_id")
    private UUID playerId;

    @Column(name = "tipo_analisis")
    private String analysisType;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String content;

    @Column(name = "resumen")
    private String summary;

    @Column(name = "fecha")
    private LocalDateTime createdAt;
}