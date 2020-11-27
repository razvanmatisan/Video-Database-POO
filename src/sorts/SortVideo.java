package sorts;

import entertainment.Video;

import java.util.List;

/**
 * Interface for sorting videos.
 */
public interface SortVideo {
    /**
     * Method that sorts videos depending on sortType.
     */
    void sort(String sortType, List<Video> videos);
}
