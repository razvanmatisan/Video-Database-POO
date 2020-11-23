package sorts;

import entertainment.Video;

import java.util.List;

public final class SortVideoByRating implements SortVideo {

    @Override
    public void sort(final String sortType, final List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getRatingVideo().equals(v2.getRatingVideo())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getRatingVideo().compareTo(v2.getRatingVideo());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getRatingVideo().equals(v1.getRatingVideo())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getRatingVideo().compareTo(v1.getRatingVideo());
            });
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
