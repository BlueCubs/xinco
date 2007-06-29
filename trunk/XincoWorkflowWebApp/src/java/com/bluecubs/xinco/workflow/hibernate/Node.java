
package com.bluecubs.xinco.workflow.hibernate ;
/**
 * 
 *
 * @hibernate.class
 *     table="NODE"
 *
 */
public class Node {
 
  // <editor-fold defaultstate="collapsed" desc=" Property:   String description ">
  private String description;
/**
  *   @hibernate.property
  */
   public String getDescription () {
      return description;
   } 
   public void setDescription (String description) {
      this.description = description;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc=" PrimaryKey:   String id ">
   private String id;
/**
  *   @hibernate.id
  *     generator-class="native"
  */
   public String getId () {
      return id;
   } 
   public void setId (String id) {
      this.id = id;
   }
   //</editor-fold>
}