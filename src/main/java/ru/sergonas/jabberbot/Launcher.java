package ru.sergonas.jabberbot;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * Simple control thread for simple XMPP-bot.
 * User: Сергей
 * Date: 09.07.13
 * Time: 14:46
 */
public class Launcher {
    private static boolean DEBUG = false;
    public static final String BOT_NAME = "CMYK";
    public static void main(String[] args) throws XMPPException {
        Connection.DEBUG_ENABLED = DEBUG;
        Connection connection = new XMPPConnection("jabber.ru");

        connection.connect();
        connection.login("", "");
        Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);
        MultiUserChat muc = new MultiUserChat(connection, "@conference.jabber.ru");
        DiscussionHistory dh = new DiscussionHistory();
        dh.setMaxChars(0);
        muc.join(BOT_NAME, "", dh, 1000);
        MUCMessageListener mucListener = new MUCMessageListener(BOT_NAME, muc);
        muc.addMessageListener(mucListener);
        if(!muc.isJoined()) System.out.println("Too bad");
        try {
            synchronized (muc) {
                while (!mucListener.isQuit()) {
                    muc.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}