package ru.sergonas.jabberbot;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.sergonas.jabberbot.plugins.BadHandlerException;
import ru.sergonas.jabberbot.plugins.CommandHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 17:12
 */
public class MUCMessageListener implements PacketListener {
    private final MultiUserChat MUC;
    private final MessageHandler handler;

    public MUCMessageListener(MultiUserChat multiUserChat) {
        this.MUC = multiUserChat;
        this.handler = getHandler();


    }

    private MessageHandler getHandler() {
        MessageHandler handler = new MessageHandler();
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(true), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("ru.sergonas.jabberbot.plugins"))));

        Set<Class<? extends CommandHandler>> allPlugins =
                reflections.getSubTypesOf(CommandHandler.class);

        for(Class<? extends CommandHandler> plugin : allPlugins) {
            try {
                CommandHandler inst = plugin.newInstance();
                handler.addCommandHandler(inst);
            } catch (InstantiationException | IllegalAccessException | BadHandlerException e) {
                e.printStackTrace();
            }
        }

        return handler;
    }

    @Override
    public void processPacket(Packet packet) {
        Message currentMessage;
        if(packet instanceof Message) {
            currentMessage = (Message) packet;
        } else {
            return;
        }

        //TODO move this functionality
        //logMessage((Message) packet);

        String response = handler.handleMessage(currentMessage);
        if(isResponseValid(response)) {
            try {
                MUC.sendMessage(response);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isResponseValid(String responseText) {
        return responseText != null && !"".equals(responseText) && !" ".equals(responseText);
    }
}
