package com.worldline.eyar.controller.userrate;

import com.worldline.eyar.common.request.userrate.UserRateRequest;
import com.worldline.eyar.common.response.userrate.UserRateResponse;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.userrate.UserRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserRateController.USER_RATE_URL_CONTEXT)
public class UserRateController extends CrudController<UserRateRequest, UserRateResponse> {

    public static final String USER_RATE_URL_CONTEXT = "/user-rate";

    @Autowired
    public UserRateController(UserRateService crudService) {
        super(crudService);
    }


}
