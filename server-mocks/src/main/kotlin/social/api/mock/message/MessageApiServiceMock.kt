package social.api.mock.message

import social.api.message.model.Message
import social.api.message.model.Messages
import social.api.message.server.MessageApiService
import social.api.mock.ResettableMock
import java.util.*
import javax.ws.rs.NotFoundException

class MessageApiServiceMock: MessageApiService, ResettableMock {
    val messages = mutableMapOf<String, Message>()

    override fun getMessage(messageId: String): Message = messages[messageId] ?: throw NotFoundException()

    override fun getMessages(): Messages = Messages().messages(messages.values.toList())

    override fun createMessage(message: Message): Message {
        val messageId = UUID.randomUUID().toString()
        messages[messageId] = message.apply { id = messageId }
        return message
    }

    override fun reset() {
        messages.clear()
    }
}
