package iterator.model;
import iterator.iterator.BingeIterator;
import iterator.iterator.EpisodeIterator;

import java.util.LinkedList;
import java.util.List;

//storing seasons of the series
public class Series{
    private List<Season> seasons = new LinkedList<>();

    public void addSeason(Season s) {
        seasons.add(s);
    }

    public EpisodeIterator getBingeIterator() {
        return new BingeIterator(this);
    }

    public List<Season> getSeasons() {
        return seasons;
    }
}
