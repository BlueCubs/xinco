/**
 *Copyright 2011 blueCubs.com
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
 * Name:            XincoMenuRepository
 *
 * Description:     XincoMenuRepository
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
 * XincoMenuRepository.java
 *
 * Created on December 11, 2006, 2:19 PM
 */
package com.bluecubs.xinco.client.object.menu;

import com.bluecubs.xinco.client.XincoExplorer;
import java.util.ResourceBundle;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Javier A. Ortiz
 */
public class XincoMenuRepository extends JMenu{
    private JMenuItem tmi = null;
    private XincoExplorer explorer;
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
    private JMenuItem [] items = new JMenuItem[20];
    public ResourceBundle xerb;
    private int counter=0;

    /**
     * Creates a new instance of XincoMenuRepository
     * @param explorer
     */
    public XincoMenuRepository(final XincoExplorer explorer){
        this.explorer=explorer;
        addMouseListener(this.explorer);
        xerb=this.explorer.getResourceBundle();
        //add item
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        this.items[counter].setEnabled(true);
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        this.items[counter].setEnabled(true);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        increaseItemNumber();
        //add item
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        addSeparator();
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        increaseItemNumber();
        this.items[counter] = new JMenuItem(this.explorer.getActionHandler().getActions()[counter]);
        add(this.items[counter]);
        //add item
        addSeparator();
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

    private int increaseItemNumber(){
        counter++;
        return counter;
    }

    public JMenuItem[] getItems(){
        return this.items;
    }
}
