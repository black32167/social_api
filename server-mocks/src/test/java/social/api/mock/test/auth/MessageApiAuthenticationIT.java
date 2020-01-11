package social.api.mock.test.auth;

import org.junit.Test;
import social.api.message.model.Message;
import social.api.mock.test.AbstractApiIntegrationTest;

import static org.junit.Assert.fail;

public class MessageApiAuthenticationIT extends AbstractApiIntegrationTest {
    private final static Message MESSAGE = new Message()
            .messageBody("Message")
            .sender(JOHN_NAME)
            .recipient(JANE_NAME);

    @Test
    public void testGetIncomingMessagesFailedWithoutAuthentication() throws Exception {
        try {
            messageApi.getIncomingMessages();
            fail("Should prevent getIncomingMessages for unauthenticated user");
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetOutgoingMessagesFailedWithoutAuthentication() throws Exception {
        try {
            messageApi.getOutgoingMessages();
            fail("Should prevent getOutgoingMessages for unauthenticated user");
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateMessageFailedWithoutAuthentication() throws Exception {
        try {
            messageApi.createMessage(MESSAGE);
            fail("Should prevent createMessage for unauthenticated user");
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetMessageFailedWithoutAuthentication() throws Exception {
        createAndAuthenticate(JOHN_NAME, JANE_PASSWORD);
        String postedMessageId = messageApi.createMessage(MESSAGE).getId();
        cleanAuthentication();
        
        try {
            messageApi.getMessage(postedMessageId);
            fail("Should prevent getMessage for unauthenticated user");
        } catch (Exception e) {
        }
    }
}
