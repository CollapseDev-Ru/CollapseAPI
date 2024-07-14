package ru.collapsedev.collapseapi.common.armorstand.store;

import com.github.retrooper.packetevents.util.Vector3f;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.common.object.TimeFormatUnits;
import ru.collapsedev.collapseapi.util.StringUtil;
import ru.collapsedev.collapseapi.util.TimeUtil;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntitySettings {
    boolean invisible;
    boolean small;
    boolean visibleCustomName;
    String customName;
    Vector3f head;
    Vector3f body;
    boolean visibleArms;
    Vector3f leftArm;
    Vector3f rightArm;
    Vector3f leftLeg;
    Vector3f rightLeg;

    public static EntitySettings ofMap(Map<?, ?> map) {
        EntitySettingsBuilder builder = EntitySettings.builder();

        convertAndConsume(map.get("invisible"), Boolean.class, builder::invisible);
        convertAndConsume(map.get("small"), Boolean.class, builder::small);
        convertAndConsume(map.get("custom-name"), String.class, builder::customName);
        convertAndConsume(map.get("visible-arms"), Boolean.class, builder::visibleArms);

        convertAndConsumeVector3f(map.get("head"), builder::head);
        convertAndConsumeVector3f(map.get("body"), builder::body);
        convertAndConsumeVector3f(map.get("left-arm"), builder::leftArm);
        convertAndConsumeVector3f(map.get("right-arm"), builder::rightArm);
        convertAndConsumeVector3f(map.get("left-leg"), builder::leftLeg);
        convertAndConsumeVector3f(map.get("right-leg"), builder::rightLeg);

        return builder.build();
    }

    private static <T> void convertAndConsume(Object configVector, Class<T> castClass, Consumer<T> consume) {
        Optional.ofNullable(configVector)
                .map(castClass::cast)
                .ifPresent(consume);
    }

    private static void convertAndConsumeVector3f(Object configVector, Consumer<Vector3f> consume) {
        Optional.ofNullable(configVector)
                .map(String.class::cast)
                .map(str -> str.split(":"))
                .map(arr -> new Vector3f(
                        Integer.parseInt(arr[0]),
                        Integer.parseInt(arr[1]),
                        Integer.parseInt(arr[2])
                ))
                .ifPresent(consume);
    }

}