package sorts;

import entertainment.Video;

import java.util.List;

public final class SortRecommendationBestUnseen implements SortRecommendation {
    @Override
    public void sort(final List<Video> videos) {
        videos.sort((v1, v2) -> {
            if (v2.getRatingVideo().equals(v1.getRatingVideo())) {
                return v1.getIndexInDatabase().compareTo(v2.getIndexInDatabase());
            }
            return v2.getRatingVideo().compareTo(v1.getRatingVideo());
        });
    }
}
