package ru.sergonas.jabberbot;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.sergonas.jabberbot.plugins.BadHandlerException;
import ru.sergonas.jabberbot.plugins.CommandHandler;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Simple control thread for simple XMPP-bot.
 * User: serega
 * Date: 09.07.13
 * Time: 14:46
 */
public class Launcher {
    private static boolean DEBUG = false;
    public static final String BOT_NAME = "CMYK";
    public static void main(String[] args) throws XMPPException {
        ConfigManager.loadConfig(new File("config.xml"));
        System.out.println();

        Connection.DEBUG_ENABLED = DEBUG;
        Connection connection = new XMPPConnection(ConfigManager.getParam("server"));
        connection.connect();
        connection.login(ConfigManager.getParam("account"), ConfigManager.getParam("password"));
        Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);
        MultiUserChat muc = new MultiUserChat(connection, ConfigManager.getParam("room") + "@conference." + ConfigManager.getParam("server"));
        DiscussionHistory dh = new DiscussionHistory();
        dh.setMaxChars(0);
        muc.join(BOT_NAME, "", dh, 1000);
        MessageHandler messageHandler = new MessageHandler(BOT_NAME);

        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(true), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("ru.sergonas.jabberbot.plugins"))));

        Set<Class<? extends CommandHandler>> allClasses =
                reflections.getSubTypesOf(CommandHandler.class);

        for(Class<? extends CommandHandler> handl : allClasses) {
            try {
                CommandHandler inst = handl.newInstance();
                messageHandler.addCommandHandler(inst);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (BadHandlerException e) {
                e.printStackTrace();
            }
        }

        MUCMessageListener mucListener = new MUCMessageListener(muc, messageHandler);
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