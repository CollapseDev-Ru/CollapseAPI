package ru.collapsedev.collapseapi.common.random;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.collapsedev.collapseapi.api.random.RandomItem;
import ru.collapsedev.collapseapi.util.RandomUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RandomItemManager {

    final List<RandomItem> items;
    int minItemsAlways = 0;
    int maxItemsAlways = 0;

    public static RandomItemManager of(List<RandomItem> items, String range) {

        if (!range.equals("-1")) {
            String[] args = range.split("-");

            int minItemsAlways = Integer.parseInt(args[0]);
            minItemsAlways = Math.min(minItemsAlways, items.size());
            int maxItemsAlways = args.length == 1 ? minItemsAlways : Integer.parseInt(args[1]);
            maxItemsAlways = Math.min(maxItemsAlways, items.size());

            return new RandomItemManager(items, minItemsAlways, maxItemsAlways);
        }

        return new RandomItemManager(items);
    }

    public List<RandomItem> getRandomItems() {
        Collections.shuffle(items);

        AtomicInteger counter = new AtomicInteger();

        List<RandomItem> sortItems = new ArrayList<>();
        List<RandomItem> tmpItems = new ArrayList<>();
        items.forEach(prize -> {
            if (prize.getChance() == 100) {
                sortItems.add(prize);
            } else {
                tmpItems.add(prize);
            }
        });

        if (minItemsAlways == 0 && maxItemsAlways == 0)  {
            sortItems.addAll(tmpItems.stream()
                    .filter(prize -> RandomUtil.random100(prize.getChance()))
                    .collect(Collectors.toList()));
        } else {
            int randomPrizeCount = getRandomItemsCount();
            while (!tmpItems.isEmpty() && counter.get() < randomPrizeCount) {
                RandomItem prize = tmpItems.get(RandomUtil.randomInt(tmpItems.size()));
                if (!sortItems.contains(prize) && RandomUtil.random100(prize.getChance())) {
                    sortItems.add(prize);
                    counter.incrementAndGet();
                }
            }
        }

        Collections.shuffle(tmpItems);
        Collections.shuffle(sortItems);

        return sortItems;
    }

    public int getRandomItemsCount() {
        if (minItemsAlways == maxItemsAlways) {
            return maxItemsAlways;
        }
        return Math.max(RandomUtil.randomInt(minItemsAlways, maxItemsAlways + 1), 1);
    }
}
