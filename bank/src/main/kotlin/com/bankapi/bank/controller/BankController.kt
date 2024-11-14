package com.bankapi.bank.controller

import com.bankapi.bank.entity.Bank
import com.bankapi.bank.service.BankService
import com.bankapi.bank.service.KafkaProducerService
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BankController(
        private val bankService: BankService,
        private val kafkaProducerService: KafkaProducerService
) {
    @GetMapping("/list")
    fun getBanks(): List<Bank> {
        return bankService.getAllBanks()
    }

    @GetMapping("/find.name")
    fun getBankName(@RequestParam name: String): ResponseEntity<String?> {
        val bankFound = bankService.getBankByName(name)
        if (bankFound != null) {
            kafkaProducerService.sendMessage(
                    "api_topic",
                    "[BANK API] Succesfully called /find.name, bank found"
            )
            return ResponseEntity.status(200)
                    .header("Message", "Bank found")
                    .body(bankFound.id?.toString())
        } else {
            kafkaProducerService.sendMessage(
                    "api-topic",
                    "[BANK API] Successfully called but bank in /find.name could not be found."
            )
            return ResponseEntity.status(200)
                    .header("Message", "No bank found")
                    .body(null.toString())
        }
    }
    @GetMapping("/find.key")
    fun getBankId(@RequestParam key: Long): ResponseEntity<String?> {
        val bank = bankService.getBankByID(key)
        if (bank != null) {
            kafkaProducerService.sendMessage("api_topic", "[BANK API] Succesfully called /find.key, bank found")
            return ResponseEntity.ok("Bank found")
        } else {
            kafkaProducerService.sendMessage("api_topic", "[BANK API] Succesfully called /find.key, bank not found")
            return ResponseEntity.status(200)
                .header("Message", "No bank found")
                .body(null.toString())
        }
    }
}
