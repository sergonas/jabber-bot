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
    private boolean quitTrigger;

    public MUCMessageListener(MultiUserChat multiUserChat, MessageHandler handler) {
        this.MUC = multiUserChat;
        this.handler = handler;
        this.quitTrigger = false;
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet instanceof Message) {
            logMessage((Message) packet);
            String messageBody = ((Message) packet).getBody();
            if(!messageBody.matches("\\w+\\s+quit\\s+.*")) {
                String response = handler.handleMessage((Message) packet);
                if(response!= null && !"".equals(response) && !" ".equals(response)) {
                    try {
                        MUC.sendMessage(response);
                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    MUC.sendMessage("Good bye.");
                    quitChat();
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void logMessage(Message msg) {
        //TODO create logger
    }

    private void quitChat() {
        synchronized (MUC) {
            quitTrigger = true;
            MUC.leave();
            MUC.notifyAll();
        }
    }

    public boolean isQuit() {
        return quitTrigger;
    }


}
