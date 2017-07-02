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
package ru.org.sevn.fsbrowser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import ru.org.sevn.utilwt.FileCommenter;


public class MainFrame extends JFrame {

    private FilePane leftPane;
    private FilePane rightPane;
    
    public static void runMain(final FileCommenter commenter) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame frame = new MainFrame("FSBrowser", commenter);
                frame.showFrame();
            }
        });

    }

    public MainFrame(String title, FileCommenter commenter) {
        super(title);
        setBounds(100, 100, 780, 580);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        leftPane = new FilePane(commenter);
        rightPane = new FilePane(commenter);
        final JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftPane, rightPane);
        mainPane.setOneTouchExpandable(true);
        setContentPane(mainPane);
        mainPane.setDividerLocation(.5);
        
        setContentPane(mainPane);
        //createFileTable(commenter);
        //main folder button
        //folder list
        //full screen
        //open in external editor
        //show image info
        //find by description and comments, year month between
    }

    public void showFrame() {
        setVisible(true);
        leftPane.onVisible();
        rightPane.onVisible();
    }
}
