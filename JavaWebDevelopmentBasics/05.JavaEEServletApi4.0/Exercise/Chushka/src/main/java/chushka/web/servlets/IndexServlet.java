package chushka.web.servlets;

import chushka.domain.models.view.AllProductsViewModel;
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
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/")
public class IndexServlet extends HttpServlet {

    private static final String INDEX_HTML_FILE = "index";

    private final ProductService productService;
    private final ViewsProvider viewsProvider;
    private final ModelMapper modelMapper;

    @Inject
    public IndexServlet(ProductService productService, ViewsProvider viewsProvider, ModelMapper modelMapper) {
        this.productService = productService;
        this.viewsProvider = viewsProvider;
        this.modelMapper = modelMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String html = this.viewsProvider.view(INDEX_HTML_FILE)
                .replace("{{items}}", formatListItems());

        resp.getWriter().println(html);
    }

    private String formatListItems() {
        List<AllProductsViewModel> allProducts = this.productService.getAll().stream()
                .map(productServiceModel -> this.modelMapper.map(productServiceModel, AllProductsViewModel.class))
                .collect(Collectors.toList());

        StringBuilder builder = new StringBuilder();

        allProducts.forEach(product -> {
            builder.append(MessageFormat.format("<li><a href=\"/products/details?name={0}\">{0}</a></li>",
                    product.getName()))
                    .append(System.lineSeparator());
        });

        return builder.toString().trim();
    }
}
