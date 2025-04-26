package iterator;
//the class that representing an episode of the series.
public class Episode {
    private String title;
    // the title of the episode
    private int runtimeSec;
    // the duration

    public Episode(String title, int runtimeSec) {
        // constructor for that accepts a title and duration
        this.title = title;
        this.runtimeSec = runtimeSec;
    }
//getters
    public String getTitle() {
        return title;
    }
    public int getRuntimeSec() {
        return runtimeSec;
    }
}