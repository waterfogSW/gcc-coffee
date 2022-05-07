package com.waterfogsw.gcccoffee.order.controller.dto;

import com.waterfogsw.gcccoffee.order.model.OrderProduct;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record OrderAddRequest(
        @NotEmpty @Email
        String email,
        @NotNull
        String address,
        @NotNull
        String postcode,
        @Valid @NotEmpty
        List<OrderProductAddRequest> orderProducts
) {
}
