package ru.sergonas.jabberbot;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: serega
 * Date: 22.08.13
 * Time: 0:19
 */
public class XMPPBot implements ControlPanel {
    private Connection connection;
    private boolean quitTrigger;
    private String botname;
    private HashMap<String, MultiUserChat> rooms;


    public XMPPBot(String server, String botname) {
        this.connection = new XMPPConnection(server);
        this.rooms = new HashMap<>();
        this.botname = botname;
        quitTrigger = false;
    }

    public void addRoomToWorkWith(String roomAddress) {
        rooms.put(roomAddress,  new MultiUserChat(connection, roomAddress));
    }

    public void connect(String username, String password) throws XMPPException {
        connection.connect();
        connection.login(username, password);
        setPresence();
        DiscussionHistory dh = new DiscussionHistory();
        dh.setMaxChars(0);
        for(MultiUserChat room : rooms.values()) {
            room.join(botname, "", dh, 1000);
            room.addMessageListener(new MUCMessageListener(room));
        }
    }

    @Override
    public void leaveRoom(String roomName) {
        MultiUserChat room = rooms.get(roomName);

        if(room.isJoined())
            room.leave();
    }

    @Override
    public void shutdown(String goodByeMsg) {
        this.quitTrigger = true;
        for (MultiUserChat muc : rooms.values()) {
            if(muc.isJoined())
                muc.leave();
        }

        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public void changeName(String newName) {

    }

    @Override
    public String getBotName() {
        return botname;
    }

    @Override
    public boolean isQuit() {
        return false;
    }

    public void setPresence() {
        Presence presence = new Presence(Presence.Type.available);
        connection.sendPacket(presence);
    }
}
