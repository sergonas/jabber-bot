package ru.sergonas.jabberbot;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import ru.sergonas.jabberbot.plugins.BadHandlerException;
import ru.sergonas.jabberbot.plugins.CommandHandler;
import java.util.Arrays;
import java.util.HashMap;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 19:11
 */
public class MessageHandler {
    private final HashMap<String, CommandHandler> handlerPool;
    private final String BOT_NAME;
    private boolean quitTrigger;

    public MessageHandler(String botName) {
        this.BOT_NAME = botName;
        handlerPool = new HashMap<>();
        quitTrigger = false;
    }

    public String handleMessage(Message msg) {
        String body = msg.getBody();
        if(isItForMe(body)) {
            String response = null;
            try {
                response = executeCmd(body);
            } catch (XMPPException e) {
                e.printStackTrace();
            }
            return response;
        } else
            return null;
    }

    private String executeCmd(String body) throws XMPPException {
        String args[] = body.split(" ");
        if(args.length <= 1) {
            return "No command.";
        } else if(args.length > 1) {
            if("quit".equals(args[1])) {
                quitTrigger = true;
                return "Good bye.";
            }
            String paramString = Arrays.toString(Arrays.copyOfRange(args, 2, args.length));
            CommandHandler currentCommandHandler = handlerPool.get(args[1]);
            if(currentCommandHandler != null) {
                return currentCommandHandler.executeCommand(paramString);
            } else {
                return "No such command.";
            }
        } else {
            return null;
        }
    }

    public boolean isQuit() {
        return quitTrigger;
    }

    public void reset() {
        quitTrigger = false;
    }

    public void addCommandHandler(CommandHandler handler) throws BadHandlerException {
        if(handler == null) throw new IllegalArgumentException("handler must not be null");
        String commandName = handler.getCommandName();
        if(commandName == null) throw new BadHandlerException("Command handler name must not be null");
        if(commandName.contains(" ")) throw new BadHandlerException("Command handler must not contain \"space\" in command name");
        if("quit".equals(commandName)) throw new BadHandlerException("Command handler name must not equals to \"quit\"");
        handlerPool.put(handler.getCommandName(), handler);
    }
    private boolean isItForMe(String body) {
        //TODO implement ignoring spaces
        return body.startsWith(BOT_NAME);
    }
}
