package ru.sergonas.jabberbot.plugins;

/**
 * Interface to command handler.
 * User: serega
 * Date: 03.08.13
 * Time: 17:32
 */
public interface CommandHandler {
    public String getCommandName();

    /**
     *
     * @param args argumets to command
     * @return response of command
     */
    public String executeCommand(String args);
}
