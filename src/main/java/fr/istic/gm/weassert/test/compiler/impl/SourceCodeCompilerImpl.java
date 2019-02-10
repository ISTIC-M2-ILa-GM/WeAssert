package fr.istic.gm.weassert.test.compiler.impl;

import fr.istic.gm.weassert.test.compiler.SourceCodeCompiler;
import fr.istic.gm.weassert.test.exception.WeAssertException;
import fr.istic.gm.weassert.test.utils.BackupUtils;
import fr.istic.gm.weassert.test.utils.ProcessBuilderFactory;
import fr.istic.gm.weassert.test.utils.ProcessBuilderWrapper;
import fr.istic.gm.weassert.test.utils.UrlClassLoaderWrapper;
import fr.istic.gm.weassert.test.utils.impl.BackupUtilsImpl;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Paths;

@Slf4j
public class SourceCodeCompilerImpl implements SourceCodeCompiler {

    private static final String POM_FILE = "/pom.xml";
    private static final String ERROR_NO_POM = "SourceCodeCompilerImpl: No pom file on the maven project %s";

    private String mavenProjectPath;

    private ProcessBuilderWrapper processBuilder;

    private UrlClassLoaderWrapper urlClassLoaderWrapper;

    public SourceCodeCompilerImpl(String mavenProjectPath, String mavenCommand, ProcessBuilderFactory processBuilderFactory, UrlClassLoaderWrapper urlClassLoaderWrapper) {
        this.mavenProjectPath = mavenProjectPath + POM_FILE;
        this.urlClassLoaderWrapper = urlClassLoaderWrapper;
        if (!Paths.get(mavenProjectPath).toFile().exists()) {
            throw new WeAssertException(String.format(ERROR_NO_POM, this.mavenProjectPath));
        }
        processBuilder = processBuilderFactory.create(mavenProjectPath, mavenCommand, "clean", "test", "-DskipTests");
    }

    @Override
    public void compileAndWait() {
        try {
            BackupUtils backupUtils = insertDeps();
            Process start = processBuilder.start();
            BufferedReader buff = new BufferedReader(new InputStreamReader(start.getInputStream()));
            while (start.isAlive()) {
                String line;
                while ((line = buff.readLine()) != null) {
                    log.info(line);
                }
            }
            urlClassLoaderWrapper.refresh();
            backupUtils.restore();
        } catch (Exception e) {
            throw new WeAssertException("SourceCodeCompilerImpl: Compile error", e);
        }
    }

    private BackupUtils insertDeps() {
        try {
            BackupUtils backupUtils = new BackupUtilsImpl(mavenProjectPath);
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setValidating(false);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(mavenProjectPath));
            NodeList nodes = doc.getElementsByTagName("dependencies");
            Element dependency = doc.createElement("dependency");
            Element groupId = doc.createElement("groupId");
            Element artifactId = doc.createElement("artifactId");
            Element version = doc.createElement("version");
            Text groupIdText = doc.createTextNode("fr.istic.gm");
            Text artifactIdText = doc.createTextNode("we-assert");
            Text versionText = doc.createTextNode("1.0-SNAPSHOT");
            groupId.appendChild(groupIdText);
            artifactId.appendChild(artifactIdText);
            version.appendChild(versionText);
            dependency.appendChild(groupId);
            dependency.appendChild(artifactId);
            dependency.appendChild(version);
            nodes.item(0).insertBefore(dependency, nodes.item(0).getChildNodes().item(0).getNextSibling());
            TransformerFactory factory = TransformerFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(mavenProjectPath));
            transformer.transform(domSource, streamResult);
            return backupUtils;
        } catch (Exception e) {
            throw new WeAssertException("Can't insert deps", e);
        }
    }
}
