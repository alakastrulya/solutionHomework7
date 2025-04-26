package iterator;

public class StreamingServiceIterator {
    // make big series with 10k episodes
    public static Series generateLargeSeries() {
        Series series = new Series();
        int seasons = 100; // 100 seasons
        int episodesPerSeason = 100; // 100 episodes per season

        for (int s = 1; s <= seasons; s++) {
            Season season = new Season();
            for (int e = 1; e <= episodesPerSeason; e++) {
                String title = String.format("S%dE%d", s, e);
                int runtime = 1800 + (s * e) % 300;
                Episode episode = new Episode(title, runtime);
                if ((s * e) % 3 == 0) { // mark every third as watched
                    episode.markAsWatched();
                }
                season.addEpisode(episode);
            }
            series.addSeason(season);
        }
        return series;
    }

    // ascii bar (1 * per 100 micros)
    public static String createBar(long timeMicros) {
        int barLength = (int) (timeMicros / 100); // 1 * per 100 micros
        return "*".repeat(Math.max(0, barLength));
    }
}