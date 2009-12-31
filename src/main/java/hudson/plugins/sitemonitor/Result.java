package hudson.plugins.sitemonitor;

public class Result {

    private Site mSite;
    private int mResponseCode;
    private String mNote;
    
    public Result(final Site site, final int responseCode, final String note) {
        mSite = site;
        mResponseCode = responseCode;
        mNote = note;
    }
    
    public final Site getSite() {
        return mSite;
    }
    
    public final int getResponseCode() {
        return mResponseCode;
    }
    
    public final String getNote() {
        return mNote;
    }
}
