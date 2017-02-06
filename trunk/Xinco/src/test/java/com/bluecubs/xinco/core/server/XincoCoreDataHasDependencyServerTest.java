package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.XincoException;
import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.deleteFromDB;
import static com.bluecubs.xinco.core.server.XincoDBManager.createdQuery;
import static com.bluecubs.xinco.core.server.XincoDBManager.getEntityManagerFactory;
import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
import java.util.HashMap;
import java.util.List;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataHasDependencyServerTest extends AbstractXincoDataBaseTestCase {

    private static HashMap parameters = new HashMap();
    private static List result;

    public XincoCoreDataHasDependencyServerTest(String testName) {
        super(testName);
    }

    /**
     * Test of write2DB method, of class XincoCoreDataHasDependencyServer.
     */
    @Test
    public void testWriteAndDelete() {
        try {
            int xincoCoreDataParentId = 1, xincoCoreDataChildrenId = 2, dependencyTypeId = 3;
            XincoCoreDataHasDependencyServer instance =
                    new XincoCoreDataHasDependencyServer(new XincoCoreDataJpaController(getEntityManagerFactory()).findXincoCoreData(xincoCoreDataParentId),
                    new XincoCoreDataJpaController(getEntityManagerFactory()).findXincoCoreData(xincoCoreDataChildrenId),
                    new XincoDependencyTypeJpaController(getEntityManagerFactory()).findXincoDependencyType(dependencyTypeId));
            assertEquals(instance.write2DB(), 0);
            deleteFromDB(xincoCoreDataParentId,
                    xincoCoreDataChildrenId, dependencyTypeId);
            parameters.clear();
            parameters.put("xincoCoreDataParentId", xincoCoreDataParentId);
            parameters.put("xincoCoreDataChildrenId", xincoCoreDataChildrenId);
            parameters.put("dependencyTypeId", dependencyTypeId);
            result = createdQuery("SELECT x FROM XincoCoreDataHasDependency x WHERE "
                    + "x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId "
                    + "and x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId "
                    + "and x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId", parameters);
            assertTrue(result.isEmpty());
        } catch (XincoException ex) {
            getLogger(XincoCoreDataHasDependencyServerTest.class.getName()).log(SEVERE, null, ex);
            fail();
        }
    }
}
