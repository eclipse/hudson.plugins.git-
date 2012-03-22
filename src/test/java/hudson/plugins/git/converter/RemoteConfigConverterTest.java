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
import org.eclipse.jgit.transport.RemoteConfig;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for RemoteConfig converter with legacy and current RemoteConfig support
 * <p/>
 * Date: 7/4/11
 *
 * @author Nikita Levyankov
 */
public class RemoteConfigConverterTest {

    private File sourceConfigFile;
    private File targetConfigFile;

    @Before
    public void setUp() throws URISyntaxException, IOException {
        sourceConfigFile = new File(this.getClass().getResource("config.xml").toURI());
        //Create target config file in order to perform marshall operation
        targetConfigFile = new File(sourceConfigFile.getParent(), "target_config.xml");
        FileUtils.copyFile(sourceConfigFile, targetConfigFile);
    }

    @Test
    public void testLegacyUnmarshall() throws Exception {
        XStream XSTREAM = initXStream();
        //Config contains legacy RemoteConfig class. Register custom converter and alias
        XSTREAM.alias("RemoteConfig", RemoteConfig.class);
        XSTREAM.alias("RemoteConfig", org.spearce.jgit.transport.RemoteConfig.class);
        XSTREAM.registerConverter(new RemoteConfigConverter(XSTREAM.getMapper(), XSTREAM.getReflectionProvider()));
        getSourceConfigFile(XSTREAM).read();
    }

    @Test
    public void testMarshall() throws Exception {
        XStream XSTREAM = initXStream();
        XSTREAM.alias("RemoteConfig", RemoteConfig.class);
        XSTREAM.alias("RemoteConfig", org.spearce.jgit.transport.RemoteConfig.class);
        XSTREAM.registerConverter(new RemoteConfigConverter(XSTREAM.getMapper(), XSTREAM.getReflectionProvider()));
        //read object from config
        Object item = getSourceConfigFile(XSTREAM).read();
        //save to new config file
        getTargetConfigFile(XSTREAM).write(item);
        getTargetConfigFile(XSTREAM).read();
    }

    private XmlFile getSourceConfigFile(XStream XSTREAM) {
        return new XmlFile(XSTREAM, sourceConfigFile);
    }
    private XmlFile getTargetConfigFile(XStream XSTREAM) {
        return new XmlFile(XSTREAM, targetConfigFile);
    }

    private XStream initXStream() {
        XStream XSTREAM = new XStream2();
        XSTREAM.alias("project", FreeStyleProject.class);
        XSTREAM.alias("build", FreeStyleBuild.class);
        return XSTREAM;
    }
}
