package com.bluecubs.xinco.ui.component;

import com.bluecubs.xinco.core.server.XincoCoreDataServer;
import com.vaadin.flow.component.grid.Grid;

public class PropertyGrid extends Grid<PropertyGrid.Row> {

  public record Row(String property, String value) {}

  public PropertyGrid() {
    addColumn(Row::property).setHeader("Property");
    addColumn(Row::value).setHeader("Value");
    setSizeFull();
  }

  public void setData(XincoCoreDataServer data) {
    setItems(
        new Row("ID", String.valueOf(data.getId())),
        new Row("Name", data.getDesignation()),
        new Row("Language", data.getXincoCoreLanguage().getSign()),
        new Row("Status", String.valueOf(data.getStatusNumber())),
        new Row("Type", data.getXincoCoreDataType().getDesignation()));
  }
}
