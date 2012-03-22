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
 * Nikita Levyankov
 *
 *******************************************************************************/
package hudson.plugins.git.converter;

import hudson.XmlFile;
import hudson.model.Run;
import java.io.IOException;
import java.net.URISyntaxException;
import hudson.plugins.git.GitTool;
import org.junit.Test;

/**
 * GitToolConverterTest
 * <p/>
 * Date: 7/18/11
 *
 * @author Nikita Levyankov
 */
public class GitToolConverterTest extends BaseLegacyConverterTest {

    @Override
    public void setUp() throws URISyntaxException, IOException {
        super.setUp();
    }

    @Override
    protected String getResourceName() {
        return "hudson.plugins.git.GitTool.xml";
    }

    @Test
    public void testLegacyUnmarshall() throws Exception {
        getSourceConfigFile(XmlFile.DEFAULT_XSTREAM).read();
    }
}
