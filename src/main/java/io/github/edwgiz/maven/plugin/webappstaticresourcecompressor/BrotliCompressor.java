package io.github.edwgiz.maven.plugin.webappstaticresourcecompressor;

import com.aayushatharva.brotli4j.Brotli4jLoader;
import com.aayushatharva.brotli4j.encoder.BrotliOutputStream;
import com.aayushatharva.brotli4j.encoder.Encoder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.commons.io.IOUtils.copyLarge;

/**
 * Implementation for <a href="https://en.wikipedia.org/wiki/Brotli">Brotli</a> compression.
 */
public class BrotliCompressor extends AbstractCompressor {

    static {
        Brotli4jLoader.ensureAvailability();
    }

    private final Encoder.Parameters params;

    /**
     * Default constructor.
     */
    protected BrotliCompressor() {
        super("br");
        Encoder.Parameters params = new Encoder.Parameters();
        params.setQuality(11);
        params.setMode(Encoder.Mode.TEXT);
        this.params = params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compress(final BufferedInputStream srcContent, final BufferedOutputStream dstContent) throws IOException {
        try (final OutputStream outputStream = new BrotliOutputStream(dstContent, params)) {
            copyLarge(srcContent, outputStream);
        }
    }
}
