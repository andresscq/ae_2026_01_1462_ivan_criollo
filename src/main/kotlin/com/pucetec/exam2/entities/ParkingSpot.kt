package com.pucetec.exam2.entities

import jakarta.persistence.*

@Entity
@Table(name = "parking_spots")
class ParkingSpot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val code: String, // Ejemplo: "A-01"

    @Column(nullable = false)
    var isOccupied: Boolean = false
)