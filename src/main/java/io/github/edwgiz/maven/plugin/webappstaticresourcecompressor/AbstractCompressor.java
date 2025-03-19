package io.github.edwgiz.maven.plugin.webappstaticresourcecompressor;

import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static java.nio.file.Files.*;
import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * Base class for compressor implementations.
 */
public abstract class AbstractCompressor {

    private static final Logger log = LoggerFactory.getLogger(AbstractCompressor.class);

    private final String extension;

    /**
     * Constructor to use in child classes.
     *
     * @param extension compression-specific file extension to be appended to original file-name.
     */
    protected AbstractCompressor(String extension) {
        this.extension = extension;
    }

    private Path toDstFile(Path srcFile) {
        final String dstFileName = srcFile.getFileName() + "." + extension;
        return srcFile.getParent().resolve(dstFileName);
    }

    /**
     * Augmented the given file with a compressed version,
     *
     * @param srcFile original file path
     * @throws MojoExecutionException thrown by read, write or compression failures
     */
    protected void compress(Path srcFile) throws MojoExecutionException {
        log.debug("Start compressing {}", srcFile);
        InputStream srcContent = null;
        OutputStream dstContent = null;
        try {
            try {
                srcContent = newInputStream(srcFile);
            } catch (IOException e) {
                throw new MojoExecutionException("Can't read " + srcFile, e);
            }
            final Path dstFile = toDstFile(srcFile);
            try {
                dstContent = newOutputStream(dstFile, StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new MojoExecutionException("Can't write " + dstFile, e);
            }
            try(final BufferedOutputStream bufferedDstContent = new BufferedOutputStream(dstContent)) {
                compress(new BufferedInputStream(srcContent), bufferedDstContent);
            } catch (IOException e) {
                try {
                    deleteIfExists(dstFile);
                } catch (IOException e2) {
                    e.addSuppressed(e2);
                }
                throw new MojoExecutionException("Can't compress " + srcFile, e);
            }
        } finally {
            closeQuietly(srcContent, dstContent);
        }
    }

    /**
     * Compresses the given content.
     *
     * @param srcContent original content
     * @param dstContent target content
     * @throws IOException thrown by I/O failures
     */
    protected abstract void compress(BufferedInputStream srcContent, BufferedOutputStream dstContent) throws IOException;
}
