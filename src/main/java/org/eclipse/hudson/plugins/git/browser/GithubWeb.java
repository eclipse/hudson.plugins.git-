/*******************************************************************************
 *
 * Copyright (c) 2011 Oracle Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *
 * Mirko Friedenhagen, Andrew Bayer, Anton Kozak, Nikita Levyankov
 *
 *******************************************************************************/

package org.eclipse.hudson.plugins.git.browser;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.scm.EditType;
import hudson.scm.RepositoryBrowser;
import hudson.util.FormValidation;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import net.sf.json.JSONObject;
import org.eclipse.hudson.plugins.git.GitChangeSet;
import org.eclipse.hudson.plugins.git.GitChangeSet.Path;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Git Browser URLs
 */
public class GithubWeb extends GitRepositoryBrowser {

    private static final long serialVersionUID = 1L;
    private final URL url;

    @DataBoundConstructor
    public GithubWeb(String url) throws MalformedURLException {
        this.url = normalizeToEndWithSlash(new URL(url));
    }

    public URL getUrl() {
        return url;
    }

    @Override
    public URL getChangeSetLink(GitChangeSet changeSet) throws IOException {
        return new URL(url, url.getPath() + "commit/" + changeSet.getId().toString());
    }

    /**
     * Creates a link to the file diff.
     * http://[GitHib URL]/commit/573670a3bb1f3b939e87f1dee3e99b6bfe281fcb#diff-N
     *
     * @param path affected file path
     * @return diff link
     * @throws IOException
     */
    @Override
    public URL getDiffLink(Path path) throws IOException {
        if (path.getEditType() != EditType.EDIT || path.getSrc() == null || path.getDst() == null
            || path.getChangeSet().getParentCommit() == null) {
            return null;
        }
        return getDiffLinkRegardlessOfEditType(path);
    }

    /**
     * Return a diff link regardless of the edit type by appending the index of the pathname in the changeset.
     *
     * @param path
     * @return
     * @throws IOException
     */
    private URL getDiffLinkRegardlessOfEditType(Path path) throws IOException {
        final GitChangeSet changeSet = path.getChangeSet();
        final ArrayList<String> affectedPaths = new ArrayList<String>(changeSet.getAffectedPaths());
        // Github seems to sort the output alphabetically by the path.
        Collections.sort(affectedPaths);
        final String pathAsString = path.getPath();
        final int i = Collections.binarySearch(affectedPaths, pathAsString);
        assert i >= 0;
        return new URL(getChangeSetLink(changeSet), "#diff-" + String.valueOf(i));
    }

    /**
     * Creates a link to the file.
     * http://[GitHib URL]/blob/573670a3bb1f3b939e87f1dee3e99b6bfe281fcb/src/main/java/hudson/plugins/git/browser/GithubWeb.java
     * Github seems to have no URL for deleted files, so just return
     * a difflink instead.
     *
     * @param path file
     * @return file link
     * @throws IOException
     */
    @Override
    public URL getFileLink(Path path) throws IOException {
        if (path.getEditType().equals(EditType.DELETE)) {
            return getDiffLinkRegardlessOfEditType(path);
        } else {
            final String spec = "blob/" + path.getChangeSet().getId() + "/" + path.getPath();
            return new URL(url, url.getPath() + spec);
        }
    }

    @Extension
    public static class GithubWebDescriptor extends Descriptor<RepositoryBrowser<?>> {
        public String getDisplayName() {
            return "githubweb";
        }

        @Override
        public GithubWeb newInstance(StaplerRequest req, JSONObject jsonObject) throws FormException {
            return req.bindParameters(GithubWeb.class, "githubweb.");
        }

        public FormValidation doCheckUrl(@QueryParameter(fixEmpty = true) final String url)
            throws IOException, ServletException {
            return new GitUrlChecker(url, "github").check();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GithubWeb githubWeb = (GithubWeb) o;

        if (url != null ? !url.equals(githubWeb.url) : githubWeb.url != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
