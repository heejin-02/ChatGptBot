package com.donatienthorez.beenwhile.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.donatienthorez.beenwhile.chat.data.Conversation
import com.donatienthorez.beenwhile.chat.data.Message
import com.donatienthorez.beenwhile.chat.data.MessageStatus
import com.donatienthorez.beenwhile.chat.domain.usecase.ObserveMessagesUseCase
import com.donatienthorez.beenwhile.chat.domain.usecase.ResendMessageUseCase
import com.donatienthorez.beenwhile.chat.domain.usecase.SendChatRequestUseCase
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendChatRequestUseCase: SendChatRequestUseCase,
    private val resendChatRequestUseCase: ResendMessageUseCase,
    private val observeMessagesUseCase: ObserveMessagesUseCase,
) : ViewModel() {

    private val _conversation = MutableLiveData<Conversation>()
    val conversation: LiveData<Conversation> = _conversation

    private val _isSendingMessage = MutableLiveData<Boolean>()
    val isSendingMessage: LiveData<Boolean> = _isSendingMessage

    init {
        observeMessageList()
    }

    private fun observeMessageList() {
        viewModelScope.launch {
            observeMessagesUseCase.invoke().collect { conversation ->
                _conversation.postValue(conversation)

                _isSendingMessage.postValue(
                    conversation.list.any { it.messageStatus == MessageStatus.Sending }
                )
            }
        }
    }

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            sendChatRequestUseCase(
                prompt
            )
        }
    }

    fun resendMessage(message: Message) {
        viewModelScope.launch {
            resendChatRequestUseCase(
                message
            )
        }
    }
}