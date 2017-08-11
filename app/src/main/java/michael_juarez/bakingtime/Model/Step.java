package michael_juarez.bakingtime.Model;

/**
 * This class holds the pattern for a step in the recipe
 */

public class Step {
    private String mId;
    private String mShortDescription;

    public Step(String id, String shortDescription, String videoUrl, String thumbnailUrl) {
        mId = id;
        mShortDescription = shortDescription;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        mShortDescription = shortDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    private String mVideoUrl;
    private String mThumbnailUrl;



}
