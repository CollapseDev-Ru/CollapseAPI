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
        String formattedText = StringUtil.replaceObjects(text, placeholders);

//        formattedLines = ObjectUtil.mapList(
//                formattedLines, line -> parseTags(sender, line)
//        );
//
//        formattedLines.removeIf(line -> line.equals("remove"));
//
//        formattedLines.forEach(sender::sendMessage);

        sender.sendMessage(formattedText);


        String command = "<command [НАЖМИ]>enderchest</command>";

        String sound = "<sound 1 0.5>UI_BUTTON_CLICK</sound>";

//        TagParser.TITLE.parse(title).forEach(args -> {
//            System.out.println(Arrays.toString(args));
//        });
//        TagParser.COMMAND.parse(command).forEach(args -> {
//            System.out.println(Arrays.toString(args));
//        });
//        TagParser.SOUND.parse(sound).forEach(args -> {
//            System.out.println(Arrays.toString(args));
//        });
    }

    public String parseTags(CommandSender sender, String line) {
        TagParser.TagResult result = TagParser.TITLE.parse(line);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            sendTitle(player, result);
        }

        line = result.getModifyText();

        if (line.isEmpty()) {
            return "remove";
        }

        return line;
    }
    public void sendTitle(Player player, TagParser.TagResult result) {
        result.getMatchedGroups().forEach(groups -> {
            groups.replaceAll(group -> group == null ? "20" : group);

            groups.add(0, groups.remove(groups.size() - 2));
            groups.add(1, groups.remove(groups.size() - 1));

            PlayerUtil.sendTitle(player, String.join(":", groups));
        });
    }

}
