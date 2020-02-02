package social.api.mock.message

import social.api.message.model.Message
import social.api.message.model.Messages
import social.api.message.server.MessageApiService
import social.api.mock.ResettableMock
import social.api.mock.SecurityContextHolder
import java.util.*
import javax.ws.rs.NotFoundException

class MessageApiServiceMock: MessageApiService, ResettableMock {
    private val messages = mutableMapOf<String, Message>()

    override fun getMessage(messageId: String): Message =
            messages[messageId] ?: throw NotFoundException()

    override fun getOutgoingMessages(): Messages = Messages().messages(
            filterMessagesByUser {userName, message->
                message.sender == userName
            }
    )

    override fun getIncomingMessages(): Messages = Messages().messages(
            filterMessagesByUser {userName, message->
                message.recipient == userName
            }
    )

    override fun createMessage(message: Message): Message {
        val messageId = UUID.randomUUID().toString()
        messages[messageId] = message.apply { id = messageId }
        return message
    }

    override fun reset() {
        messages.clear()
    }

    private fun filterMessagesByUser(filter:(userName:String, message:Message)->Boolean) =
            SecurityContextHolder.get()
                    ?.let {sc->
                        messages.values.toList().filter {
                            filter(sc.userPrincipal.name, it)}
                    }
                    ?: emptyList()
}
