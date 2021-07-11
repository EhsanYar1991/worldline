package com.worldline.eyar.controller.product;

import com.worldline.eyar.common.request.product.ProductQueryRequest;
import com.worldline.eyar.common.request.product.ProductRequest;
import com.worldline.eyar.common.response.product.ProductResponse;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ProductController.PRODUCT_URL_CONTEXT)
public class ProductController extends CrudController<ProductRequest, ProductResponse> {

    public static final String PRODUCT_URL_CONTEXT = "/product";
    private static final String PRODUCT_QUERY_URL_CONTEXT = "/query";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        super(productService);
        this.productService = productService;
    }

    @PostMapping(PRODUCT_QUERY_URL_CONTEXT)
    public ResponseEntity<?> query(@Valid ProductQueryRequest request){
      return okResponse(productService.query(request));
    }


}
