package com.worldline.eyar.controller.productcategory;

import com.worldline.eyar.common.request.productcategory.ProductCategoryRequest;
import com.worldline.eyar.common.response.productcategory.ProductCategoryResponse;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.productcategory.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ProductCategoryController.PRODUCT_CATEGORY_URL_CONTEXT)
public class ProductCategoryController extends CrudController<ProductCategoryRequest, ProductCategoryResponse> {

    public static final String PRODUCT_CATEGORY_URL_CONTEXT = "/product-category";

    @Autowired
    public ProductCategoryController(ProductCategoryService crudService) {
        super(crudService);
    }


}
