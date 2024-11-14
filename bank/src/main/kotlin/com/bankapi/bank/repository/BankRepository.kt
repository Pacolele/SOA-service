package com.bankapi.bank.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.bankapi.bank.entity.Bank


@Repository
interface BankRepository : JpaRepository<Bank,Long> {
    fun findByName(name : String) : Bank?
}
