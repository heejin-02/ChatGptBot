package com.donatienthorez.beenwhile.di

import com.donatienthorez.beenwhile.chat.data.ConversationRepository
import com.donatienthorez.beenwhile.chat.data.api.OpenAIRepository
import com.donatienthorez.beenwhile.chat.domain.usecase.ObserveMessagesUseCase
import com.donatienthorez.beenwhile.chat.domain.usecase.ResendMessageUseCase
import com.donatienthorez.beenwhile.chat.domain.usecase.SendChatRequestUseCase
import com.donatienthorez.beenwhile.chat.ui.ChatViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val chatModule = module {
    viewModel {
        ChatViewModel(get(), get(), get())
    }
    single { OpenAIRepository(openAI = get()) }
    single { ConversationRepository() }

    single { SendChatRequestUseCase(openAIRepository = get(), conversationRepository = get()) }
    single { ResendMessageUseCase(openAIRepository = get(), conversationRepository = get()) }
    single { ObserveMessagesUseCase(conversationRepository = get()) }
}