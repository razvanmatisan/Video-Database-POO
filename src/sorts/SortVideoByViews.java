package sorts;

import entertainment.Video;

import java.util.List;

public final class SortVideoByViews implements SortVideo {
    @Override
    public void sort(final String sortType, final List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getNumberViews().equals(v2.getNumberViews())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getNumberViews().compareTo(v2.getNumberViews());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getNumberViews().equals(v1.getNumberViews())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getNumberViews().compareTo(v1.getNumberViews());
            });
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
