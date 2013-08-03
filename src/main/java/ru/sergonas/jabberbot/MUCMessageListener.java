package ru.sergonas.jabberbot;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smackx.muc.MultiUserChat;

/**
 * No JavaDoc
 * User: serega
 * Date: 03.08.13
 * Time: 17:12
 */
public class MUCMessageListener implements PacketListener {
    private final MultiUserChat MUC;
    private final MessageHandler handler;


    public MUCMessageListener(MultiUserChat multiUserChat, MessageHandler handler) {
        this.MUC = multiUserChat;
        this.handler = handler;
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet instanceof Message) {
            logMessage((Message) packet);
            String response = handler.handleMessage((Message) packet);
            if(response!= null && !"".equals(response) && !" ".equals(response)) {
                try {
                    MUC.sendMessage(response);
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
            if(handler.isQuit()) quitChat();
        }
    }

    private void logMessage(Message msg) {
        //TODO create logger
    }

    private void quitChat() {
        synchronized (MUC) {
            MUC.leave();
            MUC.notifyAll();
        }
    }

    public boolean isQuit() {
        return handler.isQuit();
    }


}
