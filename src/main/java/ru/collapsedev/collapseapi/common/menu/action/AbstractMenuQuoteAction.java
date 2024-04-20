package ru.collapsedev.collapseapi.common.menu.action;

import lombok.Getter;
import lombok.Setter;
import ru.collapsedev.collapseapi.api.menu.action.MenuQuoteAction;

@Setter
@Getter
public abstract class AbstractMenuQuoteAction implements MenuQuoteAction {
    private String quote;
}
