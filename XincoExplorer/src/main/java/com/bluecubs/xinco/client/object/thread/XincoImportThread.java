/**
 * Copyright 2010 blueCubs.com
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>************************************************************ This project supports the
 * blueCubs vision of giving back to the community in exchange for free software! More information
 * on: http://www.bluecubs.org ************************************************************
 *
 * <p>Name: XincoImportThread
 *
 * <p>Description: XincoImportThread
 *
 * <p>Original Author: Javier A. Ortiz Date: 2010
 *
 * <p>Modifications:
 *
 * <p>Who? When? What?
 *
 * <p>************************************************************ XincoImportThread.java
 *
 * <p>Created on January 9, 2010, 3:52 PM
 */
package com.bluecubs.xinco.client.object.thread;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

/**
 * @author Javier A. Ortiz
 */
public class XincoImportThread extends Thread {
  private XincoExplorer explorer;

  @Override
  public void run() {
    if (this.explorer != null) {
      ResourceBundle xerb = this.explorer.getResourceBundle();
      // import data structure
      if (explorer.getSession().getCurrentTreeNodeSelection() != null) {
        if (explorer.getSession().getCurrentTreeNodeSelection().getUserObject().getClass()
            == XincoCoreNode.class) {
          JOptionPane.showMessageDialog(
              explorer,
              xerb.getString("window.massiveimport.info"),
              xerb.getString("window.massiveimport"),
              JOptionPane.INFORMATION_MESSAGE);
          try {
            JFileChooser fc = new JFileChooser();

            fc.setCurrentDirectory(new File(explorer.current_path));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            // show dialog
            int result = fc.showOpenDialog(explorer);

            if (result != JFileChooser.APPROVE_OPTION) {
              throw new XincoException(xerb.getString("datawizard.updatecancel"));
            }
            explorer.setCurrentPath(fc.getSelectedFile().toString());
            explorer.progressBar.setTitle(xerb.getString("window.massiveimport.progress"));
            explorer.progressBar.show();
            // update transaction info
            JOptionPane.showMessageDialog(
                explorer,
                xerb.getString("window.massiveimport.progress"),
                xerb.getString("window.massiveimport"),
                JOptionPane.INFORMATION_MESSAGE);
            explorer.jLabelInternalFrameInformationText.setText(
                xerb.getString("window.massiveimport.progress"));
            explorer.importContentOfFolder(
                (XincoCoreNode) explorer.getSession().getCurrentTreeNodeSelection().getUserObject(),
                new File(explorer.current_path));
            // select current path
            explorer.jTreeRepository.setSelectionPath(
                new TreePath(explorer.getSession().getCurrentTreeNodeSelection().getPath()));
            // update transaction info
            explorer.jLabelInternalFrameInformationText.setText(
                xerb.getString("window.massiveimport.importsuccess"));
          } catch (Exception ie) {
            JOptionPane.showMessageDialog(
                explorer,
                xerb.getString("window.massiveimport.importfailed")
                    + " "
                    + xerb.getString("general.reason")
                    + ": "
                    + ie.toString(),
                xerb.getString("general.error"),
                JOptionPane.WARNING_MESSAGE);
            explorer.jLabelInternalFrameInformationText.setText("");
            explorer.progressBar.hide();
          }
        }
      }
      explorer.progressBar.hide();
    }
  }

  public void setXincoExplorer(XincoExplorer e) {
    this.explorer = e;
  }
}
