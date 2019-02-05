package chushka.web.servlets;

import chushka.domain.models.view.ProductDetailsViewModel;
import chushka.service.ProductService;
import chushka.util.ModelMapper;
import chushka.util.ViewsProvider;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet("/products/details")
public class ProductDetailsServlet extends HttpServlet {

    private static final String PRODUCT_DETAILS_HTML_FILE = "details-product";

    private final ProductService productService;
    private final ViewsProvider viewsProvider;
    private final ModelMapper modelMapper;

    @Inject
    public ProductDetailsServlet(ProductService productService, ViewsProvider viewsProvider, ModelMapper modelMapper) {
        this.productService = productService;
        this.viewsProvider = viewsProvider;
        this.modelMapper = modelMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = URLDecoder.decode(req.getQueryString(), "UTF-8").split("=")[1];

        ProductDetailsViewModel productDetailsViewModel = this.modelMapper
                .map(this.productService.findProductByName(name), ProductDetailsViewModel.class);
        String html = this.viewsProvider.view(PRODUCT_DETAILS_HTML_FILE)
                .replace("{{name}}", productDetailsViewModel.getName())
                .replace("{{description}}", productDetailsViewModel.getDescription())
                .replace("{{type}}", productDetailsViewModel.getType());
        resp.getWriter().println(html);
    }
}
