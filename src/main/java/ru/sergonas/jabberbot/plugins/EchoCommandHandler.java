package ru.sergonas.jabberbot.plugins;

/**
 * Created with IntelliJ IDEA.
 * User: serega
 * Date: 03.08.13
 * Time: 20:35
 * To change this template use File | Settings | File Templates.
 */
public class EchoCommandHandler implements CommandHandler {
    private static final String COMMAND_NAME = "echo";
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String executeCommand(String args) {
        return args;
    }
}
