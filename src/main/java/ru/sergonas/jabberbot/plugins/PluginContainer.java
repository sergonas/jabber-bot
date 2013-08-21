package ru.sergonas.jabberbot.plugins;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: serega
 * Date: 22.08.13
 * Time: 1:09
 */
public class PluginContainer {
    private static volatile Set<Class<? extends CommandHandler>> instance;

    public static Set<Class<? extends CommandHandler>> getInstance() {
        Set<Class<? extends CommandHandler>> localInstance = instance;
        if (localInstance == null) {
            synchronized (PluginContainer.class) {
                localInstance = instance;
                if (localInstance == null) {
                    List<ClassLoader> classLoadersList = new LinkedList<>();
                    classLoadersList.add(ClasspathHelper.contextClassLoader());
                    classLoadersList.add(ClasspathHelper.staticClassLoader());

                    Reflections reflections = new Reflections(new ConfigurationBuilder()
                            .setScanners(new SubTypesScanner(true), new ResourcesScanner())
                            .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                            .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("ru.sergonas.jabberbot.plugins"))));

                    instance = localInstance = reflections.getSubTypesOf(CommandHandler.class);
                }
            }
        }
        return localInstance;
    }
}
