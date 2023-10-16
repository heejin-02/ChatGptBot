package com.donatienthorez.beenwhile.chat.domain.usecase

import com.donatienthorez.beenwhile.chat.data.ConversationRepository

class ObserveMessagesUseCase(
    private val conversationRepository: ConversationRepository
) {

    operator fun invoke() = conversationRepository.conversationFlow

}