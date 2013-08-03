package ru.sergonas.jabberbot;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;
import ru.sergonas.jabberbot.plugins.BadHandlerException;
import ru.sergonas.jabberbot.plugins.CommandHandler;

import java.util.Arrays;
import java.util.HashMap;

/**
 * No JavaDoc
 * User: serega
 * Date: 03.08.13
 * Time: 17:12
 */
public class MUCMessageListener implements PacketListener {
    private final MultiUserChat MUC;
    private final String BOT_NAME;
    private final HashMap<String, CommandHandler> handlerPool;
    private boolean quitTrigger;

    public MUCMessageListener(String botName, MultiUserChat multiUserChat) {
        this.BOT_NAME = botName;
        this.MUC = multiUserChat;
        handlerPool = new HashMap<>();
        quitTrigger = false;
    }

    @Override
    public void processPacket(Packet packet) {
        if(packet instanceof Message) {
            handleMessage((Message) packet);
        }
    }

    public void addCommandHandler(CommandHandler handler) throws BadHandlerException {
        if(handler == null) throw new IllegalArgumentException("handler must not be null");
        String commandName = handler.getCommandName();
        if(commandName == null) throw new BadHandlerException("Command handler name must not be null");
        if(commandName.contains(" ")) throw new BadHandlerException("Command handler must not contain \"space\" in command name");
        if("quit".equals(commandName)) throw new BadHandlerException("Command handler name must not equals to \"quit\"");
        handlerPool.put(handler.getCommandName(), handler);
    }

    public boolean isQuit() {
        return quitTrigger;
    }

    private void handleMessage(Message msg) {
        //System.out.printf("<%s> %s%n",msg.getFrom(), msg.getBody());
        logMessage(msg);
        String body = msg.getBody();
        if(isItForMe(body)) {
            try {
                executeCmd(body);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCmd(String body) throws XMPPException {
        String args[] = body.split(" ");
        if(args.length <= 1) {
            MUC.sendMessage("No command");
            return;
        }

        if(args.length > 1) {
            if("quit".equals(args[1])) {
                quitChat();
                return;
            }

            String paramString = Arrays.toString(Arrays.copyOfRange(args, 2, args.length));
            CommandHandler currentCommandHandler = handlerPool.get(args[1]);
            if(currentCommandHandler != null) {
                currentCommandHandler.executeCommand(paramString);
            } else {
                MUC.sendMessage("No such command.");
            }
        }

    }

    private void quitChat() throws XMPPException {
        synchronized (MUC) {
            MUC.sendMessage("Good bye");
            MUC.leave();
            quitTrigger = true;
            MUC.notifyAll();
        }
    }

    private void logMessage(Message msg) {
        //TODO create logger
    }

    private boolean isItForMe(String body) {
        //TODO implement ignoring spaces
        return body.startsWith(BOT_NAME);
    }
}
