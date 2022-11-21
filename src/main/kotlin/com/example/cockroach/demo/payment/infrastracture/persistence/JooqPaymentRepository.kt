package com.example.cockroach.demo.payment.infrastracture.persistence

import com.example.cockroach.demo.configuration.JooqEngine
import com.example.cockroach.demo.payment.domain.Payment
import database.schema.Keys
import database.schema.Tables
import database.schema.Tables.DATA
import database.schema.tables.Payment.PAYMENT
import database.schema.tables.records.PaymentRecord
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jooq.Record
import java.util.*

internal class JooqPaymentRepository(
    private val dataRepository: DataRepository,
    private val jooqEngine: JooqEngine
) : PaymentRepository {

    override suspend fun insertPayment(payment: Payment): UUID {
        val data = payment.data
        jooqEngine.transaction {
            dataRepository.insert(data)
            insert(payment)
        }
        return payment.id
    }

    override suspend fun findById(uuid: UUID): Payment? = jooqEngine.query {
        select()
            .from(PAYMENT)
            .join(DATA).onKey(Keys.FK_DATA)
            .where(Tables.PAYMENT.ID.eq(uuid))
            .awaitFirstOrNull()
            ?.toDomain()
    }

    override suspend fun findAll(limit: Int?, offset: Int?): List<Payment> = jooqEngine.query {
        select()
            .from(PAYMENT)
            .join(DATA).onKey(Keys.FK_DATA)
            .apply { limit?.let { limit(it) } }
            .apply { offset?.let { offset(it) } }
            .asFlow()
            .map { it.toDomain() }
            .toList()
    }

    private suspend fun insert(payment: Payment) = jooqEngine.query {
        insertInto(PAYMENT)
            .set(payment.toRecord())
            .awaitLast()
    }
}

private fun Record.toDomain() = Payment(
    id = this[PAYMENT.ID],
    amount = this[PAYMENT.AMOUNT].toLong(),
    data = Payment.Data(
        id = this[DATA.ID],
        information = this[DATA.INFORMATION]
    )
)

private fun Payment.toRecord(): PaymentRecord {
    return PaymentRecord(
        this.id,
        this.amount.toString(),
        this.data.id
    )
}