/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV
    Authors: Bruno Lowagie, Paulo Soares, et al.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.io.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A RandomAccessSource that is based on an underlying {@link java.nio.ByteBuffer}.  This class takes steps to ensure that the byte buffer
 * is completely freed from memory during {@link ByteBufferRandomAccessSource#close()}
 */
class ByteBufferRandomAccessSource implements IRandomAccessSource, Serializable {

    private static final long serialVersionUID = -1477190062876186034L;
    /**
     * Internal cache of memory mapped buffers
     */
    private transient java.nio.ByteBuffer byteBuffer;
    private byte[] bufferMirror;

    /**
     * Constructs a new {@link ByteBufferRandomAccessSource} based on the specified ByteBuffer
     * @param byteBuffer the buffer to use as the backing store
     */
    public ByteBufferRandomAccessSource(java.nio.ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note: Because ByteBuffers don't support long indexing, the position must be a valid positive int
     * @param position the position to read the byte from - must be less than Integer.MAX_VALUE
     */
    public int get(long position) throws java.io.IOException {
        if (position > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");
        try {

            if (position >= byteBuffer.limit())
                return -1;
            byte b = byteBuffer.get((int)position);
            return b & 0xff;
        } catch (BufferUnderflowException e) {
            return -1; // EOF
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Note: Because ByteBuffers don't support long indexing, the position must be a valid positive int
     * @param position the position to read the byte from - must be less than Integer.MAX_VALUE
     */
    public int get(long position, byte[] bytes, int off, int len) throws java.io.IOException {
        if (position > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Position must be less than Integer.MAX_VALUE");

        if (position >= byteBuffer.limit())
            return -1;

        // Not thread safe!
        byteBuffer.position((int)position);
        int bytesFromThisBuffer = Math.min(len, byteBuffer.remaining());
        byteBuffer.get(bytes, off, bytesFromThisBuffer);

        return bytesFromThisBuffer;
    }


    /**
     * {@inheritDoc}
     */
    public long length() {
        return byteBuffer.limit();
    }

    /**
     * @see java.io.RandomAccessFile#close()
     * Cleans the mapped bytebuffers and closes the channel
     */
    public void close() throws java.io.IOException {
        clean(byteBuffer);
    }

    /**
     * invokes the clean method on the ByteBuffer's cleaner
     * @param buffer ByteBuffer
     * @return boolean true on success
     */
    private static boolean clean(final java.nio.ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect())
            return false;

        Boolean b = AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                Boolean success = Boolean.FALSE;
                try {
                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", (Class<?>[]) null);
                    getCleanerMethod.setAccessible(true);
                    Object cleaner = getCleanerMethod.invoke(buffer, (Object[]) null);
                    Method clean = cleaner.getClass().getMethod("clean", (Class<?>[]) null);
                    clean.invoke(cleaner, (Object[]) null);
                    success = Boolean.TRUE;
                } catch (Exception e) {
                    // This really is a show stopper on windows
                    Logger logger = LoggerFactory.getLogger(ByteBufferRandomAccessSource.class);
                    logger.debug(e.getMessage());
                }
                return success;
            }
        });

        return b;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (byteBuffer != null && byteBuffer.hasArray()) {
            throw new NotSerializableException(byteBuffer.getClass().toString());
        } else if (byteBuffer != null) {
            bufferMirror = byteBuffer.array();
        }
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        if (bufferMirror != null) {
            byteBuffer = java.nio.ByteBuffer.wrap(bufferMirror);
            bufferMirror = null;
        }
    }
}
