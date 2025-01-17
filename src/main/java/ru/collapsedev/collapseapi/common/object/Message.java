package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.collapsedev.collapseapi.util.ObjectUtil;
import ru.collapsedev.collapseapi.util.PlayerUtil;
import ru.collapsedev.collapseapi.util.StringUtil;
import ru.collapsedev.collapseapi.util.TagParser;

import java.util.Collections;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    final String text;

    private static final Message EMPTY_MESSAGE = Message.of("");

    public static Message getEmptyMessage() {
        return EMPTY_MESSAGE;
    }

    public static Message of(Object object) {
        String lines = null;
        if (object instanceof String) {
            lines = (String) object;
        } else if (object instanceof List) {
            lines = StringUtil.listToString(ObjectUtil.castValue(object));
        }

        if (lines == null) {
            throw new IllegalArgumentException();
        }

        lines = StringUtil.color(lines);

        return new Message(lines);
    }

    public Message replace(String placeholder, Object value) {
        return new Message(text.replace(placeholder, value.toString()));
    }

    public void sendMessage(CommandSender sender, Object... placeholders) {
        sender.sendMessage(StringUtil.replaceObjects(text, placeholders));
    }
}
