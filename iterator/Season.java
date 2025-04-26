package iterator;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//class that stores episodes and implements the Iterable interface (support for-each)
public class Season implements Iterable<Episode> {
    //the list of episodes (its hidden from the client)
    private List<Episode> episodes = new LinkedList<>();

    // an episode to a season adding method
    public void addEpisode(Episode e) {
        episodes.add(e);
    }

// get iterator in normal order
    public EpisodeIterator getNormalIterator() {
        // return a new SeasonIterator, passing the current season
        return new SeasonIterator(this);
    }

//get the iterator in reverse
    public EpisodeIterator getReverseIterator() {
        return new ReverseIterator(this);
    }

//get random iterator with fixed seed
    public EpisodeIterator getShuffleIterator(long seed) {
        return new ShuffleIterator(this, seed);
    }
// getter for list of episodes
    List<Episode> getEpisodes() {
        return episodes;
    }
// implementation of the method from Iterable
    @Override
    public Iterator<Episode> iterator() {
        // returning iterator in usual way that support for-each
        return new SeasonIterator(this);
    }

    public SkipIntroIterator getSkipIntroIterator(int skipSeconds) {
        return new SkipIntroIterator(this.getNormalIterator(), skipSeconds);
    }

    public WatchHistoryIterator getWatchHistoryIterator() {
        return new WatchHistoryIterator(this.getNormalIterator());
    }
}
