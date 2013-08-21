package ru.sergonas.jabberbot.plugins;

import org.junit.Test;
import ru.sergonas.jabberbot.ConfManager;

import java.io.File;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * User: Сергей
 * Date: 15.08.13
 * Time: 13:49
 */
public class RebuildPluginTest {
    @Test
    public void rebuildTest() throws Exception {
        ConfManager.loadConfig(new File("botconfig.xml"));
        RebuildCommandHandler rch = new RebuildCommandHandler();
        String response = rch.executeCommand("");
        assertTrue(Pattern.matches("Rebuild started\\. Shutting down\\.",response));
    }
}
