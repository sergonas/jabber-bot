package ru.sergonas.jabberbot;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import ru.sergonas.jabberbot.plugins.BadHandlerException;
import ru.sergonas.jabberbot.plugins.CommandHandler;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 19:11
 */
public class MessageHandler {
    private final HashMap<String, CommandHandler> handlerPool;


    public MessageHandler() {
        handlerPool = new HashMap<>();
    }

    public void addCommandHandler(CommandHandler handler) throws BadHandlerException {
        if(handler == null) throw new IllegalArgumentException("handler must not be null");
        String commandName = handler.getCommandName();
        if(commandName == null) throw new BadHandlerException("Command handler name must not be null");
        if(commandName.contains(" ")) throw new BadHandlerException("Command handler must not contain \"space\" in command name");
        handlerPool.put(handler.getCommandName(), handler);
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

    private boolean isItForMe(String body) {
        return true;
    }

    private String executeCmd(String body) throws XMPPException {
        String args[] = body.split(" ");
        if(args.length <= 1) {
            return "No command.";
        } else if(args.length > 1) {
            String paramString = body.replaceAll("\\S+\\s+\\w+\\s+", "");
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
}
