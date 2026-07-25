package com.bluecubs.xinco.ui.component;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.server.service.XincoCoreLog;
import com.bluecubs.xinco.server.service.XincoVersion;
import com.vaadin.flow.component.grid.Grid;
import java.util.ArrayList;
import java.util.List;

public class PropertyGrid extends Grid<PropertyGrid.Row> {

  public record Row(String property, String value) {}

  public PropertyGrid() {
    addColumn(Row::property).setHeader("Property");
    addColumn(Row::value).setHeader("Value");
    setSizeFull();
  }

  public void setNode(XincoCoreNodeServer node) {
    if (node == null) {
      setItems(List.of());
      return;
    }
    List<Row> rows = new ArrayList<>();
    rows.add(new Row("ID", String.valueOf(node.getId())));
    rows.add(new Row("Name", node.getDesignation()));
    if (node.getXincoCoreNodeId() > 0) {
      rows.add(new Row("Parent ID", String.valueOf(node.getXincoCoreNodeId())));
    }
    if (node.getXincoCoreLanguage() != null) {
      rows.add(new Row("Language", node.getXincoCoreLanguage().getSign()));
    }
    int itemCount = node.getXincoCoreData() != null ? node.getXincoCoreData().size() : 0;
    rows.add(new Row("Items", String.valueOf(itemCount)));
    setItems(rows);
  }

  public void setData(XincoCoreDataServer data) {
    if (data == null) {
      setItems(List.of());
      return;
    }
    List<Row> rows = new ArrayList<>();
    rows.add(new Row("ID", String.valueOf(data.getId())));
    rows.add(new Row("Name", data.getDesignation()));
    rows.add(new Row("Language", data.getXincoCoreLanguage().getSign()));
    String typeKey = data.getXincoCoreDataType().getDesignation();
    rows.add(new Row("Type", typeKey != null ? getTranslation(typeKey) : ""));
    rows.add(new Row("Status", String.valueOf(data.getStatusNumber())));

    List<Object> logs = data.getXincoCoreLogs();
    if (logs != null) {
      for (Object obj : logs) {
        XincoCoreLog log = (XincoCoreLog) obj;
        XincoVersion v = log.getVersion();
        String version = v.getVersionHigh() + "." + v.getVersionMid() + "." + v.getVersionLow();
        if (v.getVersionPostfix() != null && !v.getVersionPostfix().isBlank()) {
          version += "-" + v.getVersionPostfix();
        }
        String desc =
            log.getOpDescription() != null && !log.getOpDescription().isBlank()
                ? log.getOpDescription()
                : "(no description)";
        rows.add(new Row("v" + version, desc));
      }
    }

    setItems(rows);
  }
}
