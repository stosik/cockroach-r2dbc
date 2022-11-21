package com.example.cockroach.demo.payment.infrastracture.persistence

import com.example.cockroach.demo.configuration.JooqEngine
import com.example.cockroach.demo.payment.domain.Payment
import database.schema.tables.Data
import database.schema.tables.records.DataRecord
import kotlinx.coroutines.reactive.awaitLast

internal class JooqDataRepository(private val jooqEngine: JooqEngine) : DataRepository {

    override suspend fun insert(data: Payment.Data): Int = jooqEngine.query {
        insertInto(Data.DATA)
            .set(data.toRecord())
            .awaitLast()
    }
}

private fun Payment.Data.toRecord(): DataRecord {
    return DataRecord(
        this.id,
        this.information
    )
}