package com.waterfogsw.gcccoffee.order.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public record OrderAddRequest(
        @NotEmpty(message = "Email should not be empty")
        @Email(message = "Invalid email format")
        String email,

        @NotNull(message = "Address should not be empty")
        String address,

        @NotNull(message = "Postcode should not be empty")
        String postcode,

        @Valid
        @NotEmpty(message = "Order Products should not be empty")
        List<OrderProductAddRequest> orderProducts
) {
}
