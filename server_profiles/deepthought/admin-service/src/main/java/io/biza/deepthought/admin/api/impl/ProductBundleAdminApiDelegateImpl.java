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
package io.biza.deepthought.admin.api.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.deepthought.admin.Labels;
import io.biza.deepthought.admin.api.delegate.ProductBundleAdminApiDelegate;
import io.biza.deepthought.admin.exceptions.ValidationListException;
import io.biza.deepthought.admin.support.DeepThoughtValidator;
import io.biza.deepthought.shared.component.mapper.DeepThoughtMapper;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioExceptionType;
import io.biza.deepthought.shared.payloads.dio.product.DioProduct;
import io.biza.deepthought.shared.payloads.dio.product.DioProductBundle;
import io.biza.deepthought.shared.persistence.model.BrandData;
import io.biza.deepthought.shared.persistence.model.product.ProductBundleData;
import io.biza.deepthought.shared.persistence.model.product.ProductData;
import io.biza.deepthought.shared.persistence.repository.BrandRepository;
import io.biza.deepthought.shared.persistence.repository.ProductBundleRepository;
import io.biza.deepthought.shared.persistence.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class ProductBundleAdminApiDelegateImpl implements ProductBundleAdminApiDelegate {

  @Autowired
  private DeepThoughtMapper mapper;

  @Autowired
  private ProductBundleRepository bundleRepository;

  @Autowired
  private BrandRepository brandRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private Validator validator;

  @Override
  public ResponseEntity<List<DioProductBundle>> listProductBundles(UUID brandId) {
    List<ProductBundleData> productData = bundleRepository.findAllByBrandId(brandId);
    LOG.debug("Listing product bundles and have database result of {}", productData);
    return ResponseEntity.ok(mapper.mapAsList(productData, DioProductBundle.class));
  }

  @Override
  public ResponseEntity<DioProductBundle> getProductBundle(UUID brandId, UUID bundleId) {
    Optional<ProductBundleData> data = bundleRepository.findByIdAndBrandId(bundleId, brandId);

    if (data.isPresent()) {
      LOG.debug("Retrieving product bundle with brand of {}, identifier of {} and content of {}",
          brandId, bundleId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioProductBundle.class));
    } else {
      LOG.warn("Unable to locate product bundle with brand of {} and identifier of {}", brandId,
          bundleId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductBundle> createProductBundle(UUID brandId,
      DioProductBundle createData) throws ValidationListException {
    Optional<BrandData> brand = brandRepository.findById(brandId);

    if (!brand.isPresent()) {
      LOG.warn("Attempted to create product bundle for brand that doesn't exist of brandId {}",
          brandId);
      throw ValidationListException.builder().type(DioExceptionType.INVALID_BRAND)
          .explanation(Labels.ERROR_INVALID_BRAND).build();
    }

    DeepThoughtValidator.validate(validator, createData);

    ProductBundleData data = mapper.map(createData, ProductBundleData.class);
    data.brand(brand.get());
    LOG.debug("Created product bundle on brand {} with content of {}", brandId, data);
    return getProductBundle(brandId, bundleRepository.save(data).id());
  }

  @Override
  public ResponseEntity<Void> deleteProductBundle(UUID brandId, UUID productId) {
    Optional<ProductBundleData> optionalData =
        bundleRepository.findByIdAndBrandId(productId, brandId);

    if (optionalData.isPresent()) {
      LOG.debug("Deleting product bundle for brand {} and product {}", brandId, productId);
      bundleRepository.delete(optionalData.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductBundle> updateProductBundle(UUID brandId, UUID bundleId,
      DioProductBundle updateData) throws ValidationListException {

    DeepThoughtValidator.validate(validator, updateData);

    Optional<ProductBundleData> optionalData =
        bundleRepository.findByIdAndBrandId(bundleId, brandId);

    if (optionalData.isPresent()) {
      ProductBundleData data = optionalData.get();
      mapper.map(updateData, data);
      data.id(bundleId);
      bundleRepository.save(data);
      LOG.debug("Updated product bundle with brand {} and product {} containing data of {}",
          brandId, bundleId, data);
      return getProductBundle(brandId, data.id());
    } else {
      LOG.warn("Attempted to update product bundle that doesn't exist with brand {} and bundle {}",
          brandId, bundleId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioProductBundle> addProductToProductBundle(UUID brandId,
      UUID bundleId, UUID productId) {
    Optional<ProductBundleData> optionalBundleData =
        bundleRepository.findByIdAndBrandId(bundleId, brandId);

    if (optionalBundleData.isPresent()) {
      ProductBundleData bundleData = optionalBundleData.get();

      Optional<ProductData> optionalProductData =
          productRepository.findByIdAndBrandId(productId, brandId);

      if (optionalProductData.isPresent()) {
        ProductData productData = optionalProductData.get();
        bundleData.products().add(productData);
        productData.bundle().add(bundleData);

        bundleRepository.save(bundleData);
        productRepository.save(productData);

        LOG.debug("Added product to bundle with brand {} and bundle {} for product id of {}",
            brandId, bundleId, productId);
        return getProductBundle(brandId, bundleData.id());
      } else {
        LOG.warn(
            "Attempted to add a product to a bundle that doesn't exist with brand {} and bundle {} and product {}",
            brandId, bundleId, productId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } else {
      LOG.warn(
          "Attempted to add a product to a bundle that doesn't exist with brand {} and bundle {} and product {}",
          brandId, bundleId, productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteProductFromProductBundle(UUID brandId,
      UUID bundleId, UUID productId) {
    Optional<ProductBundleData> optionalBundleData =
        bundleRepository.findByIdAndBrandId(bundleId, brandId);

    if (optionalBundleData.isPresent()) {
      ProductBundleData bundleData = optionalBundleData.get();

      Optional<ProductData> optionalProductData =
          productRepository.findByIdAndBrandId(productId, brandId);

      if (optionalProductData.isPresent()) {
        ProductData productData = optionalProductData.get();
        bundleData.products().remove(productData);
        productData.bundle().remove(bundleData);

        bundleRepository.save(bundleData);
        productRepository.save(productData);

        LOG.debug("Removed product from bundle with brand {} and bundle {} for product id of {}",
            brandId, bundleId, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } else {
        LOG.warn(
            "Attempted to delete a product from a bundle that doesn't exist with brand {} and bundle {} and product {}",
            brandId, bundleId, productId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } else {
      LOG.warn(
          "Attempted to delete a product from a bundle that doesn't exist with brand {} and bundle {} and product {}",
          brandId, bundleId, productId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<List<DioProduct>> listProductsForBundle(UUID brandId,
      UUID bundleId) {
    Optional<ProductBundleData> data = bundleRepository.findByIdAndBrandId(bundleId, brandId);

    if (data.isPresent()) {
      LOG.debug(
          "Retrieving products belonging to product bundle with brand of {}, identifier of {}",
          brandId, bundleId);

      Set<ProductData> products = new HashSet<ProductData>();
      if (data.get().products() != null) {
        products.addAll(data.get().products());
      }

      return ResponseEntity.ok(mapper.mapAsList(products, DioProduct.class));
    } else {
      LOG.warn("Unable to locate product bundle with brand of {} and identifier of {}", brandId,
          bundleId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

}
