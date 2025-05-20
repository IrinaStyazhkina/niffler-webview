package ru.niffer_android.adapter.people

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.niffer_android.databinding.PeopleItemBinding
import ru.niffer_android.model.FriendshipStatus
import ru.niffer_android.model.User
import ru.niffer_android.utils.loadBase64Image

class PeopleViewHolder(
    private val binding: PeopleItemBinding,
    private val onInteractionListener: OnInteractionListener,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.personUsername.text = user.username
        binding.personName.text = user.fullname
        if (user.photoSmall != null) {
            binding.ivProfileImage.loadBase64Image(
                user.photoSmall
            )
        }

        setButtonsState(user.friendshipStatus)
        addButtonsEventListeners(user.username)
    }

    private fun setButtonsState(friendshipStatus: FriendshipStatus?) {
        when(friendshipStatus) {
            FriendshipStatus.INVITE_SENT -> {
                binding.acceptFriendButton.isVisible = false
                binding.declineFriendButton.isVisible = false
                binding.inviteFriendButton.isVisible = false
                binding.unfriendButton.isVisible = false
                binding.waitChip.isVisible = true
            }
            FriendshipStatus.FRIEND -> {
                binding.acceptFriendButton.isVisible = false
                binding.declineFriendButton.isVisible = false
                binding.inviteFriendButton.isVisible = false
                binding.unfriendButton.isVisible = true
                binding.waitChip.isVisible = false
            }
            FriendshipStatus.INVITE_RECEIVED -> {
                binding.acceptFriendButton.isVisible = true
                binding.declineFriendButton.isVisible = true
                binding.inviteFriendButton.isVisible = false
                binding.unfriendButton.isVisible = false
                binding.waitChip.isVisible = false
            }
            else -> {
                binding.acceptFriendButton.isVisible = false
                binding.declineFriendButton.isVisible = false
                binding.inviteFriendButton.isVisible = true
                binding.unfriendButton.isVisible = false
                binding.waitChip.isVisible = false
            }
        }
    }

    private fun addButtonsEventListeners(username: String) {
        binding.inviteFriendButton.setOnClickListener {
            onInteractionListener.onInviteSendButtonClick(username)
        }
        binding.acceptFriendButton.setOnClickListener {
            onInteractionListener.onAcceptFriendshipButtonClick(username)
        }
        binding.declineFriendButton.setOnClickListener {
            onInteractionListener.onDeclineFriendshipButtonClick(username)
        }
        binding.unfriendButton.setOnClickListener {
            onInteractionListener.onUnfriendButtonClick(username)
        }
    }
}