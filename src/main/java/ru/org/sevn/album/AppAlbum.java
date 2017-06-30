/*******************************************************************************
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
 *******************************************************************************/
package ru.org.sevn.album;

import java.io.File;

public class AppAlbum {
    public static void main( String[] args ) throws Exception {
        String magicHome = System.getenv("MAGICK_HOME");
        System.out.println("Hi>"+System.getProperty("java.version")+":"+magicHome);
        File IM_PATH = null;
        if (magicHome != null) {
        	IM_PATH = new File(magicHome);
        	if (!IM_PATH.exists()) {
        		IM_PATH = null;
        	}
        }
        if (IM_PATH == null) {
        	IM_PATH = new File("D:/Portable/Portable/PortableApps/ImageMagick-7.0.6-0-portable-Q16-x86");
        }
        MainFrame.runMain(IM_PATH);
    }
}
