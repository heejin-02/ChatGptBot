package com.donatienthorez.beenwhile.chat.domain.usecase

import com.donatienthorez.beenwhile.chat.data.ConversationRepository
import com.donatienthorez.beenwhile.chat.data.Message
import com.donatienthorez.beenwhile.chat.data.api.OpenAIRepository

class ResendMessageUseCase(
    private val openAIRepository: OpenAIRepository,
    private val conversationRepository: ConversationRepository
) {

    suspend operator fun invoke(
        message: Message
    ) {
        val conversation = conversationRepository.resendMessage(message)

        try {
            val reply = openAIRepository.sendChatRequest(conversation)
            conversationRepository.setMessageStatusToSent(message.id)
            conversationRepository.addMessage(reply)
        } catch (exception: Exception) {
            conversationRepository.setMessageStatusToError(message.id)
        }
    }
}