package ru.sergonas.jabberbot;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * User: serega
 * Date: 03.08.13
 * Time: 19:08
 */
public class ConfigManager {
    private static HashMap<String, String> vault = new HashMap<>();

    public static void loadConfig(File pathToConfig) {
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            Document doc = builder.parse(pathToConfig);
            NodeList nodes = doc.getFirstChild().getChildNodes();
            for(int i = 0; i < nodes.getLength(); i++) {
                if(!nodes.item(i).hasChildNodes()) continue;
                vault.put(nodes.item(i).getNodeName(), nodes.item(i).getFirstChild().getTextContent());
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static String getParam(String key) {
        return vault.get(key);
    }
}
