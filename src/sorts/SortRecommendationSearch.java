package sorts;

import entertainment.Video;

import java.util.List;

/**
 * Class that implements the operation of asc sorting videos by their ratings.
 * Second criteria is their titles.
 */
public final class SortRecommendationSearch implements SortRecommendation {
    @Override
    public void sort(final List<Video> videos) {
        videos.sort((v1, v2) -> {
            if (v1.getRatingVideo().equals(v2.getRatingVideo())) {
                return v1.getTitle().compareTo(v2.getTitle());
            }
            return v1.getRatingVideo().compareTo(v2.getRatingVideo());
        });
    }
}
