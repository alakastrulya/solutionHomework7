package iterator.iterator;

import iterator.model.Episode;
import iterator.model.Series;

import java.util.NoSuchElementException;
// iterating over all episodes of series in a row
public class BingeIterator implements EpisodeIterator {
    //storing a reference to the series
    private Series series;
    //tracking the current season
    private int currentSeasonIndex;
    //storing  iterator of the current season
    private EpisodeIterator currentSeasonIterator;

    public BingeIterator(Series series) {
        this.series = series;
        // initial season index
        this.currentSeasonIndex = 0;
        // check if there are seasons
        this.currentSeasonIterator = series.getSeasons().isEmpty() ?
         //take the first season iterator, otherwise null.
                null : series.getSeasons().get(0).getNormalIterator();
    }
// Check if there is  next episode
    @Override
    public boolean hasNext() {
        // if there are no seasons
        if (currentSeasonIterator == null) {
            return false;
        }
// if there are episodes in the current season
        if (currentSeasonIterator.hasNext()) {
            return true; // Return true.
        }

// moving to the next season
        while (++currentSeasonIndex < series.getSeasons().size()) {
            // increment season index and check if there more seasons
            currentSeasonIterator = series.getSeasons()
                    // get the next iterator
                    .get(currentSeasonIndex).getNormalIterator();
            // if the new season has episodes
            if (currentSeasonIterator.hasNext()) {
                return true;
            }
        }
        return false; // if the seasons or episodes ended, return false
    }
    // Return the next episode
    @Override
    public Episode next() {
        // check if there a next episode
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return currentSeasonIterator.next();
    }


}