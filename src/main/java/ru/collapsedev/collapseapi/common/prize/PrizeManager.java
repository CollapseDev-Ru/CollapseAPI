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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrizeManager {

    final List<Prize> prizes;
    int minPrizesAlways = 0;
    int maxPrizesAlways = 0;

    public static PrizeManager of(List<Prize> prizes, String range) {
        String[] args = range.split("-");
        int minPrizesAlways = Integer.parseInt(args[0]);
        int maxPrizesAlways = Integer.parseInt(args[1]);

        return new PrizeManager(prizes, minPrizesAlways, maxPrizesAlways);
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

        if (minPrizesAlways == 0 && maxPrizesAlways == 0)  {
            sortPrizes.addAll(tmpPrizes.stream()
                    .filter(prize -> checkChance(prize.getChance()))
                    .collect(Collectors.toList()));
        } else {
            while (counter.get() < getRandomPrizeCount()) {
                Prize prize = tmpPrizes.get(RandomUtil.randomInt(tmpPrizes.size()));
                if (!sortPrizes.contains(prize) && checkChance(prize.getChance())) {
                    sortPrizes.add(prize);
                    counter.incrementAndGet();
                }
            }
        }

        return sortPrizes;
    }

    public boolean checkChance(double chance) {
        return RandomUtil.randomDouble(100) < chance;
    }

    public int getRandomPrizeCount() {
        return Math.max(ThreadLocalRandom.current()
                .nextInt(minPrizesAlways, maxPrizesAlways), 1);
    }
}
