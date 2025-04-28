package iterator.iterator;
import iterator.model.Episode;
import iterator.model.Season;

import java.util.NoSuchElementException;

//iterate through episodes in reverse order
public class ReverseIterator implements EpisodeIterator {
    private Season season;
    private int currentIndex; // track the current position.

    public ReverseIterator(Season season) {
        this.season = season;
//set the starting index like last episode
        this.currentIndex = season.getEpisodes().size() - 1;
    }

    // check if there have next episode
    @Override
    public boolean hasNext() {
        // Rrturn true if we have not reached the beginning
        return currentIndex >= 0;
    }

    @Override
    public Episode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return season.getEpisodes().get(currentIndex--);
    }
}
