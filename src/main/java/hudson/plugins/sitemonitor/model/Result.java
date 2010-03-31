package hudson.plugins.sitemonitor.model;

/**
 * This class keeps the details of a site monitoring result.
 * @author cliffano
 */
public class Result {

    /**
     * The monitored site.
     */
    private Site mSite;

    /**
     * The response code returned from the site.
     */
    private Integer mResponseCode;

    /**
     * Status of the monitored site. It can't be derived from the response code
     * because the success response code global config can differ between each
     * build.
     */
    private Status mStatus;

    /**
     * Additional textual information for the site monitoring result, e.g.
     * exception message when the site monitoring gives an unexpected exception.
     */
    private String mNote;

    /**
     * Constructs a {@link Result}.
     * @param site the monitored site
     * @param responseCode the response code returned from the site
     * @param status status of the monitored site
     * @param note additional textual information of the result
     */
    public Result(final Site site, final Integer responseCode,
            final Status status, final String note) {
        mSite = site;
        mResponseCode = responseCode;
        mStatus = status;
        mNote = note;
    }

    /**
     * @return the monitored site
     */
    public final Site getSite() {
        return mSite;
    }

    /**
     * @return the response code
     */
    public final Integer getResponseCode() {
        return mResponseCode;
    }

    /**
     * @return the status of the monitored site
     */
    public final Status getStatus() {
        return mStatus;
    }

    /**
     * @return the note
     */
    public final String getNote() {
        return mNote;
    }
}
