package ru.collapsedev.collapseapi.common.object;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import ru.collapsedev.collapseapi.util.ObjectUtil;
import ru.collapsedev.collapseapi.util.StringUtil;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class Message {

    String message;

    public static Message of(Object object) {
        String message = null;
        if (object instanceof String) {
            message = (String) object;
        } else if (object instanceof List) {
            message = StringUtil.listToString(ObjectUtil.castValue(object));
        }

        if (message == null) {
            throw new IllegalArgumentException();
        }

        message = StringUtil.color(message);

        return new Message(message);
    }

    public void sendMessage(CommandSender sender, Object... args) {
        sender.sendMessage(StringUtil.replaceObjects(message, args));
    }
}
