package ru.collapsedev.collapseapi.api.menu.action;

import org.bukkit.event.inventory.ClickType;


public interface MenuQuoteAction extends IMenuAction {

     void onAction(ClickType clickType, String quote);

}