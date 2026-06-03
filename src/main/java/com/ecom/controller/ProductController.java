package com.ecom.controller;
import com.ecom.entity.Category;
import com.ecom.entity.Product;
import com.ecom.entity.User;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {
        if (p != null) {
            String email = p.getName();
            User user = userService.getUserByEmail(email);
            m.addAttribute("user", user);
        }
    }
    @GetMapping("/add")
    public String loadAddProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
                              HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());
        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                    + image.getOriginalFilename());

            // System.out.println(path);
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Product Saved Success");
        } else {
            session.setAttribute("errorMsg", "something wrong on server");
        }

        return "redirect:/products/add";
    }

    @GetMapping("/view")
    public String viewProduct(Model m) {
       m.addAttribute("products",productService.getAllProducts());
        return "admin/products";
    }

    @RequestMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id)
    {
        productService.deleteProduct(id);
        return "redirect:/products/veiw";
    }

    @RequestMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") int id, Model model)
    {
        model.addAttribute("product",productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @RequestMapping("/update")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,HttpSession session)
    {
        Product product1=productService.updateProduct(product,image);
        if(product1!=null)
        {
            session.setAttribute("succMsg", "Product updated Successfully");
        }
        else
        {
            session.setAttribute("errorMsg", "something wrong on server");
        }
        return "redirect:/products/edit/" + product1.getId();
    }
}
