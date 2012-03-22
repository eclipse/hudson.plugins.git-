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

import com.thoughtworks.xstream.XStream;
import hudson.XmlFile;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.util.XStream2;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.ObjectId;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for ObjectID converter with legacy and current ObjectID support
 * <p/>
 * Date: 7/4/11
 *
 * @author Nikita Levyankov
 */
public class ObjectIdConverterTest {
    private File sourceConfigFile;
    private File targetConfigFile;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        sourceConfigFile = new File(this.getClass().getResource("build.xml").toURI());
        //Create target config file in order to perform marshall operation
        targetConfigFile = new File(sourceConfigFile.getParent(), "target_build.xml");
        FileUtils.copyFile(sourceConfigFile, targetConfigFile);
    }

    @Test(expected = Exception.class)
    public void testFailedUnmarshall() throws Exception {
        XStream XSTREAM = initXStream();
        //Config contains legacy ObjectId class. Should fail without custom converter
        getSourceConfigFile(XSTREAM).read();
    }

    @Test
    public void testLegacyUnmarshall() throws Exception {
        XStream XSTREAM = initXStream();
        XSTREAM.registerConverter(new ObjectIdConverter());
        getSourceConfigFile(XSTREAM).read();
    }

    @Test
    public void testMarshall() throws Exception {
        XStream XSTREAM = initXStream();
        XSTREAM.registerConverter(new ObjectIdConverter());
        //read object from config
        Object item = getSourceConfigFile(XSTREAM).read();
        //save to new config file
        getTargetConfigFile(XSTREAM).write(item);
        getTargetConfigFile(XSTREAM).read();
    }

    private XStream initXStream() {
        XStream XSTREAM = new XStream2();
        XSTREAM.alias("project", FreeStyleProject.class);
        XSTREAM.alias("build", FreeStyleBuild.class);
        return XSTREAM;
    }


    private XmlFile getSourceConfigFile(XStream XSTREAM) {
        return new XmlFile(XSTREAM, sourceConfigFile);
    }

    private XmlFile getTargetConfigFile(XStream XSTREAM) {
        return new XmlFile(XSTREAM, targetConfigFile);
    }
}
