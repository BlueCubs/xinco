package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.Locale;
import java.util.ResourceBundle;
import com.bluecubs.xinco.core.server.XincoDBManager;

public final class changePassword_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\r\n");
      out.write("\"http://www.w3.org/TR/html4/loose.dtd\">\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("    <head>\r\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("        <title>Password Aging</title>\r\n");
      out.write("        <link rel=\"stylesheet\" href=\"xincostyle.css\" type=\"text/css\"/>\r\n");
      out.write("        <link rel='shortcut icon' href='resources/images/favicon.ico' type='image/x-icon'>\r\n");
      out.write("        <link rel='icon' href='resources/images/favicon.ico' type='image/x-icon'>\r\n");
      out.write("    </head>\r\n");
      out.write("    ");

            XincoDBManager DBM = new XincoDBManager();
            out.println("<body " + (!DBM.config.isAllowOutsideLinks() ? "oncontextmenu='return false;' class='text'>" : "class='text'>"));
    
      out.write("\r\n");
      out.write("    <center>\r\n");
      out.write("        <br/><img src=\"resources/images/blueCubs.gif\" width=\"356\" height=\"400\" alt=\"blueCubs\"/><br/>\r\n");
      out.write("        ");

            Locale loc = null;
            try {
                String list = request.getParameter("list");
                String[] locales;
                locales = list.split("_");
                switch (locales.length) {
                    case 1:
                        loc = new Locale(locales[0]);
                        break;
                    case 2:
                        loc = new Locale(locales[0], locales[1]);
                        break;
                    case 3:
                        loc = new Locale(locales[0], locales[1], locales[2]);
                        break;
                    default:
                        loc = Locale.getDefault();
                }
            } catch (Exception e) {
                loc = Locale.getDefault();
            }
            ResourceBundle rb = ResourceBundle.getBundle("com.bluecubs.xinco.messages.XincoMessages", loc),
                    settings = ResourceBundle.getBundle("com.bluecubs.xinco.settings.settings", loc);
            out.println("<span class='bigtext'>" + rb.getString("password.aged") + "</span><br/><br/>");
            out.print(rb.getString("password.change.warning").replaceAll("%n", settings.getString("password.unusable_period")));
            out.println("</span>");
      out.write("\r\n");
      out.write("        <br/><br/>\r\n");
      out.write("        <form name=\"password\" action=\"XincoAdmin\" method=\"post\">\r\n");
      out.write("            <table border=\"0\">\r\n");
      out.write("                <tbody>\r\n");
      out.write("                    <tr>\r\n");
      out.write("                        <td class=\"text\">");
out.println(rb.getString("general.password") + ":");
      out.write("</td>\r\n");
      out.write("                        <td class=\"text\"><input type=\"password\" name=\"new\" value=\"\" /></td>\r\n");
      out.write("                    </tr>\r\n");
      out.write("                    <tr>\r\n");
      out.write("                        <td class=\"text\">");
out.println(rb.getString("password.confirm") + ":");
      out.write("</td>\r\n");
      out.write("                        <td class=\"text\"><input type=\"password\" name=\"confirm\" value=\"\" /></td>\r\n");
      out.write("                    </tr>\r\n");
      out.write("                </tbody>\r\n");
      out.write("            </table>\r\n");
      out.write("            <br/>\r\n");
      out.write("            <input type=\"submit\" value=\"");
out.println(rb.getString("password.change"));
      out.write("\" name=\"changePassword\" />\r\n");
      out.write("            <input type=\"hidden\" name=\"user\" value=\"");
 out.print(request.getParameter("user"));
      out.write("\"/>\r\n");
      out.write("        </form>\r\n");
      out.write("    </center>\r\n");
      out.write("    ");

            out.println("</body>");
    
      out.write("\r\n");
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
