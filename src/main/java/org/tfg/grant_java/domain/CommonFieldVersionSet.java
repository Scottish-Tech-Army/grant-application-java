package org.tfg.grant_java.domain;

import java.util.ArrayList;
import java.util.List;

public class CommonFieldVersionSet {
    private int currentVersion;
    private List<CommonFieldVersion> versions = new ArrayList<>();

    public CommonFieldVersionSet() {}

    public int getCurrentVersion() { return currentVersion; }
    public void setCurrentVersion(int currentVersion) { this.currentVersion = currentVersion; }

    public List<CommonFieldVersion> getVersions() { return versions; }
    public void setVersions(List<CommonFieldVersion> versions) { this.versions = versions; }
}

