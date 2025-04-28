package iterator.simulator;


import iterator.iterator.EpisodeIterator;
import iterator.iterator.SkipIntroIterator;
import iterator.iterator.WatchHistoryIterator;
import iterator.model.Episode;
import iterator.model.EpisodeView;
import iterator.model.Season;
import iterator.model.Series;

import static iterator.iterator.StreamingServiceIterator.createBar;
import static iterator.iterator.StreamingServiceIterator.generateLargeSeries;

public class Main {
    public static void main(String[] args) {

        Series series = new Series();
        Season season1 = new Season();
        Season season2 = new Season();
        Season season3 = new Season();
        Season season4 = new Season();

        // season 1
        season1.addEpisode(new Episode("1x01: Winter Is Coming", 3600)); // 60 min
        season1.addEpisode(new Episode("1x02: The Kingsroad", 3300));   // 55 min
        season1.addEpisode(new Episode("1x03: Lord Snow", 3400));       // 56 min
        season1.addEpisode(new Episode("1x04: Cripples, Bastards, and Broken Things", 3500)); // 58 min
        season1.addEpisode(new Episode("1x05: The Wolf and the Lion", 3300)); // 55 min

        // season 2
        season2.addEpisode(new Episode("2x01: The North Remembers", 3500)); // 58 min
        season2.addEpisode(new Episode("2x02: The Night Lands", 3400));     // 56 min
        season2.addEpisode(new Episode("2x03: What Is Dead May Never Die", 3600)); // 60 min
        season2.addEpisode(new Episode("2x04: Garden of Bones", 3300));     // 55 min
        season2.addEpisode(new Episode("2x05: The Ghost of Harrenhal", 3400)); // 56 min

        // season 3
        season3.addEpisode(new Episode("3x01: Valar Dohaeris", 3500)); // 58 min
        season3.addEpisode(new Episode("3x02: Dark Wings, Dark Words", 3400)); // 56 min
        season3.addEpisode(new Episode("3x03: Walk of Punishment", 3300)); // 55 min
        season3.addEpisode(new Episode("3x04: And Now His Watch Is Ended", 3600)); // 60 min

        // season 4
        season4.addEpisode(new Episode("4x01: Two Swords", 3600)); // 60 min
        season4.addEpisode(new Episode("4x02: The Lion and the Rose", 3500)); // 58 min
        season4.addEpisode(new Episode("4x03: Breaker of Chains", 3400)); // 56 min
        season4.addEpisode(new Episode("4x04: Oathkeeper", 3300)); // 55 min

        series.addSeason(season1);
        series.addSeason(season2);
        series.addSeason(season3);
        series.addSeason(season4);

        System.out.println("Results of all Iterators:");

        // show normal order
        printSectionHeader("Normal Episode Order (Season 1)");
        EpisodeIterator normalIter = season1.getNormalIterator();
        int episodeCount = 1;
        while (normalIter.hasNext()) {
            System.out.printf("Episode %d: %s%n", episodeCount++, normalIter.next().getTitle());
        }

        // reverse order
        printSectionHeader("Reverse Episode Order (Season 1)");
        EpisodeIterator reverseIter = season1.getReverseIterator();
        episodeCount = 1;
        while (reverseIter.hasNext()) {
            System.out.printf("Episode %d: %s%n", episodeCount++, reverseIter.next().getTitle());
        }

        // shuffled order
        printSectionHeader("Shuffled Episode Order (Season 1)");
        EpisodeIterator shuffleIter = season1.getShuffleIterator(42);
        episodeCount = 1;
        while (shuffleIter.hasNext()) {
            System.out.printf("Episode %d: %s%n", episodeCount++, shuffleIter.next().getTitle());
        }

        // our binge watching
        printSectionHeader("Binge Watching (All Seasons)");
        EpisodeIterator bingeIter = series.getBingeIterator();
        episodeCount = 1;
        while (bingeIter.hasNext()) {
            System.out.printf("Episode %d: %s%n", episodeCount++, bingeIter.next().getTitle());
        }

        // for-each
        printSectionHeader("Using for-each (Season 1)");
        episodeCount = 1;
        for (Episode e : season1) {
            System.out.printf("Episode %d: %s%n", episodeCount++, e.getTitle());
        }

        //the skip intro
        printSectionHeader("Skip Intro (Season 1, skipping 10 seconds)");
        SkipIntroIterator skipIntroIter = season1.getSkipIntroIterator(10);
        episodeCount = 1;
        while (skipIntroIter.hasNext()) {
            EpisodeView view = skipIntroIter.next();
            System.out.printf("Episode %d: %s (Playing with intro skipped)%n", episodeCount++, view.getEpisode().getTitle());
            view.play();
        }

        //  watch history filter(optional)
        printSectionHeader("Watch History Filter (Unwatched Episodes, Season 1)");
        // mark some as watched
        season1.getEpisodes().get(0).markAsWatched(); // 1x01 watched
        season2.getEpisodes().get(1).markAsWatched(); // 2x02 watched
        season3.getEpisodes().get(2).markAsWatched(); // 3x03 watched
        season4.getEpisodes().get(3).markAsWatched(); // 4x04 watched
        WatchHistoryIterator historyFilterIter = season1.getWatchHistoryIterator();
        episodeCount = 1;
        while (historyFilterIter.hasNext()) {
            System.out.printf("Unwatched Episode %d: %s%n", episodeCount++, historyFilterIter.next().getTitle());
        }

        // run test with 10.000 episodes
        printSectionHeader("Performance Test (10,000 Episodes)");
        Series largeSeries = generateLargeSeries();
        Season largeSeason = largeSeries.getSeasons().get(0); // grab first season

        // test normal iterator
        long startTime = System.nanoTime();
        EpisodeIterator normalTestIter = largeSeason.getNormalIterator();
        while (normalTestIter.hasNext()) {
            normalTestIter.next();
        }
        long normalTimeMicros = (System.nanoTime() - startTime) / 1_000; // to micros

        // reverse iterator
        startTime = System.nanoTime();
        EpisodeIterator reverseTestIter = largeSeason.getReverseIterator();
        while (reverseTestIter.hasNext()) {
            reverseTestIter.next();
        }
        long reverseTimeMicros = (System.nanoTime() - startTime) / 1_000;

        // shuffle iterator
        startTime = System.nanoTime();
        EpisodeIterator shuffleTestIter = largeSeason.getShuffleIterator(42);
        while (shuffleTestIter.hasNext()) {
            shuffleTestIter.next();
        }
        long shuffleTimeMicros = (System.nanoTime() - startTime) / 1_000;

        // test binge iterator
        startTime = System.nanoTime();
        EpisodeIterator bingeTestIter = largeSeries.getBingeIterator();
        while (bingeTestIter.hasNext()) {
            bingeTestIter.next();
        }
        long bingeTimeMicros = (System.nanoTime() - startTime) / 1_000;

        // skip intro iterator
        startTime = System.nanoTime();
        SkipIntroIterator skipIntroTestIter = largeSeason.getSkipIntroIterator(10);
        while (skipIntroTestIter.hasNext()) {
            skipIntroTestIter.next();
        }
        long skipIntroTimeMicros = (System.nanoTime() - startTime) / 1_000;

        // watch history iterator
        startTime = System.nanoTime();
        WatchHistoryIterator historyFilterTestIter = largeSeason.getWatchHistoryIterator();
        while (historyFilterTestIter.hasNext()) {
            historyFilterTestIter.next();
        }
        long historyFilterTimeMicros = (System.nanoTime() - startTime) / 1_000;

        // show perf report
        printSectionHeader("Performance Report (in microseconds)");
        System.out.printf("Normal Iterator: %d microseconds %s%n", normalTimeMicros, createBar(normalTimeMicros));
        System.out.printf("Reverse Iterator: %d microseconds %s%n", reverseTimeMicros, createBar(reverseTimeMicros));
        System.out.printf("Shuffle Iterator: %d microseconds %s%n", shuffleTimeMicros, createBar(shuffleTimeMicros));
        System.out.printf("Binge Iterator: %d microseconds %s%n", bingeTimeMicros, createBar(bingeTimeMicros));
        System.out.printf("Skip Intro Iterator: %d microseconds %s%n", skipIntroTimeMicros, createBar(skipIntroTimeMicros));
        System.out.printf("Watch History Filter Iterator: %d microseconds %s%n", historyFilterTimeMicros, createBar(historyFilterTimeMicros));
    }

    // header for sections
    private static void printSectionHeader(String title) {
        System.out.println();
        System.out.println("====== " + title + " ======");
    }
}
