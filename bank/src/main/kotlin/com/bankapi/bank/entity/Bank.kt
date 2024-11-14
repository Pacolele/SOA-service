package com.bankapi.bank.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.Entity
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column


@Entity
data class Bank (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,
    @Column(unique = true)
    val name: String?
)
{
    // No-argument constructor is required for Hibernate
    constructor() : this(null, null)
}
