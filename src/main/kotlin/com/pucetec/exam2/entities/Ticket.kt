package com.pucetec.exam2.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tickets")
class Ticket(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val licensePlate: String,

    @Column(nullable = false)
    val entryTime: LocalDateTime = LocalDateTime.now(),

    @Column
    var exitTime: LocalDateTime? = null, // Nulo mientras el auto siga adentro

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_spot_id", nullable = false)
    val parkingSpot: ParkingSpot
)