package ru.collapsedev.collapseapi.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.collapsedev.collapseapi.util.StringUtil.HEX_PATTERN;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TagParser {

    GRADIENT("<(" + HEX_PATTERN + ")>(.*?)</(" + HEX_PATTERN + ")>"),
    TITLE("<title(?:\\s+(\\d+)\\s+(\\d+)\\s+(\\d+))?>([^<:]+):([^<]+)<\\/title>"),
    COMMAND("<command \\[(.*?)\\]>(.*?)</command>"),
    SOUND("<sound (\\d+(?:\\.\\d+)?) (\\d+(?:\\.\\d+)?)>(.*?)</sound>"),
    ;

    Pattern pattern;

    TagParser(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public TagResult parse(String text, Function<List<String>, String> replacement) {
        return parse(text, pattern, replacement);
    }

    public TagResult parse(String text) {
        return parse(text, groups -> "");
    }

    private static TagResult parse(String text, Pattern pattern, Function<List<String>, String> replacement) {
        List<List<String>> matchedGroups = new ArrayList<>();
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int groupCount = matcher.groupCount();
            List<String> groups = new ArrayList<>();

            for (int i = 1; i <= groupCount; i++) {
                groups.add(matcher.group(i));
            }
            matchedGroups.add(groups);

            text = text.replace(
                    matcher.group(),
                    replacement.apply(groups)
            );
        }

        return TagResult.of(matchedGroups, text);
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public static class TagResult {
        List<List<String>> matchedGroups;
        String modifyText;
    }
}