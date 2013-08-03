package ru.sergonas.jabberbot.plugins;

/**
 * Interface to command handler.
 * User: serega
 * Date: 03.08.13
 * Time: 17:32
 */
public interface CommandHandler {
    public String getCommandName();
    public void executeCommand(String args);
}
