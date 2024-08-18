package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.bluecubs.xinco.core.server.XincoDBManager;
import java.util.Locale;
import java.util.ResourceBundle;
import com.bluecubs.xinco.server.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

            BrowserDataExtractor extractor = new BrowserDataExtractor(request, session);
            XincoDBManager db = new XincoDBManager();
            if (db.config.isGuessLanguage()
                    && !extractor.getLanguage().isEmpty()
                    && extractor.isLanguageSupported(extractor.getLanguage())) {
                pageContext.forward("menu.jsp?list=" + extractor.getLanguage());
            }

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <title>xinco DMS - Open Source Document Management</title>\r\n");
      out.write("        <link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>\r\n");
      out.write("        <link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>\r\n");
      out.write("        <link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'>\r\n");
      out.write("    </head>\r\n");
      out.write("    <body>\r\n");
      out.write("        <center>\r\n");
      out.write("            <span class=\"text\"><br><img src=\"resources/images/blueCubs.gif\" border=\"0\" alt=\"Blue Cubs\"/>\r\n");
      out.write("                <br><span class=\"bigtext\">xinco DMS - the Core of Information and Document Management</span>\r\n");
      out.write("                <br><br>\r\n");
      out.write("\r\n");
      out.write("                <form name='language' action='menu.jsp'>\r\n");
      out.write("\r\n");
      out.write("                    <table border='0'>\r\n");
      out.write("                        <tbody>\r\n");
      out.write("                            <tr>\r\n");
      out.write("                                <td class=\"text\">Please choose a language:&nbsp;</td>\r\n");
      out.write("                                <td class=\"text\">\r\n");
      out.write("                                    ");

                                                int i = 0;
                                                ResourceBundle lrb = null;
                                                String[] locales;
                                                String text = "";
                                                //load locales
                                                lrb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessagesLocale", Locale.getDefault());
                                                locales = lrb.getString("AvailableLocales").split(",");
                                                out.println("<select name='list'>");
                                                for (i = 0; i < locales.length; i++) {
                                                    out.println("<option value='" + locales[i] + "'>" + lrb.getString("Locale." + locales[i]) + "</option>");
                                                }
                                                out.println("</select>&nbsp;");
                                    
      out.write("\r\n");
      out.write("                                </td>\r\n");
      out.write("                                <td class=\"text\">\r\n");
      out.write("                                    <input type='submit' value='Submit' />\r\n");
      out.write("                                </td>\r\n");
      out.write("                            </tr>\r\n");
      out.write("                    </table>\r\n");
      out.write("\r\n");
      out.write("                </form>\r\n");
      out.write("\r\n");
      out.write("                <br>\r\n");
      out.write("                <span class=\"text\" style=\"font-size: 10px;\">\r\n");
      out.write("                    ");

                                //load settings
                                ResourceBundle settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings");
                                out.println("[Version " + settings.getString("version.high") + "." + settings.getString("version.mid") + "." + settings.getString("version.low") + (settings.getString("version.postfix").isEmpty() ? "" : " " + settings.getString("version.postfix")) + "]");
                    
      out.write("\r\n");
      out.write("                </span>\r\n");
      out.write("\r\n");
      out.write("            </span>\r\n");
      out.write("        </center>\r\n");
      out.write("    </body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
