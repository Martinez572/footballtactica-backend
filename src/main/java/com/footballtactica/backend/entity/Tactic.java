package com.footballtactica.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tacticas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tactic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id")
    private UUID userId;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "descripcion")
    private String description;

    @Column(name = "formacion")
    private String formation;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TacticState state;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "datos", columnDefinition = "jsonb")
    private String data;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdAt;

    @Column(name = "fecha_modificacion")
    private LocalDateTime updatedAt;
}