package hudson.plugins.sitemonitor.model;

/**
 * Status of a monitored site.
 * @author cliffano
 */
public enum Status {

    /**
     * Site is up and there's no error.
     */
    UP,
    /**
     * Site is considered to have an error when it gives a response code that is
     * not configured in Site Monitor global config.
     */
    ERROR,
    /**
     * Site is down when host is unknown or a connection timeout is detected.
     */
    DOWN,
    /**
     * Site connection throws unexpected exception.
     */
    EXCEPTION;
}
