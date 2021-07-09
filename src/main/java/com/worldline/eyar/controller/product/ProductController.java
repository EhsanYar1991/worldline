package com.worldline.eyar.controller.product;

import com.worldline.eyar.common.request.user.UserRequest;
import com.worldline.eyar.common.response.UserResponse;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.product.ProductService;
import com.worldline.eyar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ProductController.PRODUCT_URL_CONTEXT)
public class ProductController extends CrudController<UserRequest, UserResponse> {

    public static final String PRODUCT_URL_CONTEXT = "/product";

    @Autowired
    public ProductController(ProductService crudService) {
        super(crudService);
    }


}
