package com.footballtactica.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "jugadas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Play {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tactica_id")
    private UUID tacticId;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "categoria")
    private String category;

    @Column(name = "orden")
    private Integer order;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(name = "datos", columnDefinition = "jsonb")
    private String data;
}