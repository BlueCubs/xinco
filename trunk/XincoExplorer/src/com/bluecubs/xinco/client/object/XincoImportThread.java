/**
 *Copyright 2007 blueCubs.com
 *
 *Licensed under the Apache License, Version 2.0 (the "License");
 *you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing, software
 *distributed under the License is distributed on an "AS IS" BASIS,
 *WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *See the License for the specific language governing permissions and
 *limitations under the License.
 *
 *************************************************************
 * This project supports the blueCubs vision of giving back
 * to the community in exchange for free software!
 * More information on: http://www.bluecubs.org
 *************************************************************
 *
 * Name:            importThread
 *
 * Description:     importThread
 *
 * Original Author: Javier A. Ortiz
 * Date:            2007
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 * importThread.java
 *
 * Created on January 9, 2007, 3:52 PM
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

/**
 *
 * @author ortizbj
 */
public class XincoImportThread extends Thread {
    private XincoExplorer explorer;
    @Override
    public void run() {
        if(this.explorer!=null){
            ResourceBundle xerb = this.explorer.getResourceBundle();
            //import data structure
            if (explorer.getSession().currentTreeNodeSelection != null) {
                if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                    JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.info"),
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
                        JOptionPane.showMessageDialog(explorer,
                                xerb.getString("window.massiveimport.progress"),
                                xerb.getString("window.massiveimport"),
                                JOptionPane.INFORMATION_MESSAGE);
                        explorer.jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.progress"));
                        explorer.importContentOfFolder((XincoCoreNode) explorer.getSession().currentTreeNodeSelection.getUserObject(),
                                new File(explorer.current_path));
                        this.sleep(10000);
                        // select current path
                        explorer.jTreeRepository.setSelectionPath(new TreePath(explorer.getSession().currentTreeNodeSelection.getPath()));
                        // update transaction info
                        explorer.jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.importsuccess"));
                    } catch (Exception ie) {
                        ie.printStackTrace();
                        JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.importfailed") +
                                " " + xerb.getString("general.reason") + ": " + ie.toString(), xerb.getString("general.error"),
                                JOptionPane.WARNING_MESSAGE);
                        explorer.jLabelInternalFrameInformationText.setText("");
                        explorer.progressBar.hide();
                    }
                }
            }
            try {
                explorer.getSession().xinco.rebuildIndex();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                explorer.progressBar.hide();
                JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.importfailed") +
                        " " + xerb.getString("general.reason") + ": " + ex.toString(), xerb.getString("general.error"),
                        JOptionPane.WARNING_MESSAGE);
                explorer.jLabelInternalFrameInformationText.setText("");
                explorer.progressBar.hide();
            }
            explorer.progressBar.hide();
        }
    }
    
    public void setXincoExplorer(XincoExplorer e){
        this.explorer=e;
    }
}
