package iterator;

import java.util.NoSuchElementException;

public class WatchHistoryIterator implements EpisodeIterator {
    // iterator that we wrap
    private EpisodeIterator wrappedIterator;
    // Store the next unwatched episode
    private Episode nextUnwatchedEpisode;

    public WatchHistoryIterator(EpisodeIterator wrappedIterator) {
        this.wrappedIterator = wrappedIterator;
        // search for the first unwatched episode when creating
        advanceToNextUnwatched();
    }

    private void advanceToNextUnwatched() {
        nextUnwatchedEpisode = null;
        while (wrappedIterator.hasNext()) {
            Episode episode = wrappedIterator.next();
            // if the episode has not been watched
            if (!episode.isWatched()) {
                nextUnwatchedEpisode = episode;
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nextUnwatchedEpisode != null;
    }

    @Override
    public Episode next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Episode result = nextUnwatchedEpisode;
        // look for the next unwatched episode
        advanceToNextUnwatched();
        return result;
    }

}
