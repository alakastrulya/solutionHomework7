package iterator.iterator;

import iterator.model.Episode;

import java.util.Iterator;
//interface for episode iterator
public interface EpisodeIterator extends Iterator<Episode> {
    // checking if there has a next episode
    boolean hasNext();
    // returning the next episode
    Episode next();

}
