package iterator;

import java.util.NoSuchElementException;
// class to iterate through episodes in the usual order
public class SeasonIterator implements EpisodeIterator {
    private Season season;
    private int currentIndex;

    public SeasonIterator(Season season) {
        this.season = season;
        this.currentIndex = 0;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < season.getEpisodes().size();
    }

    @Override
    public Episode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // return the episode the current index and increment the index
        return season.getEpisodes().get(currentIndex++);
    }

}
