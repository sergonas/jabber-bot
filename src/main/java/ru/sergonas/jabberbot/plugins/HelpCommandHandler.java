package ru.sergonas.jabberbot.plugins;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 21:20
 */
public class HelpCommandHandler implements CommandHandler {
    private static final String COMMAND_NAME = "echo";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String executeCommand(String args) {
        return "No help for u, criminal scum!";
    }
}
