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
package io.biza.deepthought.product.api.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductDetailV2;
import io.biza.babelfish.cdr.models.payloads.banking.product.BankingProductV2;
import io.biza.babelfish.cdr.models.responses.ResponseBankingProductByIdV2;
import io.biza.babelfish.cdr.models.responses.ResponseBankingProductListV2;
import io.biza.babelfish.cdr.models.responses.container.ResponseBankingProductListDataV2;
import io.biza.deepthought.product.api.delegate.ProductApiDelegate;
import io.biza.deepthought.product.api.requests.RequestListProducts;
import io.biza.deepthought.product.service.ProductService;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.util.CDRContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class ProductApiDelegateImpl implements ProductApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private ProductService productService;

  @Override
  public ResponseEntity<ResponseBankingProductListV2> listProducts(
      RequestListProducts requestList) {
    
    Page<ProductData> productList = productService.listProducts(requestList);
    
    /**
     * Build response components
     */
    ResponseBankingProductListV2 listResponse = ResponseBankingProductListV2.builder()
        .meta(CDRContainerAttributes.toMetaPaginated(productList))
        .links(CDRContainerAttributes.toLinksPaginated(productList))
        .data(ResponseBankingProductListDataV2.builder()
            .products(mapper.mapAsList(productList.getContent(), BankingProductV2.class)).build())
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

  @Override
  public ResponseEntity<ResponseBankingProductByIdV2> getProductDetail(UUID productId) {
    
    Optional<ProductData> productResult = productService.getProduct(productId);
    if (productResult.isPresent()) {
      ResponseBankingProductByIdV2 productResponse = new ResponseBankingProductByIdV2();
      productResponse.meta(CDRContainerAttributes.toMeta());
      productResponse.links(CDRContainerAttributes.toLinks());
      productResponse.data(mapper.map(productResult.get(), BankingProductDetailV2.class));
      
      return ResponseEntity.ok(productResponse);
      
    } else {
      LOG.warn("Invalid Product Identifier of {} requested, returning 400 Error", productId);
      return ResponseEntity.badRequest().build();
    }
  }

}
