package sorts;

import entertainment.Video;

import java.util.List;

public final class SortVideoByDuration implements SortVideo {
    @Override
    public void sort(final String sortType, final List<Video> videos) {
        switch (sortType) {
            case "asc" -> videos.sort((v1, v2) -> {
                if (v1.getDuration().equals(v2.getDuration())) {
                    return v1.getTitle().compareTo(v2.getTitle());
                }
                return v1.getDuration().compareTo(v2.getDuration());
            });
            case "desc" -> videos.sort((v1, v2) -> {
                if (v2.getDuration().equals(v1.getDuration())) {
                    return v2.getTitle().compareTo(v1.getTitle());
                }
                return v2.getDuration().compareTo(v1.getDuration());
            });
            default -> throw new IllegalStateException("Unexpected value: " + sortType);
        }
    }
}
