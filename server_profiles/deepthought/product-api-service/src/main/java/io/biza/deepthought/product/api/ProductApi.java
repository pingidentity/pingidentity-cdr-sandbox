/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package io.biza.deepthought.product.api;

import io.biza.babelfish.cdr.enumerations.BankingProductCategory;
import io.biza.babelfish.cdr.enumerations.BankingProductEffectiveWithAll;
import io.biza.babelfish.cdr.models.responses.ResponseBankingProductByIdV1;
import io.biza.babelfish.cdr.models.responses.ResponseBankingProductByIdV2;
import io.biza.babelfish.cdr.models.responses.ResponseBankingProductListV2;
import io.biza.deepthought.product.api.delegate.ProductApiDelegate;
import io.biza.deepthought.product.api.requests.RequestListProducts;
import io.biza.deepthought.shared.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.UUID;

@Tag(name = Constants.TAG_PRODUCT_NAME, description = Constants.TAG_PRODUCT_DESCRIPTION)
@RequestMapping("/v1/banking/products")
public interface ProductApi {

  default ProductApiDelegate getDelegate() {
    return new ProductApiDelegate() {};
  }

  @Operation(summary = "Obtain a List of Products",
      description = "Obtain a list of products available from the Data Holder")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = Constants.RESPONSE_SUCCESSFUL_LIST,
      content = @Content(schema = @Schema(implementation = ResponseBankingProductListV2.class)))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  default ResponseEntity<ResponseBankingProductListV2> listProducts(
      @Valid @RequestParam(name = "effective", required = false,
          defaultValue = "CURRENT") BankingProductEffectiveWithAll effective,
      @Valid @RequestParam(name = "updated-since", required = false) @DateTimeFormat(
          iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime updatedSince,
      @Valid @RequestParam(name = "brand", required = false) String brand,
      @Valid @RequestParam(name = "product-category",
          required = false) BankingProductCategory productCategory,
      @Valid @RequestParam(name = "page", required = false,
          defaultValue = "1") @Min(1) Integer page,
      @Valid @RequestParam(name = "page-size", required = false,
          defaultValue = "25") Integer pageSize) {

    return getDelegate()
        .listProducts(RequestListProducts.builder().effective(effective).updatedSince(updatedSince)
            .brand(brand).productCategory(productCategory).page(page).pageSize(pageSize).build());
  }

  @Operation(summary = "Get Product Detail",
      description = "Returns details product information based on the specified product identifier")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
              description = Constants.RESPONSE_SUCCESSFUL_READ,
              content = @Content(schema = @Schema(oneOf = {ResponseBankingProductByIdV1.class,
                  ResponseBankingProductByIdV2.class}))),
          @ApiResponse(responseCode = Constants.RESPONSE_CODE_NOT_FOUND,
              description = Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  default ResponseEntity<ResponseBankingProductByIdV2> getProductDetail(
      @NotNull @Valid @PathVariable("productId") UUID productId) {
    return getDelegate().getProductDetail(productId);
  }

}
