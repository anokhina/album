/*
 * Copyright 2017 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.org.sevn.common.mime;

//https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Complete_list_of_MIME_types
public class Mime {

    /*
.aac	AAC audio file	audio/aac
.abw	AbiWord document	application/x-abiword
.arc	Archive document (multiple files embedded)	application/octet-stream
.avi	AVI: Audio Video Interleave	video/x-msvideo
.azw	Amazon Kindle eBook format	application/vnd.amazon.ebook
.bin	Any kind of binary data	application/octet-stream
.bz	BZip archive	application/x-bzip
.bz2	BZip2 archive	application/x-bzip2
.csh	C-Shell script	application/x-csh
.css	Cascading Style Sheets (CSS)	text/css
.csv	Comma-separated values (CSV)	text/csv
.doc	Microsoft Word	application/msword
.epub	Electronic publication (EPUB)	application/epub+zip
.gif	Graphics Interchange Format (GIF)	image/gif
.htm
.html	HyperText Markup Language (HTML)	text/html
.ico	Icon format	image/x-icon
.ics	iCalendar format	text/calendar
.jar	Java Archive (JAR)	application/java-archive
.jpeg
.jpg	JPEG images	image/jpeg
.js	JavaScript (ECMAScript)	application/javascript
.json	JSON format	application/json
.mid
.midi	Musical Instrument Digital Interface (MIDI)	audio/midi
.mpeg	MPEG Video	video/mpeg
.mpkg	Apple Installer Package	application/vnd.apple.installer+xml
.odp	OpenDocuemnt presentation document	application/vnd.oasis.opendocument.presentation
.ods	OpenDocuemnt spreadsheet document	application/vnd.oasis.opendocument.spreadsheet
.odt	OpenDocument text document	application/vnd.oasis.opendocument.text
.oga	OGG audio	audio/ogg
.ogv	OGG video	video/ogg
.ogx	OGG	application/ogg
.pdf	Adobe Portable Document Format (PDF)	application/pdf
.ppt	Microsoft PowerPoint	application/vnd.ms-powerpoint
.rar	RAR archive	application/x-rar-compressed
.rtf	Rich Text Format (RTF)	application/rtf
.sh	Bourne shell script	application/x-sh
.svg	Scalable Vector Graphics (SVG)	image/svg+xml
.swf	Small web format (SWF) or Adobe Flash document	application/x-shockwave-flash
.tar	Tape Archive (TAR)	application/x-tar
.tif
.tiff	Tagged Image File Format (TIFF)	image/tiff
.ttf	TrueType Font	font/ttf
.vsd	Microsoft Visio	application/vnd.visio
.wav	Waveform Audio Format	audio/x-wav
.weba	WEBM audio	audio/webm
.webm	WEBM video	video/webm
.webp	WEBP image	image/webp
.woff	Web Open Font Format (WOFF)	font/woff
.woff2	Web Open Font Format (WOFF)	font/woff2
.xhtml	XHTML	application/xhtml+xml
.xls	Microsoft Excel	application/vnd.ms-excel
.xml	XML	application/xml
.xul	XUL	application/vnd.mozilla.xul+xml
.zip	ZIP archive	application/zip
.3gp	3GPP audio/video container	video/3gpp
audio/3gpp if it doesn't contain video
.3g2	3GPP2 audio/video container	video/3gpp2
audio/3gpp2 if it doesn't contain video
.7z	7-zip archive	application/x-7z-compressed
    
    */
    
    public static String getMimeTypeFile(String fname) {
        int extIdx = fname.lastIndexOf(".");
        if (extIdx > 0) {
            return getMimeTypeExt(fname.substring(extIdx).toLowerCase());
        }
        return null;
    }
    public static String getMimeTypeExt(String ext) {
        switch(ext) {
            case ".aac": /*  "AAC audio file"*/ return  "audio/aac";
            case ".abw": /*  "AbiWord document"*/ return  "application/x-abiword";
            case ".arc": /*  "Archive document (multiple files embedded)"*/ return  "application/octet-stream";
            case ".avi": /*  "AVI: Audio Video Interleave"*/ return  "video/x-msvideo";
            case ".azw": /*  "Amazon Kindle eBook format"*/ return  "application/vnd.amazon.ebook";
            case ".bin": /*  "Any kind of binary data"*/ return  "application/octet-stream";
            case ".bz": /*  "BZip archive"*/ return  "application/x-bzip";
            case ".bz2": /*  "BZip2 archive"*/ return  "application/x-bzip2";
            case ".csh": /*  "C-Shell script"*/ return  "application/x-csh";
            case ".css": /*  "Cascading Style Sheets (CSS)"*/ return  "text/css";
            case ".csv": /*  "Comma-separated values (CSV)"*/ return  "text/csv";
            case ".doc": /*  "Microsoft Word"*/ return  "application/msword";
            case ".epub": /*  "Electronic publication (EPUB)"*/ return  "application/epub+zip";
            case ".gif": /*  "Graphics Interchange Format (GIF)"*/ return  "image/gif";
            case ".html": /*  "HyperText Markup Language (HTML)"*/ return  "text/html";
            case ".htm": /*  "HyperText Markup Language (HTML)"*/ return  "text/html";
            case ".ico": /*  "Icon format"*/ return  "image/x-icon";
            case ".ics": /*  "iCalendar format"*/ return  "text/calendar";
            case ".jar": /*  "Java Archive (JAR)"*/ return  "application/java-archive";
            case ".jpg": /*  "JPEG images"*/ return  "image/jpeg";
            case ".jpeg": /*  "JPEG images"*/ return  "image/jpeg";
            case ".js": /*  "JavaScript (ECMAScript)"*/ return  "application/javascript";
            case ".json": /*  "JSON format"*/ return  "application/json";
            case ".mid": /*  "Musical Instrument Digital Interface (MIDI)"*/ return  "audio/midi";
            case ".midi": /*  "Musical Instrument Digital Interface (MIDI)"*/ return  "audio/midi";
            case ".mpeg": /*  "MPEG Video"*/ return  "video/mpeg";
            case ".mpkg": /*  "Apple Installer Package"*/ return  "application/vnd.apple.installer+xml";
            case ".odp": /*  "OpenDocuemnt presentation document"*/ return  "application/vnd.oasis.opendocument.presentation";
            case ".ods": /*  "OpenDocuemnt spreadsheet document"*/ return  "application/vnd.oasis.opendocument.spreadsheet";
            case ".odt": /*  "OpenDocument text document"*/ return  "application/vnd.oasis.opendocument.text";
            case ".oga": /*  "OGG audio"*/ return  "audio/ogg";
            case ".ogv": /*  "OGG video"*/ return  "video/ogg";
            case ".ogx": /*  "OGG"*/ return  "application/ogg";
            case ".pdf": /*  "Adobe Portable Document Format (PDF)"*/ return  "application/pdf";
            case ".ppt": /*  "Microsoft PowerPoint"*/ return  "application/vnd.ms-powerpoint";
            case ".rar": /*  "RAR archive"*/ return  "application/x-rar-compressed";
            case ".rtf": /*  "Rich Text Format (RTF)"*/ return  "application/rtf";
            case ".sh": /*  "Bourne shell script"*/ return  "application/x-sh";
            case ".svg": /*  "Scalable Vector Graphics (SVG)"*/ return  "image/svg+xml";
            case ".swf": /*  "Small web format (SWF) or Adobe Flash document"*/ return  "application/x-shockwave-flash";
            case ".tar": /*  "Tape Archive (TAR)"*/ return  "application/x-tar";
            case ".tif": /*  "Tagged Image File Format (TIFF)"*/ return  "image/tiff";
            case ".tiff": /*  "Tagged Image File Format (TIFF)"*/ return  "image/tiff";
            case ".ttf": /*  "TrueType Font"*/ return  "font/ttf";
            case ".vsd": /*  "Microsoft Visio"*/ return  "application/vnd.visio";
            case ".wav": /*  "Waveform Audio Format"*/ return  "audio/x-wav";
            case ".weba": /*  "WEBM audio"*/ return  "audio/webm";
            case ".webm": /*  "WEBM video"*/ return  "video/webm";
            case ".webp": /*  "WEBP image"*/ return  "image/webp";
            case ".woff": /*  "Web Open Font Format (WOFF)"*/ return  "font/woff";
            case ".woff2": /*  "Web Open Font Format (WOFF)"*/ return  "font/woff2";
            case ".xhtml": /*  "XHTML"*/ return  "application/xhtml+xml";
            case ".xls": /*  "Microsoft Excel"*/ return  "application/vnd.ms-excel";
            case ".xml": /*  "XML"*/ return  "application/xml";
            case ".xul": /*  "XUL"*/ return  "application/vnd.mozilla.xul+xml";
            case ".zip": /*  "ZIP archive"*/ return  "application/zip";
            case ".3gp": /*  "3GPP audio/video container"*/ return  "video/3gpp"; //audio/3gpp if it doesn't contain video
            case ".3g2": /*  "3GPP2 audio/video container"*/ return  "video/3gpp2"; //audio/3gpp2 if it doesn't contain video
            case ".7z": /*  "7-zip archive"*/ return  "application/x-7z-compressed";
        }
        return null;
    }
    
}
