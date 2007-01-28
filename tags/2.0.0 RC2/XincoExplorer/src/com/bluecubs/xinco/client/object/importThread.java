/*
 * importThread.java
 *
 * Created on January 9, 2007, 3:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import com.bluecubs.xinco.core.XincoCoreNode;
import com.bluecubs.xinco.core.XincoException;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

/**
 *
 * @author ortizbj
 */
public class importThread extends Thread {
    private XincoExplorer explorer;
    public void run() {
        if(this.explorer!=null){
            ResourceBundle xerb = this.explorer.getResourceBundle();
            //import data structure
            if (explorer.getSession().currentTreeNodeSelection != null) {
                if (explorer.getSession().currentTreeNodeSelection.getUserObject().getClass() == XincoCoreNode.class) {
                    JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.info"), xerb.getString("window.massiveimport"), JOptionPane.INFORMATION_MESSAGE);
                    try {
                        JFileChooser fc = new JFileChooser();
                        fc.setCurrentDirectory(new File(explorer.current_path));
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        //show dialog
                        int result = fc.showOpenDialog(explorer);
                        if(result == JFileChooser.APPROVE_OPTION) {
                            explorer.setCurrentPath(fc.getSelectedFile().toString());
                        } else {
                            throw new XincoException(xerb.getString("datawizard.updatecancel"));
                        }
                        explorer.progressBar.setTitle(xerb.getString("window.massiveimport.progress"));
                        explorer.progressBar.show();
                        //update transaction info
                        JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.progress"), xerb.getString("window.massiveimport"), JOptionPane.INFORMATION_MESSAGE);
                        explorer.jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.progress"));
                        explorer.importContentOfFolder((XincoCoreNode)explorer.getSession().currentTreeNodeSelection.getUserObject(), new File(explorer.current_path));
                        //select current path
                        explorer.jTreeRepository.setSelectionPath(new TreePath(explorer.getSession().currentTreeNodeSelection.getPath()));
                        //update transaction info
                        explorer.jLabelInternalFrameInformationText.setText(xerb.getString("window.massiveimport.importsuccess"));
                    } catch (Exception ie) {
                        JOptionPane.showMessageDialog(explorer, xerb.getString("window.massiveimport.importfailed") +
                                " " + xerb.getString("general.reason") + ": " + ie.toString(), xerb.getString("general.error"),
                                JOptionPane.WARNING_MESSAGE);
                        explorer.jLabelInternalFrameInformationText.setText("");
                        explorer.progressBar.hide();
                    }
                }
            }
            explorer.progressBar.hide();
        }
    }
    
    public void setXincoExplorer(XincoExplorer e){
        this.explorer=e;
    }
}
