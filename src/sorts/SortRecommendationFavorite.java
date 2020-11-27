package sorts;

import entertainment.Video;

import java.util.List;

/**
 * Class that implements the operation of desc sorting videos by number of favorites.
 * Second criteria is their appearance in database.
 */
public final class SortRecommendationFavorite implements SortRecommendation {
    @Override
    public void sort(final List<Video> videos) {
        videos.sort((v1, v2) -> {
            if (v2.getNumberOfFavorites().equals(v1.getNumberOfFavorites())) {
                return v1.getIndexInDatabase().compareTo(v2.getIndexInDatabase());
            }
            return v2.getNumberOfFavorites().compareTo(v1.getNumberOfFavorites());
        });
    }
}
