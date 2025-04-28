package iterator.iterator;

import iterator.model.Episode;
import iterator.model.EpisodeView;

import java.util.Iterator;
import java.util.NoSuchElementException;

class SkipIntroIterator implements Iterator<EpisodeView> {
    private EpisodeIterator wrappedIterator;
    // number of seconds to skip
    private int skipSeconds;

    public SkipIntroIterator(EpisodeIterator wrappedIterator, int skipSeconds) {
        this.wrappedIterator = wrappedIterator;
        this.skipSeconds = skipSeconds;
    }

    @Override
    public boolean hasNext() {
// delegate checking to the original iterator
        return wrappedIterator.hasNext();
    }

    @Override
    public EpisodeView next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        // get the next episode
        Episode episode = wrappedIterator.next();
// wrap it in EpisodeView with the given offset
        return new EpisodeView(episode, skipSeconds);
    }

}