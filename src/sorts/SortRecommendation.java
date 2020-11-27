package sorts;

import entertainment.Video;

import java.util.List;

/**
 * Interface for sorting by recommendations' requirements
 */
public interface SortRecommendation {
    /**
     * Method that sorts videos by recommendations' requirements.
     */
    void sort(List<Video> videos);
}
