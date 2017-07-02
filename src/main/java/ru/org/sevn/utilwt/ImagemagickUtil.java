/** *****************************************************************************
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
 ****************************************************************************** */
package ru.org.sevn.utilwt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.StringJoiner;

public class ImagemagickUtil implements FileCommenter {

    /*
\n	newline
\r	carriage return
<	less-than character.
>	greater-than character.
&	ampersand character.
%%	a percent sign
%b	file size of image read in
%c	comment meta-data property
%d	directory component of path
%e	filename extension or suffix
%f	filename (including suffix)
%g	layer canvas page geometry (equivalent to "%Wx%H%X%Y")
%h	current image height in pixels
%i	image filename (note: becomes output filename for "info:")
%k	CALCULATED: number of unique colors
%l	label meta-data property
%m	image file format (file magic)
%n	number of images in current image sequence
%o	output filename (used for delegates)
%p	index of image in current image list
%q	quantum depth (compile-time constant)
%r	image class and colorspace
%s	scene number (from input unless re-assigned)
%t	filename without directory or extension (suffix)
%u	unique temporary filename (used for delegates)
%w	current width in pixels
%x	x resolution (density)
%y	y resolution (density)
%z	image depth (as read in unless modified, image save depth)
%A	image transparency channel enabled (true/false)
%C	image compression type
%D	image GIF dispose method
%G	original image size (%wx%h; before any resizes)
%H	page (canvas) height
%M	Magick filename (original file exactly as given, including read mods)
%O	page (canvas) offset ( = %X%Y )
%P	page (canvas) size ( = %Wx%H )
%Q	image compression quality ( 0 = default )
%S	?? scenes ??
%T	image time delay (in centi-seconds)
%U	image resolution units
%W	page (canvas) width
%X	page (canvas) x offset (including sign)
%Y	page (canvas) y offset (including sign)
%Z	unique filename (used for delegates)
%@	CALCULATED: trim bounding box (without actually trimming)
%#	CALCULATED: 'signature' hash of image values	 * 
     */
    //http://www.imagemagick.org/Usage/text/
    //http://www.imagemagick.org/script/command-line-options.php#comment
    // see https://svn.apache.org/repos/asf/commons/proper/imaging/trunk/src/test/java/org/apache/commons/imaging/examples/MetadataExample.java
    //identify -verbose commentedPciture.png |grep comment
    //convert image -set comment "sometext" image
    //convert image -format "%c" info:
    //identify -format "%c" image
    //http://www.imagemagick.org/script/escape.php

    private final String IDENTIFY;
    private final String CONVERT;

    public String identifyComment(File file) {
        String ret = null;
        ProcessBuilder pb = new ProcessBuilder(IDENTIFY, "-format", "%c", file.getAbsolutePath());
        //pb.redirectErrorStream(true);
        try {
            Process p = pb.start();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), charset));
            StringJoiner sj = new StringJoiner(System.getProperty("line.separator"));
            reader.lines().iterator().forEachRemaining(sj::add);
            ret = sj.toString();

            p.waitFor();
            //p.destroy();
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public final static String UTF8 = "UTF-8";
    public final static String UTF16 = "UTF-16";
    public final static String CP866 = "cp866";
    public final static String CP1251 = "cp1251";
    public final static String ISO = "iso-8859-1";
    //iso-8859-1

    private File tempFile;
    private Charset charset;

    public ImagemagickUtil(File imPath) throws IOException {
        this(imPath, CP1251);
    }

    public ImagemagickUtil(File imPath, String outEncoding) throws IOException {
        this(imPath, Charset.forName(outEncoding));
    }

    public ImagemagickUtil(File imPath, Charset charset) throws IOException {
        this.charset = charset;
        IDENTIFY = new File(imPath, "identify.exe").getAbsolutePath();
        CONVERT = new File(imPath, "convert.exe").getAbsolutePath();
        tempFile = File.createTempFile(ImagemagickUtil.class.getCanonicalName().replace(".", "_"), ".txt");
        //System.err.println("?????????????????>"+tempFile.getAbsolutePath());
        tempFile.deleteOnExit();
    }

    private void updateTemp(String text) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tempFile), charset);
        osw.write(text);
        osw.close();
    }

    public boolean setComment(File file, String text) {
        //convert.exe C:\pub\20140906_145606_1.jpg -set comment @zzz1.txt C:\pub\20140906_145606_1.jpg 
        if (file != null && text != null) {
            try {
                updateTemp(text);
                ProcessBuilder pb = new ProcessBuilder(CONVERT, file.getAbsolutePath(), "-set", "comment", "@" + tempFile.getAbsolutePath(), file.getAbsolutePath());
                Process p = pb.start();
                p.waitFor();
                return true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String identifyComment(Path file) {
        try {
            return identifyComment(file.toFile());
        } catch (UnsupportedOperationException e) {}
        return null;
    }

    @Override
    public boolean setComment(Path file, String text) {
        try {
            return setComment(file.toFile(), text);
        } catch (UnsupportedOperationException e) {}
        return false;
    }

}
