package com.asusoftware.Clinic_api.model;

@Entity
@Table(name = "clinic_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicHistory {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cabinet_id", nullable = false)
    private Cabinet cabinet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String action;
    private String details;
    private LocalDateTime createdAt;
}
