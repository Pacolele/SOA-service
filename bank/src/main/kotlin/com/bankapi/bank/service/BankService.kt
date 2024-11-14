package com.bankapi.bank.service

import com.bankapi.bank.entity.Bank
import com.bankapi.bank.repository.BankRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class BankService(private val bankRepository: BankRepository) {
    fun getBankByID(id: Long): Bank? {
        return bankRepository.findById(id).orElse(null)
    }
    fun getBankByName(name: String): Bank? {
        return bankRepository.findByName(name)
    }
    fun getAllBanks(): List<Bank> {
        return bankRepository.findAll()
    }
}

@Component
class DataLoader(val bankRepository: BankRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        bankRepository.saveAll(
                listOf(
                        Bank(name = "SWEDBANK"),
                        Bank(name = "IKANOBANKEN"),
                        Bank(name = "JPMORGAN"),
                        Bank(name = "NORDEA"),
                        Bank(name = "CITIBANK"),
                        Bank(name = "HANDELSBANKEN"),
                        Bank(name = "SBAB"),
                        Bank(name = "HSBC"),
                        Bank(name = "NORDNET")
                )
        )
    }
}
