package iterator.model;
//the class that representing an episode of the series.
public class Episode {
    // the title of the episode
    private String title;
    // the duration
    private int runtimeSec;
    // new field for tracking views
    private boolean isWatched;

    public Episode(String title, int runtimeSec) {
        // constructor for that accepts a title and duration
        this.title = title;
        this.runtimeSec = runtimeSec;
        // by default the episode is not watched
        this.isWatched = false;
    }
//getters
    public String getTitle() {
        return title;
    }
    public int getRuntimeSec() {
        return runtimeSec;
    }
    public boolean isWatched() {
        return isWatched;
    }
    public void markAsWatched() {
        // mark an episode as watched
        this.isWatched = true;
    }
}


