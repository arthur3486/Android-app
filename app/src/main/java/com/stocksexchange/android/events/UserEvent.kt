package com.stocksexchange.android.events

import com.stocksexchange.android.api.model.User
import com.stocksexchange.android.utils.helpers.tag

/**
 * An event to send to notify subscribers about
 * [User] model class updates.
 */
class UserEvent private constructor(
    type: Int,
    sourceTag: String,
    val action: Actions
) : BaseEvent<Void?>(type, null, sourceTag) {


    companion object {


        fun signOut(source: Any): UserEvent {
            return signOut(tag(source))
        }


        fun signOut(sourceTag: String): UserEvent {
            return UserEvent(TYPE_INVALID, sourceTag, Actions.SIGNED_OUT)
        }


    }


    enum class Actions {

        SIGNED_OUT

    }


}