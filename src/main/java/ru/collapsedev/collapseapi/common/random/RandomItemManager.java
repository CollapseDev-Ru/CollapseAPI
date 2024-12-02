package ru.collapsedev.collapseapi.common.random;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class RandomItemManager<V extends RandomItem> {

    @Getter
    final List<V> items;
    int minItemsAlways = 0;
    int maxItemsAlways = 0;

    public static <V extends RandomItem> RandomItemManager<V> of(List<V> items, String range) {

        if (!range.equals("-1")) {
            String[] args = range.split("-");

            int minItemsAlways = Integer.parseInt(args[0]);
            minItemsAlways = Math.min(minItemsAlways, items.size());
            int maxItemsAlways = args.length == 1 ? minItemsAlways : Integer.parseInt(args[1]);
            maxItemsAlways = Math.min(maxItemsAlways, items.size());

            return new RandomItemManager<>(items, minItemsAlways, maxItemsAlways);
        }

        return new RandomItemManager<>(items);
    }

    public List<V> getRandomItems() {
        Collections.shuffle(items);

        List<V> sortItems = new ArrayList<>();
        List<V> tmpItems = new ArrayList<>();
        items.forEach(prize -> {
            if (prize.getChance() == 100) {
                sortItems.add(prize);
            } else {
                tmpItems.add(prize);
            }
        });
        Collections.shuffle(tmpItems);

        if (minItemsAlways == 0 && maxItemsAlways == 0)  {
            sortItems.addAll(tmpItems.stream()
                    .filter(prize -> RandomUtil.random100(prize.getChance()))
                    .collect(Collectors.toList()));
        } else {
            int randomPrizeCount = getRandomItemsCount();
            AtomicInteger counter = new AtomicInteger();

            while (!tmpItems.isEmpty() && counter.get() < randomPrizeCount) {
                V prize = tmpItems.get(RandomUtil.randomInt(tmpItems.size()));
                if (!sortItems.contains(prize) && RandomUtil.random100(prize.getChance())) {
                    sortItems.add(prize);
                    counter.incrementAndGet();
                }
            }
        }

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
