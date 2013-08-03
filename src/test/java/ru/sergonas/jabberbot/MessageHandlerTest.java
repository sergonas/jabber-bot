package ru.sergonas.jabberbot;

import org.jivesoftware.smack.packet.Message;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 19:32
 */
public class MessageHandlerTest {
    private MessageHandler messageHandler;
    private final String BOT_NAME = "CMYK";
    @Before
    public void init() {
        messageHandler = new MessageHandler(BOT_NAME);
    }

    @Test
    public void quitTest() {
        Message msg = new Message();
        msg.setBody(BOT_NAME + " quit");
        String response = messageHandler.handleMessage(msg);
        assertTrue("Good bye.".equals(response));
        assertTrue(messageHandler.isQuit());
        messageHandler.reset();
    }

    @Test
    public void badCommandTest() {
        Message msg = new Message();
        msg.setBody(BOT_NAME + " asdqwe");
        String response = messageHandler.handleMessage(msg);
        assertTrue("No such command.".equals(response));
        assertFalse(messageHandler.isQuit());
    }

    @Test
    public void noCommandTest() {
        Message msg = new Message();
        msg.setBody(BOT_NAME + " ");
        String response = messageHandler.handleMessage(msg);
        assertTrue("No command.".equals(response));
        assertFalse(messageHandler.isQuit());
    }
}
