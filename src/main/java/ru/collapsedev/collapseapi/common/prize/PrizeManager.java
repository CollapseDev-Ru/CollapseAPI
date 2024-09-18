package ru.collapsedev.collapseapi.common.prize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.api.prize.Prize;
import ru.collapsedev.collapseapi.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrizeManager {

    final List<Prize> prizes;
    int minPrizesAlways = -1;

    public void give(String playerName) {
        getRandomPrizes().forEach(prize -> prize.give(playerName));
    }

    public List<Prize> getRandomPrizes() {
        Collections.shuffle(prizes);

        AtomicInteger counter = new AtomicInteger();

        List<Prize> sortPrizes = new ArrayList<>();
        List<Prize> tmpPrizes = new ArrayList<>();
        prizes.forEach(prize -> {
            if (prize.getChance() == 100) {
                sortPrizes.add(prize);
            } else {
                tmpPrizes.add(prize);
            }
        });

        if (minPrizesAlways == -1) {
            sortPrizes.addAll(tmpPrizes.stream()
                    .filter(Prize::checkChance)
                    .collect(Collectors.toList()));
        } else {
            while (counter.get() < minPrizesAlways) {
                Prize prize = tmpPrizes.get(RandomUtil.randomInt(tmpPrizes.size()));
                if (!sortPrizes.contains(prize) && prize.checkChance()) {
                    sortPrizes.add(prize);
                    counter.incrementAndGet();
                }
            }
        }

        return sortPrizes;
    }
}
