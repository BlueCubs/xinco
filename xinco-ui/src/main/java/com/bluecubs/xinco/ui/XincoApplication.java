package com.bluecubs.xinco.ui;

import com.bluecubs.xinco.core.server.XincoDBManager;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class XincoApplication {

  private static final Logger LOG = Logger.getLogger(XincoApplication.class.getName());

  public static void main(String[] args) {
    SpringApplication.run(XincoApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initDatabase() {
    LOG.warning("Starting eager database initialization...");
    try {
      XincoDBManager.get();
      LOG.warning("Database initialization complete.");
    } catch (Exception e) {
      LOG.warning("Database initialization failed at startup: " + e.getMessage());
    }
  }
}
