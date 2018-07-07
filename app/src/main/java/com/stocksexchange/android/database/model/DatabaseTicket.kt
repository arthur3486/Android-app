package com.stocksexchange.android.database.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.api.model.TicketMessage
import com.stocksexchange.android.database.model.DatabaseTicket.Companion.TABLE_NAME

/**
 * A Room database model class of [Ticket] class.
 */
@Entity(tableName = TABLE_NAME)
data class DatabaseTicket(
    @PrimaryKey @ColumnInfo(name = ID) var id: Long,
    @ColumnInfo(name = STATUS_ID) var statusId: Int,
    @ColumnInfo(name = CATEGORY_ID) var categoryId: Int,
    @ColumnInfo(name = SUBJECT) var subject: String,
    @ColumnInfo(name = MESSAGES) var messages: Map<Long, TicketMessage>,
    @ColumnInfo(name = UPDATED_AT) var updatedAt: Long,
    @ColumnInfo(name = CREATED_AT) var createdAt: Long
) {

    companion object {

        const val TABLE_NAME = "tickets"

        const val ID = "id"
        const val STATUS_ID = "status_id"
        const val CATEGORY_ID = "category_id"
        const val SUBJECT = "subject"
        const val MESSAGES = "messages"
        const val UPDATED_AT = "updated_at"
        const val CREATED_AT = "created_at"

    }


    constructor(): this(-1L, -1, -1, "", mapOf(), -1L, -1L)

}