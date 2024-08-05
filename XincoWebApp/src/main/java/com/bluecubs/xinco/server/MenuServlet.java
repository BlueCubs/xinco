package com.bluecubs.xinco.server;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MenuServlet extends HttpServlet {
    /**
     * Initializes the servlet.
     *
     * @param config
     * @throws jakarta.servlet.ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /** Destroys the servlet. */
    @Override
    public void destroy() {}

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws java.io.IOException
     */
    @SuppressWarnings("unchecked")
    protected synchronized void processRequest(
            HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
}
