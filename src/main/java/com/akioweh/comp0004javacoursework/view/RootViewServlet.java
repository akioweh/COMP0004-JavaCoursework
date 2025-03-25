package com.akioweh.comp0004javacoursework.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for rendering the home page.
 * This servlet simply forwards to the index.jsp page.
 */
@WebServlet(name = "Root View", urlPatterns = {"", "/"})
public class RootViewServlet extends ViewServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Forward to the index JSP
        forwardToJsp(request, response, "/WEB-INF/jsp/index.jsp");
    }
}