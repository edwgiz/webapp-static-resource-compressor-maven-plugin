package io.github.edwgiz.maven.plugin.webappstaticresourcecompressor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import static org.apache.commons.io.IOUtils.copyLarge;

/**
 * Implementation for gzip compression.
 */
public class GzipCompressor extends AbstractCompressor {

    /**
     * Default constructor.
     */
    protected GzipCompressor() {
        super("gz");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compress(final BufferedInputStream srcContent, final BufferedOutputStream dstContent) throws IOException {
        try (final GZIPOutputStream outputStream = new GZIPOutputStream(dstContent) {{
            def.setLevel(Deflater.BEST_COMPRESSION);
        }}) {
            copyLarge(srcContent, outputStream);
        }
    }
}
