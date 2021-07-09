package com.worldline.eyar.controller.product;

import com.worldline.eyar.common.request.product.ProductRequest;
import com.worldline.eyar.common.response.product.ProductResponse;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProductController.PRODUCT_URL_CONTEXT)
public class ProductController extends CrudController<ProductRequest, ProductResponse> {

    public static final String PRODUCT_URL_CONTEXT = "/product";

    @Autowired
    public ProductController(ProductService crudService) {
        super(crudService);
    }


}
