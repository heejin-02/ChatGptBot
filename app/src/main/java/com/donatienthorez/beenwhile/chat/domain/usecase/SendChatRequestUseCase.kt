package com.donatienthorez.beenwhile.chat.domain.usecase

import com.donatienthorez.beenwhile.chat.data.ConversationRepository
import com.donatienthorez.beenwhile.chat.data.Message
import com.donatienthorez.beenwhile.chat.data.MessageStatus
import com.donatienthorez.beenwhile.chat.data.api.OpenAIRepository

class SendChatRequestUseCase(
    private val openAIRepository: OpenAIRepository,
    private val conversationRepository: ConversationRepository
) {

    suspend operator fun invoke(
        prompt: String
    ) {
        val message = Message(
            text = prompt,
            isFromUser = true,
            messageStatus = MessageStatus.Sending
        )
        val conversation = conversationRepository.addMessage(message)

        try {
            val reply = openAIRepository.sendChatRequest(conversation)
            conversationRepository.setMessageStatusToSent(message.id)
            conversationRepository.addMessage(reply)
        } catch (exception: Exception) {
            conversationRepository.setMessageStatusToError(message.id)
        }
    }
}