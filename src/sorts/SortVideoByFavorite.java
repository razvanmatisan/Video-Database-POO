package sorts;

import entertainment.Video;

import java.util.List;

public final class SortVideoByFavorite implements SortVideo {
    @Override
    public void sort(final String sortType, final List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getNumberOfFavorites().equals(v2.getNumberOfFavorites())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getNumberOfFavorites().compareTo(v2.getNumberOfFavorites());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getNumberOfFavorites().equals(v1.getNumberOfFavorites())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getNumberOfFavorites().compareTo(v1.getNumberOfFavorites());
            });
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
