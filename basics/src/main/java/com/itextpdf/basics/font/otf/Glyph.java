/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.basics.font.otf;

import com.itextpdf.basics.Utilities;

import java.util.Arrays;

public class Glyph {

    /**
     * The <i>code</i> or <i>id</i> by which this is represented in the Font File.
     */
    //TODO rename to code, due to dual notation: index in otl and code in type1
    public final int index;
    /**
     * The normalized width of this Glyph.
     */
    public final int width;
    public int[] bbox = null;
    /**
     * utf-32 representation of glyph if appears. Zer
     */
    public Integer unicode;
    /**
     * The Unicode text represented by this Glyph
     */
    public char[] chars;
    public final int XPlacement;
    public final int YPlacement;
    public final int XAdvance;
    public final int YAdvance;
    public final boolean isMark;

    public Glyph(int index, int width, Integer unicode) {
        this(index, width, unicode, null, false);
    }

    public Glyph(int index, int width, char[] chars) {
        this(index, width, codePoint(chars), chars, false);

    }

    public Glyph(int index, int width, Integer unicode, int[] bbox) {
        this(index, width, unicode, null, false);
        this.bbox = bbox;
    }

    public Glyph(int width, Integer unicode) {
        this(-1, width, unicode, unicode != null ? Utilities.convertFromUtf32(unicode) : null, false);
    }

    public Glyph(int index, int width, Integer unicode, char[] chars, boolean IsMark) {
        this.index = index;
        this.width = width;
        this.unicode = unicode;
        this.XPlacement = 0;
        this.YPlacement = 0;
        this.XAdvance = 0;
        this.YAdvance = 0;
        this.isMark = IsMark;
        if (chars == null && unicode != null && Character.isValidCodePoint(unicode)) {
            this.chars = Utilities.convertFromUtf32(unicode);
        }
    }

//    public Glyph(Glyph glyph, int XPlacement, int YPlacement, int XAdvance, int YAdvance) {
//        this.index = glyph.index;
//        this.width = glyph.width;
//        this.chars = glyph.chars;
//        this.unicode = glyph.unicode;
//        this.XPlacement = glyph.XPlacement + XPlacement;
//        this.YPlacement = glyph.YPlacement + YPlacement;
//        this.XAdvance = glyph.XAdvance + XAdvance;
//        this.YAdvance = glyph.YAdvance + YAdvance;
//        this.IsMark = glyph.IsMark;
//    }


    public Glyph(Glyph glyph) {
        this.index = glyph.index;
        this.width = glyph.width;
        this.chars = glyph.chars;
        this.unicode = glyph.unicode;
        this.XPlacement = glyph.XPlacement;
        this.YPlacement = glyph.YPlacement;
        this.XAdvance = glyph.XAdvance;
        this.YAdvance = glyph.YAdvance;
        this.isMark = glyph.isMark;
    }

    public Glyph(Glyph glyph, Integer unicode) {
        this.index = glyph.index;
        this.width = glyph.width;
        this.chars = unicode != null ? Utilities.convertFromUtf32(unicode) : null;
        this.unicode = unicode;
        this.XPlacement = glyph.XPlacement;
        this.YPlacement = glyph.YPlacement;
        this.XAdvance = glyph.XAdvance;
        this.YAdvance = glyph.YAdvance;
        this.isMark = glyph.isMark;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((chars == null) ? 0 : Arrays.hashCode(chars));
        result = prime * result + index;
        result = prime * result + width;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Glyph other = (Glyph) obj;
        if (chars == null) {
            if (other.chars != null) {
                return false;
            }
        } else if (!Arrays.equals(chars, other.chars)) {
            return false;
        }
        return index == other.index && width == other.width;
    }

    public void setUnicode(Integer unicode) {
        this.unicode = unicode;
        this.chars = unicode != null ? Utilities.convertFromUtf32(unicode) : null;
    }

    static Integer codePoint(char[] a) {
        if (a != null) {
            if (a.length == 1 && Character.isISOControl(a[0])) {
                return (int) a[0];
            } else if (a.length == 2 && Character.isHighSurrogate(a[0]) && Character.isLowSurrogate(a[1])) {
                return Character.toCodePoint(a[0], a[1]);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s [uni=%d, id=%d, width=%d, chars=%s]", Glyph.class.getSimpleName(),
                unicode, index, width, chars != null ? Arrays.toString(chars) : "null");
    }
}
