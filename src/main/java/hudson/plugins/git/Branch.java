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


import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;

public class Branch extends GitObject {
    private static final long serialVersionUID = 1L;

    public Branch(String name, ObjectId sha1) {
        super(name, sha1);
        // TODO Auto-generated constructor stub
    }

    public Branch(Ref candidate) {
        super(strip(candidate.getName()), candidate.getObjectId());
    }

    private static String strip(String name) {
        return name.substring(name.indexOf('/', 5) + 1);
    }

    public
    @Override
    String toString() {
        return "Branch " + name + "(" + sha1 + ")";
    }

}
