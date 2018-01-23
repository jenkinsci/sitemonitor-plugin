package hudson.plugins.sitemonitor;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Descriptor;
import hudson.model.TopLevelItem;
import hudson.plugins.sitemonitor.model.Result;
import hudson.plugins.view.dashboard.DashboardPortlet;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class DashboardSupport extends DashboardPortlet {
    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public String getText() {
        StringBuilder result = new StringBuilder();
        for (Result r: getResults()) {
            result.append(String.format("site=%s, code=%s, status=%s", r.getSite().getUrl(), r.getResponseCode(), r.getStatus()));
        }
        return result.toString();
    }

    public List<Result> getResults() {
        TopLevelItem item = Jenkins.getInstance().getItem(jobName);
        AbstractProject<?, ?> project = (AbstractProject<?, ?>) item;
        for (Action action: project.getLastBuild().getActions()) {
            if (action instanceof SiteMonitorRootAction) {
                return ((SiteMonitorRootAction) action).getResults();
            }
        }
        return new LinkedList<Result>();
    }

    @DataBoundConstructor
    public DashboardSupport(String name, String jobName ) {
        super(name);
        this.jobName = jobName;
    }

    public boolean showDescription() {
        return new SiteMonitorDescriptor().getShowDescription();
    }

    public static Collection<String> getAllJobNames() {
        return Jenkins.getInstance().getJobNames();
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {
        @Override
        public String getDisplayName() {
            return Messages.SiteMonitor_DisplayName();
        }
    }
}
