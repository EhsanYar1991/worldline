package com.worldline.eyar.controller;

import com.worldline.eyar.common.GeneralResponse;
import com.worldline.eyar.common.ListWithTotalSizeResponse;
import com.worldline.eyar.service.ICrudService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
public class CrudController<REQUEST, RESPONSE extends Serializable> extends BaseController {

    private static final String ACTIVATION_URL_CONTEXT = "/activation";
    private static final String ID_PARAM = "id";
    private static final String SEARCH_PARAM = "search";
    private static final String PAGE_NUMBER_PARAM = "username";
    private static final String PAGE_SIZE_PARAM = "page";
    private static final String ACTIVE_PARAM = "size";

    private static final String ALL_AUTHORITY = "hasAnyAuthority('ADMIN','USER')";
    private static final String ADMIN_AUTHORITY = "hasAuthority('ADMIN')";



    private final ICrudService<REQUEST,RESPONSE,?> crudService;

    public ICrudService<REQUEST,RESPONSE,?> getCrudService() {
        return crudService;
    }

    @PreAuthorize(ALL_AUTHORITY)
    @PostMapping
    public ResponseEntity<GeneralResponse<?>> add(@Valid REQUEST request) {
        RESPONSE add = getCrudService().add(request);
        return okResponse(add);
    }

    @PreAuthorize(ALL_AUTHORITY)
    @PutMapping
    public ResponseEntity<GeneralResponse<?>> edit(@Valid REQUEST request) {
        RESPONSE edit = getCrudService().edit(request);
        return okResponse(edit);
    }

    @PreAuthorize(ADMIN_AUTHORITY)
    @DeleteMapping
    public ResponseEntity<GeneralResponse<?>> delete(@RequestParam(ID_PARAM)
                                                         @NotNull(message = "id must be determined.")
                                                         @Min(value = 1 , message = "id must be greater than 0.")
                                                                 Long id) {
        RESPONSE deleted = getCrudService().delete(id);
        return okResponse(deleted);
    }

    @PreAuthorize(ADMIN_AUTHORITY)
    @GetMapping(ACTIVATION_URL_CONTEXT)
    public ResponseEntity<GeneralResponse<?>> activation(@RequestParam(ID_PARAM) @NotNull(message = "id must be determined.") Long id,
                                                         @RequestParam(ACTIVE_PARAM) @NotBlank(message = "active must be determined.") Boolean active) {
        RESPONSE deleted = getCrudService().activation(id,active);
        return okResponse(deleted);
    }

    @PreAuthorize(ALL_AUTHORITY)
    @GetMapping("/")
    public ResponseEntity<GeneralResponse<?>> getOrList(@RequestParam(value = ID_PARAM, required = false) Long id,
                                                        @RequestParam(value = SEARCH_PARAM, required = false, defaultValue = "") String search,
                                                        @RequestParam(value = PAGE_NUMBER_PARAM, required = false, defaultValue = "0") Integer page,
                                                        @RequestParam(value = PAGE_SIZE_PARAM, required = false, defaultValue = "15") Integer size) {
        if (id != null) {
            RESPONSE result = getCrudService().get(id);
            return okResponse(result);
        } else {
            ListWithTotalSizeResponse<RESPONSE> result = getCrudService().list(search, page, size);
            return okResponse(result);
        }
    }

}
