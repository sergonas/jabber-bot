package ru.sergonas.jabberbot.plugins;

import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.*;

/**
 * User: Сергей
 * Date: 13.08.13
 * Time: 14:59
 */
public class CountPluginTest {
    @Test
    public void findMock() {
        CountCommandHandler cch = new CountCommandHandler();
        String comName = cch.getCommandName();

        assertTrue("count".equals(comName));

        //Должно считать количество вхождений mock в базе
        String resp = cch.executeCommand("\"fuck\"");
        System.out.println(resp);
        assertNotNull(resp);
        assertTrue(Pattern.matches("Count of \"[^\"]+\" is \\d+", resp ));
    }
}
