package com.worldline.eyar.controller.user;

import com.worldline.eyar.common.request.user.UserRequest;
import com.worldline.eyar.common.response.user.UserResponse;
import com.worldline.eyar.controller.CrudController;
import com.worldline.eyar.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(UserController.USER_URL_CONTEXT)
public class UserController extends CrudController<UserRequest, UserResponse> {

    public static final String USER_URL_CONTEXT = "/user";
    public static final String CHANGE_PASSWORD_URL_CONTEXT = "/change-password/{id}";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @PutMapping(CHANGE_PASSWORD_URL_CONTEXT)
    public ResponseEntity<?> changePassword(@PathVariable(name = "id") @NotNull(message = "id must be determined.") Long id,
                                            @RequestParam(name = "oldPassword") @NotBlank(message = "oldPassword must be determined.") String oldPassword,
                                            @RequestParam(name = "newPassword") @NotBlank(message = "newPassword must be determined.") String newPassword){
        return okResponse(userService.changePassword(id,oldPassword,newPassword));
    }


}
