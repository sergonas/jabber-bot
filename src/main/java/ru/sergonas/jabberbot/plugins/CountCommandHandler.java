package ru.sergonas.jabberbot.plugins;

/**
 * User: Сергей
 * Date: 13.08.13
 * Time: 14:54
 */
public class CountCommandHandler implements CommandHandler {
    private static final String COMMAND_NAME = "count";

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public String executeCommand(String args) {
        //TODO get access to DB
        int count = findInDB(args);
        return String.format("Count of \"%s\" is %d", args, count);
    }

    private int findInDB(String toFind) {
        return 0;
    }
}
