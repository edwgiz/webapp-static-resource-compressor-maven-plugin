package io.github.edwgiz.maven.plugin.webappstaticresourcecompressor;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_STRING_ARRAY;

/**
 * Goal to compress resources in the output directory.
 */
@Mojo(name = "add-compressed-resources", defaultPhase = LifecyclePhase.PROCESS_SOURCES, threadSafe = true)
public class AddCompressedResourcesMojo extends AbstractMojo {

    private static final Logger log = LoggerFactory.getLogger(AddCompressedResourcesMojo.class);

    /**
     * Default constructor.
     */
    public AddCompressedResourcesMojo() {
    }

    /**
     * The output directory where to augment the resources.
     *
     * @since 1.0.0
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", required = true)
    private File outputDirectory;

    /**
     * The list of resources we want to augment with compressed variants.
     *
     * @since 1.0.0
     */
    @Parameter(required = true)
    private List<Resource> resources;

    /**
     * Autowired {@link MavenProject} instance
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    /**
     * You can skip the execution of the plugin if you need to. Its use is NOT RECOMMENDED, but quite convenient on
     * occasion.
     *
     * @since 1.0.0
     */
    @Parameter(property = "maven.resources.skip", defaultValue = "false")
    private boolean skip;

    /**
     * Getter for {@link #resources}.
     *
     * @return {@link #resources}
     */
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * Setter for {@link #resources}.
     *
     * @param resources set {@link #resources}
     */
    @SuppressWarnings("unused")
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    /**
     * Getter for {@link #outputDirectory}.
     *
     * @return {@link #outputDirectory}
     */
    public File getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Setter for {@link #outputDirectory}.
     *
     * @param outputDirectory the output folder.
     */
    @SuppressWarnings("unused")
    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * Getter for {@link #skip}.
     *
     * @return {@link #skip}
     */
    public boolean isSkip() {
        return skip;
    }

    /**
     * Setter for {@link #skip}.
     *
     * @param skip flag
     */
    @SuppressWarnings("unused")
    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    /**
     * {@inheritDoc}
     */
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            getLog().info("Skipping the execution.");
            return;
        }

        final AbstractCompressor[] compressors = createCompressors();
        for (final Resource resource : getResources()) {
            compress(resource, compressors);
        }
        log.info("Resources compressed");
    }

    private AbstractCompressor[] createCompressors() {
        return new AbstractCompressor[]{new GzipCompressor(), new BrotliCompressor()};
    }

    void compress(Resource resource, AbstractCompressor[] compressors) throws MojoExecutionException {
        final String subDirectory = resource.getSubDirectory();
        final Path dstDir = getOutputDirectory().toPath().resolve(subDirectory);
        final DirectoryScanner scanner = createScanner(resource, dstDir);
        for (final String includedFile : scanner.getIncludedFiles()) {
            final Path srcFile = dstDir.resolve(includedFile);
            for (final AbstractCompressor compressor : compressors) {
                compressor.compress(srcFile);
            }
        }
    }

    private DirectoryScanner createScanner(Resource resource, Path dstDir) {
        final DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(dstDir.toFile());
        scanner.setIncludes(resource.getIncludes().toArray(EMPTY_STRING_ARRAY));
        scanner.setExcludes(resource.getExcludes().toArray(new String[0]));
        scanner.scan();
        return scanner;
    }
}
