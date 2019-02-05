package chushka.web.servlets;

import chushka.domain.models.service.ProductServiceModel;
import chushka.service.ProductService;
import chushka.util.ProductType;
import chushka.util.ViewsProvider;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

@WebServlet("/products/create")
public class ProductCreateServlet extends HttpServlet {

    private static final String CREATE_PRODUCT_HTML_FILE = "create-product";

    private final ProductService productService;
    private final ViewsProvider viewsProvider;

    @Inject
    public ProductCreateServlet(ProductService productService, ViewsProvider viewsProvider) {
        this.productService = productService;
        this.viewsProvider = viewsProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = this.viewsProvider
                .view(CREATE_PRODUCT_HTML_FILE)
                .replace("{{options}}", formatProductTypeOptions());

        resp.getWriter().println(html);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductServiceModel productServiceModel = new ProductServiceModel();
        productServiceModel.setName(req.getParameter("name"));
        productServiceModel.setDescription(req.getParameter("description"));
        productServiceModel.setType(req.getParameter("type"));

        this.productService.saveProduct(productServiceModel);
        resp.sendRedirect("/");
    }

    private String formatProductTypeOptions() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(ProductType.values()).forEach(productType ->
                builder.append(MessageFormat.format("<option value=\"{0}\">{0}</option>", productType.name())));

        return builder.toString().trim();
    }
}
