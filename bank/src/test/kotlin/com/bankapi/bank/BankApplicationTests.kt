package com.bankapi.bank

import com.bankapi.bank.entity.Bank
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class BankApplicationTests {
    @Autowired private lateinit var mockMvc: MockMvc
    companion object {
        private var allBanks: Array<String> =
                arrayOf<String>(
                        "SWEDBANK",
                        "IKANOBANKEN",
                        "JPMORGAN",
                        "NORDEA",
                        "CITIBANK",
                        "HANDELSBANKEN",
                        "SBAB",
                        "HSBC",
                        "NORDNET"
                )

        @JvmStatic fun bankNamesProvider(): Stream<String> = Stream.of(*allBanks)
        @JvmStatic
        fun bankIdsProvider(): LongStream = LongStream.range(1, allBanks.size.toLong() + 1)
    }

    @Test
    fun `Should return a list of all banks`() {
        val banks = allBanks.toList()
        val res: MvcResult =
                mockMvc.perform(get("/list"))
                        .andExpect(status().isOk)
                        .andExpect(content().contentType("application/json"))
                        .andExpect(jsonPath("$.length()").value(banks.size))
                        .andReturn()

        val mapper = jacksonObjectMapper()
        val json = res.response.contentAsString
        val returnedBanks: List<Bank> = mapper.readValue(json)
        val returnedBankNames = returnedBanks.map { it.name }
        assert(returnedBankNames.containsAll(banks) && banks.containsAll(returnedBankNames)) {
            "The returned banks doesn't match the expected list!"
        }
    }

    @ParameterizedTest
    @MethodSource("bankNamesProvider")
    fun `Should return bank found from name`(name: String) {
        val res =
                mockMvc.perform(get("/find.name?name={name}", name))
                        .andExpect(status().isOk)
                        .andReturn()

        assertEquals("Bank found", res.response.contentAsString)
    }

    @ParameterizedTest
    @MethodSource("bankIdsProvider")
    fun `Should return bank for given id`(key: Long) {

        val res =
                mockMvc.perform(get("/find.key?key={key}", key))
                        .andExpect(status().isOk)
                        .andReturn()

        // println(
        //         "\n\n\n\nRUN ID: " +
        //                 id +
        //                 " With name: " +
        //                 allBanks[id.toInt()] +
        //                 "\n" +
        //                 res.response.toString() +
        //                 "\n\n\n\n\n"
        // )

        assertEquals("Bank found", res.response.contentAsString)
    }
}
