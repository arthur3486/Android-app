package com.stocksexchange.android.database.mappings

import com.stocksexchange.android.api.model.Ticket
import com.stocksexchange.android.database.model.DatabaseTicket

fun Ticket.mapToDatabaseTicket(): DatabaseTicket {
    return DatabaseTicket(
        id = id,
        statusId = statusId,
        categoryId = categoryId,
        subject = subject,
        messages = messages,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}


fun List<Ticket>.mapToDatabaseTicketList(): List<DatabaseTicket> {
    return map { it.mapToDatabaseTicket() }
}


fun DatabaseTicket.mapToTicket(): Ticket {
    return Ticket(
        id = id,
        statusId = statusId,
        categoryId = categoryId,
        subject = subject,
        messages = messages,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}


fun List<DatabaseTicket>.mapToTicketList(): List<Ticket> {
    return map { it.mapToTicket() }
}


fun Map<Long, Ticket>.mapToTicketList(): List<Ticket> {
    val list: MutableList<Ticket> = mutableListOf()

    for(item in this) {
        list.add(item.value)
    }

    return list
}