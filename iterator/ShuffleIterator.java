package iterator;

import java.util.*;

public class ShuffleIterator implements EpisodeIterator {
    private List<Episode> shuffledEpisodes;
    private int currentIndex;

    public ShuffleIterator(Season season, long seed) {
        this.shuffledEpisodes = new ArrayList<>(season.getEpisodes());
// shuffle the list using Random with a fixed seed
        Collections.shuffle(this.shuffledEpisodes, new Random(seed));
// Set the starting index to 0
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < shuffledEpisodes.size();
    }

    @Override
    public Episode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return shuffledEpisodes.get(currentIndex++);
    }
}
