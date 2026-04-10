package com.footballtactica.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jugadores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "usuario_id")
    private UUID userId;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "numero")
    private Integer number;

    @Column(name = "posicion")
    private String position;

    @Column(name = "edad")
    private Integer age;

    @Column(name = "nacionalidad")
    private String nationality;

    @Column(name = "fecha_creacion")
    private LocalDateTime createdAt;
}