package com.bluecubs.xinco.workflow;

import com.bigbross.bossa.Bossa;
import com.bigbross.bossa.BossaFactory;
import com.bigbross.bossa.PersistenceException;
import com.bigbross.bossa.wfnet.CaseTypeManager;
import java.io.File;

/**
 *
 * @author Javier A. Ortiz Bultrón <javier.ortiz.78@gmail.com>
 */
public class XincoWorkflowManager extends CaseTypeManager {

    private static XincoWorkflowManager instance = null;
    private static Bossa engine = null;

    private XincoWorkflowManager(Bossa engine) {
        super(engine);
    }

    public static XincoWorkflowManager get() throws PersistenceException {
        if (instance == null) {
            instance = new XincoWorkflowManager(getBossaEngine());
        }
        return instance;
    }

    private static Bossa getBossaEngine() throws PersistenceException {
        if (engine == null) {
            BossaFactory factory = new BossaFactory();
            //TODO: should be configurable
            String path="C:\\Temp\\Xinco\\Workflow";
            new File(path).mkdirs();
            factory.setStateDir(path);
            engine = factory.createBossa();
        }
        return engine;
    }
}
