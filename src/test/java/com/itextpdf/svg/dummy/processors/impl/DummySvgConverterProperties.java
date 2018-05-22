/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2018 iText Group NV
    Authors: iText Software.

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
package com.itextpdf.svg.dummy.processors.impl;

import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.styledxmlparser.css.ICssResolver;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.resolver.resource.ResourceResolver;
import com.itextpdf.svg.dummy.css.impl.DummyCssResolver;
import com.itextpdf.svg.dummy.factories.DummySvgNodeFactory;
import com.itextpdf.svg.processors.ISvgConverterProperties;
import com.itextpdf.svg.renderers.factories.ISvgNodeRendererFactory;

public class DummySvgConverterProperties implements ISvgConverterProperties {
    ICssResolver cssResolver;
    ISvgNodeRendererFactory rendererFactory;
    String baseUri;
    MediaDeviceDescription mediaDeviceDescription;
    ResourceResolver resourceResolver;

    public DummySvgConverterProperties(){
        cssResolver = new DummyCssResolver();
        rendererFactory = new DummySvgNodeFactory();
        mediaDeviceDescription = new MediaDeviceDescription( "" );
        baseUri = "";
        resourceResolver = new ResourceResolver("");
    }

    @Override
    public ICssResolver getCssResolver() {
        return cssResolver;
    }

    @Override
    public ISvgNodeRendererFactory getRendererFactory() {
        return rendererFactory;
    }

    @Override
    public FontProvider getFontProvider() {
        return null;
    }

    @Override
    public String getCharset() {
        return null;
    }

    @Override
    public String getBaseUri() {
        return baseUri;
    }

    @Override
    public ISvgConverterProperties setBaseUri(String baseUri) {
        return this;
    }

    @Override
    public MediaDeviceDescription getMediaDeviceDescription() {
        return mediaDeviceDescription;
    }

    @Override
    public ISvgConverterProperties setMediaDeviceDescription(MediaDeviceDescription mediaDeviceDescription) {
        return this;
    }

    @Override
    public ResourceResolver getResourceResolver() {
        return this.resourceResolver;
    }

    @Override
    public ISvgConverterProperties setFontProvider(FontProvider fontProvider) {
        return this;
    }
}
