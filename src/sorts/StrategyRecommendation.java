package sorts;

import entertainment.Video;

import java.util.List;

/**
 * Class that is specialized in executing a given sorting operation for recommendations.
 */
public final class StrategyRecommendation {
    private final SortRecommendation sortRecommendation;

    public StrategyRecommendation(final SortRecommendation sortRecommendation) {
        this.sortRecommendation = sortRecommendation;
    }

    /**
     * Method that implements a sorting operation for recommendations that was received
     * in constructor.
     */
    public void sort(final List<Video> videos) {
        sortRecommendation.sort(videos);
    }
}
