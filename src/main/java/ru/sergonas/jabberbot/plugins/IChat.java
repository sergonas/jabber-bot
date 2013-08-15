package ru.sergonas.jabberbot.plugins;

/**
 * Говно итерфейс дабы реализовать костыль выходящий из чата. Не использовать!
 * TODO избавится от этого интерфейса и реализовать нормальную логику выхода.
 * User: Сергей
 * Date: 15.08.13
 * Time: 12:45
 */
@Deprecated
public interface IChat {
    public void quitChat();
}
