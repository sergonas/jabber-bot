package ru.sergonas.jabberbot;

import org.jivesoftware.smack.*;
import java.io.File;

/**
 * Simple control thread for simple XMPP-bot.
 * User: serega
 * Date: 09.07.13
 * Time: 14:46
 */
public class Launcher {
    private static final String pathToConfigFile = "botconfig.xml";
    private static XMPPBot bot;
    public static void main(String[] args) throws XMPPException {
        ConfManager.loadConfig(new File(pathToConfigFile));
        bot = new XMPPBot(ConfManager.get("server"), ConfManager.get("botname"));

        //TODO load all rooms from config
        bot.addRoomToWorkWith(ConfManager.get("room") + "@conference." + ConfManager.get("server"));
        try {
            bot.connect(ConfManager.get("account"), ConfManager.get("password"));
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        //connection with server established now

        try {
            synchronized (bot) {
                while (!bot.isQuit()) {
                    bot.wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}