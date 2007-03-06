/**
 *Copyright 2006 blueCubs.com
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
 * Name:            XincoMenuPopUpRepository
 *
 * Description:     XincoMenuPopUpRepository
 *
 * Original Author: Javier A. Ortiz
 * Date:            2006
 *
 * Modifications:
 *
 * Who?             When?             What?
 *
 *
 *************************************************************
 * XincoMenuPopUpRepository.java
 *
 * Created on December 11, 2006, 2:19 PM
 */

package com.bluecubs.xinco.client.object;

import com.bluecubs.xinco.client.XincoExplorer;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author ortizbj
 */
public class XincoPopUpMenuRepository extends JPopupMenu{
    public JMenuItem tmi = null;
    public XincoExplorer explorer=null;
    public JMenuItem AddData=null,AddDataStructure=null,
            ViewURL=null,EmailContact=null,
            EditFolderData=null,CheckoutData=null,
            UndoCheckoutData=null,CheckinData=null,
            PublishData=null,LockData=null,
            DownloadRevision=null,Refresh=null,
            AddFolder=null,ViewEditAddAttributes=null,
            EditFolderDataACL=null,MoveFolderData=null,
            InsertFolderData=null,ViewData=null,
            CommentData=null;
    private JMenuItem [] items,copy;
    public ResourceBundle xerb;
    
    /**
     * Creates a new instance of XincoMenuPopUpRepository
     */
    public XincoPopUpMenuRepository(final XincoExplorer explorer) {
        this.explorer=explorer;
        xerb=this.explorer.getResourceBundle();
        int count =0;
        items=new JMenuItem[this.explorer.getActionSize()];
        copy=new JMenuItem[this.explorer.getActionSize()];
        //Make a copy & paste of the menus in XincoMenuRepository class!
        this.copy=((XincoMenuRepository)this.explorer.getJMenuRepository()).getItems();
        Component [] components=((XincoMenuRepository)this.explorer.getJMenuRepository()).getMenuComponents();
        for(int i=0;i<components.length;i++) {
            if(components[i].getClass()==Separator.class)
                add(components[i]);
            else{
                items[count]=new JMenuItem(copy[count].getAction());
                add(items[count]);
                count++;
            }
        }
        resetItems();
    }
    public void resetItems(){
        for(int i=1;i<this.items.length;i++){
            if(this.items[i]!=null)
                this.itemSetEnable(i,false);
        }
    }
    public void itemSetEnable(int number,boolean enable){
        items[number].setEnabled(enable);
    }
}
