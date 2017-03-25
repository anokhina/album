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
package ru.org.sevn.utilwin;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class FileUtil {
	public static boolean cmdStartDefaultEditor(File file) {
		if (file != null) {
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", "start", file.toURI().toASCIIString());
			try {
				pb.start();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	public static boolean openDefaultEditor(File file) {
		if (file != null) {
			try {
				Desktop.getDesktop().open(file);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private File tempFile;
	private Charset charset;
	
	public FileUtil() throws IOException {
		this("cp866");
	}
	public FileUtil(String outEncoding) throws IOException {
		this(Charset.forName(outEncoding));
	}
	public FileUtil(Charset charset) throws IOException {
		this.charset = charset;
		tempFile = File.createTempFile(FileUtil.class.getCanonicalName().replace(".", "_"), ".bat");
		//System.err.println("?????????????????>"+tempFile.getAbsolutePath());
		tempFile.deleteOnExit();
	}
	
	private void updateTemp(File file) throws IOException {
		String command = "RUNDLL32.EXE SHELL32.DLL,OpenAs_RunDLL "+file.getAbsolutePath();
		//System.err.println("***********"+command);
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tempFile), charset);
		osw.write(command);
		osw.close();
	}
	
	public boolean runTemp(File file) {
		if (file != null) {
			try {
				updateTemp(file);
				ProcessBuilder pb = new ProcessBuilder(tempFile.getAbsolutePath());
				pb.start();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
}
