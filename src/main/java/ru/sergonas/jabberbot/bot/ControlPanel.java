package ru.sergonas.jabberbot.bot;

/**
 * United control panel for bot commands
 * User: serega
 * Date: 22.08.13
 * Time: 0:12
 */
public interface ControlPanel {
    public void leaveRoom(String roomName);
    public void shutdown(String goodByeMsg);
    public void changeName(String newName);
    public String getBotName();
    public boolean isQuit();
}
