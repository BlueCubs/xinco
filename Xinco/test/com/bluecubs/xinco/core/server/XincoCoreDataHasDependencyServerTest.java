package com.bluecubs.xinco.core.server;

import com.bluecubs.xinco.core.server.persistence.controller.XincoCoreDataJpaController;
import com.bluecubs.xinco.core.server.persistence.controller.XincoDependencyTypeJpaController;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoCoreDataHasDependencyServerTest extends XincoTestCase {

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
        System.out.println("writeAndDelete");
        int xincoCoreDataParentId = 1, xincoCoreDataChildrenId = 2, dependencyTypeId = 3;
        XincoCoreDataHasDependencyServer instance =
                new XincoCoreDataHasDependencyServer(new XincoCoreDataJpaController().findXincoCoreData(xincoCoreDataParentId),
                new XincoCoreDataJpaController().findXincoCoreData(xincoCoreDataChildrenId),
                new XincoDependencyTypeJpaController().findXincoDependencyType(dependencyTypeId));
        assertEquals(instance.write2DB(), 0);
        XincoCoreDataHasDependencyServer.deleteFromDB(xincoCoreDataParentId,
                xincoCoreDataChildrenId, dependencyTypeId);
        parameters.clear();
        parameters.put("xincoCoreDataParentId", xincoCoreDataParentId);
        parameters.put("xincoCoreDataChildrenId", xincoCoreDataChildrenId);
        parameters.put("dependencyTypeId", dependencyTypeId);
        result = XincoDBManager.createdQuery("SELECT x FROM XincoCoreDataHasDependency x WHERE "
                + "x.xincoCoreDataHasDependencyPK.xincoCoreDataParentId = :xincoCoreDataParentId "
                + "and x.xincoCoreDataHasDependencyPK.xincoCoreDataChildrenId = :xincoCoreDataChildrenId "
                + "and x.xincoCoreDataHasDependencyPK.dependencyTypeId = :dependencyTypeId", parameters);
        assertTrue(result.isEmpty());
    }
}
