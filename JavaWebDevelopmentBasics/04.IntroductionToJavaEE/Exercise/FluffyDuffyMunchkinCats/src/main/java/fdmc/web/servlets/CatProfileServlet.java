package fdmc.web.servlets;

import fdmc.domain.entities.Cat;
import fdmc.utils.HtmlReader;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/cats/profile")
public class CatProfileServlet extends HttpServlet {

    private static final String CAT_PROFILE_HTML_FILE_PATH = "views/profile.html";
    private static final String NON_EXISTENT_CAT_HTML_FILE_PATH = "views/non-existent-cat.html";

    private final HtmlReader htmlReader;

    @Inject
    public CatProfileServlet(HtmlReader htmlReader) {
        this.htmlReader = htmlReader;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();

        String html;
        String finalHtml;
        if (req.getSession().getAttribute("cats") == null) {
            html = this.htmlReader.readHtmlFile(this.getServletContext().getRealPath(NON_EXISTENT_CAT_HTML_FILE_PATH));
            finalHtml = html.replace("{{catName}}", req.getParameter("catName"));
        } else {
            Cat cat = ((Map<String, Cat>)req.getSession().getAttribute("cats")).get(req.getParameter("catName"));
            html = this.htmlReader.readHtmlFile(this.getServletContext().getRealPath(CAT_PROFILE_HTML_FILE_PATH));
            finalHtml = html
                    .replace("{{catName}}", cat.getName())
                    .replace("{{catBreed}}", cat.getBreed())
                    .replace("{{catColor}}", cat.getColor())
                    .replace("{{catAge}}", String.valueOf(cat.getAge()));
        }
        writer.println(finalHtml);
    }
}
