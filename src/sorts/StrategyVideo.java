package sorts;

import entertainment.Video;

import java.util.List;

/**
 * Class that is specialized in executing a given sorting operation for videos.
 */
public final class StrategyVideo {
    private final SortVideo sortVideo;

    public StrategyVideo(final SortVideo sortVideo) {
        this.sortVideo = sortVideo;
    }

    /**
     * Method that implements a sorting operation for videos that was received
     * in constructor.
     */
    public void sort(final String sortType, final List<Video> videos) {
        sortVideo.sort(sortType, videos);
    }
}
