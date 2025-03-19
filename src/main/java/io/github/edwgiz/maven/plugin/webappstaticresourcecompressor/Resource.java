package io.github.edwgiz.maven.plugin.webappstaticresourcecompressor;

import org.apache.maven.model.PatternSet;

/**
 * The configuration element representing resources in output target directory.
 */
public class Resource extends PatternSet {

    /**
     * Default constructor.
     */
    public Resource() {
    }

    /**
     * A relative path in the output target directory.
     */
    private String subDirectory;

    /**
     * Getter for {@link #subDirectory}
     *
     * @return {@link #subDirectory}
     */
    public String getSubDirectory() {
        return subDirectory;
    }

    /**
     * Setter for {@link #subDirectory} for autowiring
     *
     * @param subDirectory a relative path in the output target directory.
     */
    @SuppressWarnings("unused")
    public void setSubDirectory(String subDirectory) {
        this.subDirectory = subDirectory;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "Resource {subDirectory: " + this.getSubDirectory() + ", " + super.toString() + "}";
    }
}
