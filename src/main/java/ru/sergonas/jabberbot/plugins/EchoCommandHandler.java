package ru.sergonas.jabberbot.plugins;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 20:35
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
