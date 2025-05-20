package ru.niffer_android.adapter.people


interface OnInteractionListener {
    fun onInviteSendButtonClick(username: String)

    fun onAcceptFriendshipButtonClick(username: String)

    fun onDeclineFriendshipButtonClick(username: String)

    fun onUnfriendButtonClick(username: String)
}