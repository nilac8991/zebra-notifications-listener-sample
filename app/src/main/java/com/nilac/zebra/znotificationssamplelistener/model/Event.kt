package com.nilac.zebra.znotificationssamplelistener.model

class Event<T>(content: T?) {

    private val mContent: T? = content
    private var hasBeenHandled = false

    val contentIfNotHandled: T?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            mContent
        }

    fun hasBeenHandled(): Boolean {
        return hasBeenHandled
    }
}