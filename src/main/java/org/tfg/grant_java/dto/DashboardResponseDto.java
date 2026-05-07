package org.tfg.grant_java.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DashboardResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int total;
    private int drafted;
    private int submitted;
    private List<RecentApplicationsDto> recentApplications = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void addTotal(int total) {
        this.total += total;
    }

    public int getDrafted() {
        return drafted;
    }

    public void setDrafted(int drafted) {
        this.drafted = drafted;
    }

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public List<RecentApplicationsDto> getRecentApplications() {
        return recentApplications;
    }

    public void setRecentApplications(List<RecentApplicationsDto> recentApplications) {
        this.recentApplications = recentApplications;
    }
}
