package io.github.edwgiz.maven.plugin.webappstaticresourcecompressor;


import org.apache.commons.io.file.PathUtils;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.*;

public class AddCompressedResourcesMojoTest {
    @Rule
    public MojoRule rule = new MojoRule();

    /**
     * @throws Exception if any
     */
    @Test
    public void testSomething()
            throws Exception {
        // Given
        final String staticResourceSubDir = "webapp/static";
        final File pom = new File("target/test-classes/project-to-test/");
        final Path srcStaticResourceDir = pom.toPath().resolve("src/main/resources/" + staticResourceSubDir);
        final Path dstStaticResourceDir = createDirectories(pom.toPath().resolve("target/classes/" + staticResourceSubDir));
        PathUtils.copyDirectory(srcStaticResourceDir, dstStaticResourceDir, REPLACE_EXISTING);
        final AddCompressedResourcesMojo myMojo = (AddCompressedResourcesMojo) rule.lookupConfiguredMojo(pom, "add-compressed-resources");

        // When
        myMojo.execute();

        // Then
        final File outputDirectory = (File) rule.getVariableValueFromObject(myMojo, "outputDirectory");
        assertNotNull(outputDirectory);
        assertTrue(outputDirectory.exists());
        final Path webappStaticDir = outputDirectory.toPath().resolve(staticResourceSubDir);
        assertTrue(Files.exists(webappStaticDir));
        Path webappStaticJsDir = webappStaticDir.resolve("js");
        try (final Stream<Path> dstFiles = Files.list(webappStaticDir)) {
            Set<Path> fileSet = dstFiles.collect(Collectors.toSet());
            assertEquals(7, fileSet.size());
            assertTrue(fileSet.contains(webappStaticDir.resolve("index.html")));
            assertTrue(fileSet.contains(webappStaticDir.resolve("index.html.gz")));
            assertTrue(fileSet.contains(webappStaticDir.resolve("index.html.br")));
            assertTrue(fileSet.contains(webappStaticDir.resolve("main.css")));
            assertTrue(fileSet.contains(webappStaticDir.resolve("main.css.gz")));
            assertTrue(fileSet.contains(webappStaticDir.resolve("main.css.br")));
            assertTrue(fileSet.contains(webappStaticJsDir));
        }
        try (final Stream<Path> dstFiles = Files.list(webappStaticJsDir)) {
            Set<Path> fileSet = dstFiles.collect(Collectors.toSet());
            assertEquals(3, fileSet.size());
            assertTrue(fileSet.contains(webappStaticJsDir.resolve("main.js")));
            assertTrue(fileSet.contains(webappStaticJsDir.resolve("main.js.gz")));
            assertTrue(fileSet.contains(webappStaticJsDir.resolve("main.js.br")));
        }
    }

    /**
     * Do not need the MojoRule.
     */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
        assertTrue(true);
    }

}

