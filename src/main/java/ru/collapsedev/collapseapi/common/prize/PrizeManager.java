package ru.collapsedev.collapseapi.common.prize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.api.prize.Prize;
import ru.collapsedev.collapseapi.util.RandomUtil;

import java.util.*;
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

        if (!range.equals("-1")) {
            String[] args = range.split("-");

            int minPrizesAlways = Integer.parseInt(args[0]);
            minPrizesAlways = Math.min(minPrizesAlways, prizes.size());
            int maxPrizesAlways = args.length == 1 ? minPrizesAlways : Integer.parseInt(args[1]);
            maxPrizesAlways = Math.min(maxPrizesAlways, prizes.size());

            return new PrizeManager(prizes, minPrizesAlways, maxPrizesAlways);
        }

        return new PrizeManager(prizes);
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
        Collections.shuffle(tmpPrizes);
        Collections.shuffle(sortPrizes);

        if (minPrizesAlways == 0 && maxPrizesAlways == 0)  {
            sortPrizes.addAll(tmpPrizes.stream()
                    .filter(prize -> RandomUtil.random100(prize.getChance()))
                    .collect(Collectors.toList()));
        } else {
            int randomPrizeCount = getRandomPrizeCount();
            while (counter.get() < randomPrizeCount) {
                Prize prize = tmpPrizes.get(RandomUtil.randomInt(tmpPrizes.size()));
                if (!sortPrizes.contains(prize) && RandomUtil.random100(prize.getChance())) {
                    sortPrizes.add(prize);
                    counter.incrementAndGet();
                }
            }
        }

        return sortPrizes;
    }

    public int getRandomPrizeCount() {
        return Math.max(RandomUtil.randomInt(minPrizesAlways, maxPrizesAlways + 1), 1);
    }
}
