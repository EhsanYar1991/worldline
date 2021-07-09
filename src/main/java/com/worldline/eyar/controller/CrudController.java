package com.worldline.eyar.controller;

import com.worldline.eyar.common.GeneralResponse;
import com.worldline.eyar.common.ListWithTotalSizeResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.worldline.eyar.service.ICrudService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
public class CrudController<REQUEST, RESPONSE extends Serializable> extends BaseController {

    private static final String ACTIVATION_URL_CONTEXT = "/activation";

    private final ICrudService crudService;

    public ICrudService getCrudService() {
        return crudService;
    }

    @PostMapping
    public ResponseEntity<GeneralResponse<?>> add(REQUEST request) {
        RESPONSE add = (RESPONSE) getCrudService().add(request);
        return okResponse(add);
    }

    @PutMapping
    public ResponseEntity<GeneralResponse<?>> edit(REQUEST request) {
        RESPONSE edit = (RESPONSE) getCrudService().edit(request);
        return okResponse(edit);
    }

    @DeleteMapping
    public ResponseEntity<GeneralResponse<?>> delete(@RequestParam("id")
                                                         @NotNull(message = "id must be determined.")
                                                         @Min(value = 1 , message = "id must be greater than 0.")
                                                                 Long id) {
        RESPONSE deleted = (RESPONSE) getCrudService().delete(id);
        return okResponse(deleted);
    }

    @GetMapping(ACTIVATION_URL_CONTEXT)
    public ResponseEntity<GeneralResponse<?>> activation(@RequestParam("id") @NotNull(message = "id must be determined.") Long id,
                                                         @RequestParam("active") @NotBlank(message = "active must be determined.") Boolean active) {
        RESPONSE deleted = (RESPONSE) getCrudService().activation(id,active);
        return okResponse(deleted);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse<?>> getOrList(@RequestParam(value = "id", required = false) Long id,
                                                        @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(value = "size", required = false, defaultValue = "15") Integer size) {
        if (id != null) {
            RESPONSE result = (RESPONSE) getCrudService().get(id);
            return okResponse(result);
        } else {
            ListWithTotalSizeResponse<RESPONSE> result = (ListWithTotalSizeResponse<RESPONSE>) getCrudService().list(search, page, size);
            return okResponse(result);
        }
    }

}
