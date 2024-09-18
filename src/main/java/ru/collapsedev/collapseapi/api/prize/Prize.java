package ru.collapsedev.collapseapi.api.prize;

import ru.collapsedev.collapseapi.common.prize.impl.PrizeImpl;
import ru.collapsedev.collapseapi.util.ObjectUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface Prize {

    double getChance();
    boolean checkChance();
    void give(String playerName);

    static Prize ofMap(Map<?, ?> map) {
        double chance = ((Number) map.get("chance")).doubleValue();

        List<String> commands = map.containsKey("command")
                ? Collections.singletonList((String) map.get("command"))
                : ObjectUtil.castValue(map.get("commands"));

        return new PrizeImpl(chance, commands);
    }
}
