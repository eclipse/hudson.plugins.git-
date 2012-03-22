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
 * Andrew Bayer, Anton Kozak, Nikita Levyankov
 *
 *******************************************************************************/

package hudson.plugins.git;

import hudson.plugins.git.BranchSpec;
import junit.framework.Assert;
import org.junit.Test;

public class TestBranchSpec{
    @Test
    public void testMatch() {
        BranchSpec l = new BranchSpec("master");
        Assert.assertTrue(l.matches("origin/master"));
        Assert.assertFalse(l.matches("origin/something/master"));
        Assert.assertFalse(l.matches("master"));
        Assert.assertFalse(l.matches("dev"));
        
        
        BranchSpec est = new BranchSpec("origin/*/dev");
        
        Assert.assertFalse(est.matches("origintestdev"));
        Assert.assertTrue(est.matches("origin/test/dev"));
        Assert.assertFalse(est.matches("origin/test/release"));
        Assert.assertFalse(est.matches("origin/test/somthing/release"));
        
        BranchSpec s = new BranchSpec("origin/*");
        
        Assert.assertTrue(s.matches("origin/master"));
      
        BranchSpec m = new BranchSpec("**/magnayn/*");
        
        Assert.assertTrue(m.matches("origin/magnayn/b1"));
        Assert.assertTrue(m.matches("remote/origin/magnayn/b1"));
      
        BranchSpec n = new BranchSpec("*/my.branch/*");
        
        Assert.assertTrue(n.matches("origin/my.branch/b1"));
        Assert.assertFalse(n.matches("origin/my-branch/b1"));
        Assert.assertFalse(n.matches("remote/origin/my.branch/b1"));
      
        BranchSpec o = new BranchSpec("**");
        
        Assert.assertTrue(o.matches("origin/my.branch/b1"));
        Assert.assertTrue(o.matches("origin/my-branch/b1"));
        Assert.assertTrue(o.matches("remote/origin/my.branch/b1"));
      
        BranchSpec p = new BranchSpec("*");

        Assert.assertTrue(p.matches("origin/x"));
        Assert.assertFalse(p.matches("origin/my-branch/b1"));
    }

    @Test
    public void testEmptyBranch() {
        BranchSpec l = new BranchSpec("");
        Assert.assertTrue(l.matches("origin/master"));
    }

}
