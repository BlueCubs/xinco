package com.bluecubs.xinco.server;

import com.bluecubs.xinco.core.XincoException;
import com.bluecubs.xinco.core.server.XincoDBManager;
import com.bluecubs.xinco.tools.ZipUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuServlet extends HttpServlet {
  private ResourceBundle rb;
  private String realPath;

  /**
   * Initializes the servlet.
   *
   * @param config
   * @throws jakarta.servlet.ServletException
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    realPath = config.getServletContext().getRealPath("/");
  }

  /** Destroys the servlet. */
  @Override
  public void destroy() {}

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws Exception
   */
  protected synchronized void processRequest(
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    Locale loc;
    XincoDBManager db = new XincoDBManager();
    try {
      String list = request.getParameter("list");
      String[] locales;
      locales = list.split("_");
      loc =
          switch (locales.length) {
            case 1 -> new Locale(locales[0]);
            case 2 -> new Locale(locales[0], locales[1]);
            case 3 -> new Locale(locales[0], locales[1], locales[2]);
            default -> Locale.getDefault();
          };
    } catch (Exception e) {
      loc = Locale.getDefault();
    }
    rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc);
    // start output
    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();

    out.println("<html>");
    out.println("<head>");
    out.println("<title>" + rb.getString("message.admin.main.title") + "</title>");
    out.println("<link rel='stylesheet' href='xincostyle.css' type='text/css'/>");
    out.println(
        "<link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>");
    out.println("<link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'>");
    out.println("</head>");
    out.println(
        "<body "
            + (!db.config.isAllowOutsideLinks() ? "oncontextmenu='return false;' " : " ")
            + ">");
    out.println("<center>");
    out.println("<span class='text'>");
    out.println("<br><img src='resources/images/blueCubs.gif' border='0'/>");
    out.println(
        "<br><span class='bigtext'>"
            + rb.getString("message.admin.main.description")
            + "</span><br><br>");
    out.println("<table border='0' cellspacing='10' cellpadding='0'>");
    out.println("<tr>");
    File getdown;
    File getdownInstaller;
    File backup;
    String protocol = "http://";
    String url = request.getRequestURL().toString();
    url = url.substring(url.indexOf(protocol) + protocol.length(), url.indexOf("/XincoMenu"));
    File last = new File(realPath + "/client/" + url + ".xinco");
    if (!last.exists()) {
      File dir = new File(realPath + "/client/");
      String[] list = dir.list(new ExtensionFilter(".xinco"));

      if (list != null) {
        for (String s : list) {
          new File(dir.getAbsolutePath(), s).delete();
        }
      }
      try {
        getdown = new File(realPath + "/client/getdown/getdown.txt");
        backup = new File(realPath + "/client/getdown/getdown.txt.bak");
        getdownInstaller = new File(realPath + "/client/installer/getdown/getdown.txt");
        backup.createNewFile();
        // Update getdown file.
        if (getdown.exists()) {
          FileChannel source = null;
          FileChannel destination = null;
          try {
            source = new FileInputStream(getdown).getChannel();
            destination = new FileOutputStream(backup).getChannel();
            destination.transferFrom(source, 0, source.size());
          } finally {
            if (source != null) {
              source.close();
            }
            if (destination != null) {
              destination.close();
            }
          }
          try {
            StringBuilder contents = new StringBuilder();
            // use buffering, reading one line at a time
            // FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(getdown));
            try {
              String line = null; // not declared within while loop
              /*
               * readLine is a bit quirky :
               * it returns the content of a line MINUS the newline.
               * it returns null only for the END of the stream.
               * it returns an empty String if two newlines appear in a row.
               */
              while ((line = input.readLine()) != null) {
                if (line.contains("appbase")) {
                  String start = line.substring(0, line.indexOf(protocol) + protocol.length());
                  String end = null;
                  end = line.substring(line.indexOf("/xinco"));
                  line = start + url + end;
                }
                contents.append(line);
                contents.append(System.lineSeparator());
              }
              // use buffering to update getdown file
              try (Writer output = new BufferedWriter(new FileWriter(getdown))) {
                // FileWriter always assumes default encoding is OK!
                output.write(contents.toString());
              }

              // Process the second file
              try (BufferedReader input2 = new BufferedReader(new FileReader(getdownInstaller))) {
                String line2 = null;
                StringBuilder contents2 = new StringBuilder();
                while ((line2 = input2.readLine()) != null) {
                  if (line2.contains("appbase")) {
                    String start2 = line2.substring(0, line2.indexOf(protocol) + protocol.length());
                    String end2 = line2.substring(line2.indexOf("/xinco"));
                    line2 = start2 + url + end2;
                  }
                  contents2.append(line2);
                  contents2.append(System.getProperty("line.separator"));
                }
                try (Writer output2 = new BufferedWriter(new FileWriter(getdownInstaller))) {
                  output2.write(contents2.toString());
                }
              }
            } finally {
              input.close();
              backup.delete();
              last.createNewFile();
            }
          } catch (IOException ex) {
            try {
              source = new FileInputStream(backup).getChannel();
              destination = new FileOutputStream(getdown).getChannel();
              destination.transferFrom(source, 0, source.size());
              backup.delete();
            } finally {
              if (source != null) {
                source.close();
              }
              if (destination != null) {
                destination.close();
              }
            }
          }
        } else {
          throw new XincoException("Missing getdown.txt!");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    out.println("</tr>");
    try {
      File zipFile = new File(realPath + "/client/installer/zip/installer.zip");
      if (!zipFile.exists()) {
        // Call the method to zip the folder
        String sourceFolderPath = realPath + "/client/installer/getdown";
        String zipFilePath = realPath + "/client/installer/zip/XincoExplorer.zip";
        try {
          ZipUtil.zipFolder(sourceFolderPath, zipFilePath);
        } catch (IOException e) {
          throw new XincoException("Failed to zip folder: " + sourceFolderPath, e);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    out.println(
        "<td class='text'><a href='client/installer/zip/XincoExplorer.zip' class='link'>"
            + rb.getString("message.admin.main.webstart.link")
            + "</a></td>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.webstart") + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.endpoint.label") + "</td>");
    String xinco_service_endpoint = request.getRequestURL().toString();
    xinco_service_endpoint =
        xinco_service_endpoint.substring(0, xinco_service_endpoint.indexOf("/XincoMenu"));
    xinco_service_endpoint = xinco_service_endpoint + "/services/Xinco";
    out.println("<td class='text'><b>" + xinco_service_endpoint + "</b></td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class='text'><a href='XincoPublisher?list="
            + request.getParameter("list")
            + "' class='link'>"
            + rb.getString("message.admin.main.publisher.label")
            + "</a></td>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.publisherdesc") + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class='text'>"
            + (db.config.isAllowOutsideLinks()
                ? "<a href='http://java.sun.com' class='link'>"
                    + rb.getString("message.admin.main.java.label")
                    + "</a>"
                : "http://java.sun.com")
            + "</td>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.javadesc") + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class='text'>&nbsp;</td>");
    out.println("<td class='text'>&nbsp;</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class='text'><a href='XincoAdmin?list="
            + request.getParameter("list")
            + "' class='link'>"
            + rb.getString("message.admin.main.admin.label")
            + "</a></td>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.admindesc"));
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class='text'><a href='XincoCron?list="
            + request.getParameter("list")
            + "' class='link'>"
            + rb.getString("message.admin.main.xincocron.label")
            + "</a></td>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.xincocrondesc") + "</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println(
        "<td class='text'><a href='happyaxis.jsp'  class='link'>"
            + rb.getString("message.admin.main.validate.label")
            + "</a></td>");
    out.println("<td class='text'>" + rb.getString("message.admin.main.validatedesc"));
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class='text'>&nbsp;</td>");
    out.println("<td class='text'>&nbsp;</td>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td class='text'>&nbsp;</td>");
    out.println(
        "<td class='text'>&copy; "
            + rb.getString("general.copyright.date")
            + ", "
            + // Avoid external links if general.setting.allowoutsidelinks is set to false
            // Security bug
            (db.config.isAllowOutsideLinks()
                ? rb.getString("message.admin.main.footer")
                : "blueCubs.com and xinco.org")
            + "</a></td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("</span>");
    out.println("</center>");
    out.println("</body>");
    out.println("</html>");
  }

  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    try {
      processRequest(request, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      processRequest(request, response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
