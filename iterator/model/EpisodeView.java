package iterator.model;

public class EpisodeView {
    // initial episode
    private Episode episode;
    //time to skip
    private int offsetSeconds;

    public EpisodeView(Episode episode, int offsetSeconds) {
        this.episode = episode;
        this.offsetSeconds = offsetSeconds;
    }

    public Episode getEpisode() {
        return episode;
    }

    public int getOffsetSeconds() {
        return offsetSeconds;
    }

    //method to demonstrate playback
    public void play() {
        System.out.println("Playing '" + episode.getTitle() + "' starting at " + offsetSeconds + " seconds " +
                "(total runtime: " + episode.getRuntimeSec() + " seconds)");
    }
}
