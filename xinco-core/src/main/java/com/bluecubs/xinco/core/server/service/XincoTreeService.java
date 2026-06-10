package com.bluecubs.xinco.core.server.service;

import static com.bluecubs.xinco.core.server.XincoCoreACEServer.checkAccess;
import static com.bluecubs.xinco.core.server.XincoCoreDataHasDependencyServer.isRendering;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoCoreNodeServer;
import com.bluecubs.xinco.core.server.XincoCoreUserServer;
import com.bluecubs.xinco.server.service.XincoCoreACE;
import com.bluecubs.xinco.server.service.XincoCoreData;
import com.bluecubs.xinco.server.service.XincoCoreNode;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for managing tree operations like fetching nodes and data filtered by permissions.
 *
 * @author Javier A. Ortiz Bultron javier.ortiz.78@gmail.com
 */
@Service
public class XincoTreeService {

  private final ResourceBundle resourceBundle;

  @Autowired
  public XincoTreeService() {
    this.resourceBundle = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages");
  }

  /**
   * Fetches child nodes for a given parent node, filtered by user permissions.
   *
   * @param parentNodeId The ID of the parent node.
   * @param user The user requesting the nodes.
   * @return A list of authorized child nodes.
   * @throws XincoException If an error occurs.
   */
  public List<XincoCoreNode> getAuthorizedChildNodes(int parentNodeId, XincoCoreUserServer user)
      throws XincoException {
    XincoCoreNodeServer xcns = new XincoCoreNodeServer(parentNodeId);
    return xcns.getXincoCoreNodes().stream()
        .filter(
            subnode -> {
              XincoCoreACE access = checkAccess(user, (ArrayList) subnode.getXincoCoreAcl());
              return access.isAdminPermission() || access.isReadPermission();
            })
        .collect(Collectors.toList());
  }

  /**
   * Fetches child data for a given parent node, filtered by user permissions and excluding
   * renderings.
   *
   * @param parentNodeId The ID of the parent node.
   * @param user The user requesting the data.
   * @return A list of authorized child data items.
   * @throws XincoException If an error occurs.
   */
  public List<XincoCoreData> getAuthorizedChildData(int parentNodeId, XincoCoreUserServer user)
      throws XincoException {
    XincoCoreNodeServer xcns = new XincoCoreNodeServer(parentNodeId);
    List<XincoCoreData> authorizedData = new ArrayList<>();
    for (XincoCoreData temp : xcns.getXincoCoreData()) {
      XincoCoreACE access = checkAccess(user, (ArrayList) temp.getXincoCoreAcl());
      if (access.isAdminPermission() || access.isReadPermission()) {
        if (!isRendering(temp.getId())) {
          authorizedData.add(temp);
        }
      }
    }
    return authorizedData;
  }

  public List<PropertyDTO> getItemProperties(Object item, XincoCoreUserServer user) {
    List<PropertyDTO> properties = new ArrayList<>();
    if (item instanceof XincoCoreNode) {
      XincoCoreNode node = (XincoCoreNode) item;
      properties.add(new PropertyDTO(resourceBundle.getString("general.id"), "" + node.getId()));
      properties.add(
          new PropertyDTO(resourceBundle.getString("general.designation"), node.getDesignation()));
      if (node.getXincoCoreLanguage() != null) {
        properties.add(
            new PropertyDTO(
                resourceBundle.getString("general.language"),
                resourceBundle.getString(node.getXincoCoreLanguage().getDesignation())
                    + " ("
                    + node.getXincoCoreLanguage().getSign()
                    + ")"));
      }
      properties.add(
          new PropertyDTO(
              resourceBundle.getString("general.status"), getStatusString(node.getStatusNumber())));
    } else if (item instanceof XincoCoreData) {
      XincoCoreData data = (XincoCoreData) item;
      properties.add(new PropertyDTO(resourceBundle.getString("general.id"), "" + data.getId()));
      properties.add(
          new PropertyDTO(resourceBundle.getString("general.designation"), data.getDesignation()));
      if (data.getXincoCoreLanguage() != null) {
        properties.add(
            new PropertyDTO(
                resourceBundle.getString("general.language"),
                resourceBundle.getString(data.getXincoCoreLanguage().getDesignation())
                    + " ("
                    + data.getXincoCoreLanguage().getSign()
                    + ")"));
      }
      if (data.getXincoCoreDataType() != null) {
        properties.add(
            new PropertyDTO(
                resourceBundle.getString("general.datatype"),
                resourceBundle.getString(data.getXincoCoreDataType().getDesignation())));
      }
      properties.add(
          new PropertyDTO(
              resourceBundle.getString("general.status"), getStatusString(data.getStatusNumber())));
    }
    return properties;
  }

  private String getStatusString(int status) {
    switch (status) {
      case 1:
        return resourceBundle.getString("general.status.open");
      case 2:
        return resourceBundle.getString("general.status.locked");
      case 3:
        return resourceBundle.getString("general.status.archived");
      case 4:
        return resourceBundle.getString("general.status.checkedout");
      case 5:
        return resourceBundle.getString("general.status.published");
      default:
        return "Unknown (" + status + ")";
    }
  }
}
