package fdmc.web.servlets;

import fdmc.domain.entities.Cat;
import fdmc.utils.HtmlReader;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * If configured through web.xml servlet would look like:
 *
 * <servlet>
 *     <servlet-name>CatsAllServlet</servlet-name>
 *     <servlet-class>fdmc.web.servlets.CatAllServlet</servlet-class>
 * </servlet>
 * <servlet-mapping>
 *     <servlet-name>CatsAllServlet</servlet-name>
 *     <url-pattern>/cats/all</url-pattern>
 * </servlet-mapping>
 */

@WebServlet("/cats/all")
public class CatAllServlet extends HttpServlet {

    private static final String ALL_CATS_HTML_FILE_PATH = "views/all.html";
    private static final String MISSING_CATS_MESSAGE = "There are no cats.<a href=\"/cats/create\">Create some!</a>";

    private final HtmlReader htmlReader;

    @Inject
    public CatAllServlet(HtmlReader htmlReader) {
        this.htmlReader = htmlReader;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String html = this.htmlReader.readHtmlFile(this.getServletContext().getRealPath(ALL_CATS_HTML_FILE_PATH));
        PrintWriter writer = resp.getWriter();
        String finalHtml;

        if (req.getSession().getAttribute("cats") == null) {
            finalHtml = html.replace("{{allCats}}", MISSING_CATS_MESSAGE);
        } else {
            List<Cat> cats = new ArrayList<>(((Map<String, Cat>) req.getSession().getAttribute("cats")).values());
            StringBuilder builder = new StringBuilder();
            for (Cat cat : cats) {
                builder.append(String.format("<a href=\"/cats/profile?catName=%s\">%s</a>", cat.getName(), cat.getName()))
                        .append("<br/>").append("</br>");
            }
            finalHtml = html.replace("{{allCats}}", builder.toString().trim());
        }
        writer.println(finalHtml);
    }
}
