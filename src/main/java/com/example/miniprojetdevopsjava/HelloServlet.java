package com.example.miniprojetdevopsjava;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Mini Projet DevOps</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }");
        out.println(".container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        out.println("h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }");
        out.println(".status { color: #27ae60; font-weight: bold; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class='container'>");
        out.println("<h1>🚀 Mini Projet DevOps Java</h1>");
        out.println("<p class='status'>✅ Application déployée avec succès !</p>");
        out.println("<p><strong>Version:</strong> 1.0-SNAPSHOT</p>");
        out.println("<p><strong>Date:</strong> " + new java.util.Date() + "</p>");
        out.println("<p><strong>Environnement:</strong> Production</p>");
        out.println("<hr>");
        out.println("<h2>📊 Pipeline CI/CD</h2>");
        out.println("<ul>");
        out.println("<li>✅ Build Maven</li>");
        out.println("<li>✅ Tests Unitaires JUnit</li>");
        out.println("<li>✅ Analyse SonarQube</li>");
        out.println("<li>✅ Déploiement Tomcat</li>");
        out.println("</ul>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Méthode non supportée pour ce endpoint
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method not supported");
    }

    @Override
    public void init() throws ServletException {
        // Initialisation personnalisée si nécessaire
        log("HelloServlet initialisé");
    }

    @Override
    public void destroy() {
        // Nettoyage personnalisé si nécessaire
        log("HelloServlet détruit");
    }
}