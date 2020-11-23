package sorts;

import entertainment.Video;

import java.util.List;

public final class StrategyVideo {
    private final SortVideo sortVideo;

    public StrategyVideo(final SortVideo sortVideo) {
        this.sortVideo = sortVideo;
    }

    public void sort(final String sortType, final List<Video> videos) {
        sortVideo.sort(sortType, videos);
    }
}
