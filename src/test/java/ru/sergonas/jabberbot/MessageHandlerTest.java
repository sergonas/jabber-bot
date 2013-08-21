package ru.sergonas.jabberbot;

import org.jivesoftware.smack.packet.Message;
import org.junit.Before;
import org.junit.Test;
import ru.sergonas.jabberbot.bot.MessageHandler;

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
        messageHandler = new MessageHandler();
    }

    @Test
    public void badCommandTest() {
        Message msg = new Message();
        msg.setBody(BOT_NAME + " asdqwe");
        String response = messageHandler.handleMessage(msg);
        assertTrue("No such command.".equals(response));
    }

    @Test
    public void noCommandTest() {
        Message msg = new Message();
        msg.setBody(BOT_NAME + " ");
        String response = messageHandler.handleMessage(msg);
        assertTrue("No command.".equals(response));
    }
}
