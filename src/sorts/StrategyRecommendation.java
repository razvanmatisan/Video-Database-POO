package sorts;

import entertainment.Video;

import java.util.List;

public final class StrategyRecommendation {
    private final SortRecommendation sortRecommendation;

    public StrategyRecommendation(final SortRecommendation sortRecommendation) {
        this.sortRecommendation = sortRecommendation;
    }

    public void sort(final List<Video> videos) {
        sortRecommendation.sort(videos);
    }
}
