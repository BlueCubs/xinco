
package com.bluecubs.xinco.workflow.hibernate ;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 * @netbeans.javax.persistence.util
 */
public class WorkflowHibernateUtil {
   private static EntityManagerFactory emf;

   public static EntityManagerFactory getEntityManagerFactory() {
      if (emf == null ) {
         // todo change the entity persistence unit name
         emf = Persistence.createEntityManagerFactory("WorkflowPersistanceManager");
      }
      return emf;
   } 

   public static void closeEntityManagerFactory () {
       if (emf != null ) {
          emf.close();
       }
   }
}