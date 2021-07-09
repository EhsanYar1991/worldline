package com.worldline.eyar.controller.user;

import com.worldline.eyar.common.request.user.UserRequest;
import com.worldline.eyar.common.response.UserResponse;
import com.worldline.eyar.controller.BaseController;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.ICrudService;
import com.worldline.eyar.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(UserController.USER_URL_CONTEXT)
public class UserController extends CrudController<UserRequest, UserResponse> {

    public static final String USER_URL_CONTEXT = "/user";

    @Autowired
    public UserController(UserService crudService) {
        super(crudService);
    }




}
