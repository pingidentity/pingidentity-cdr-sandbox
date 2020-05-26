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
package io.biza.deepthought.shared.component.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import io.biza.babelfish.cdr.enumerations.BankingAccountStatusWithAll;
import io.biza.deepthought.shared.exception.InvalidSubjectException;
import io.biza.deepthought.shared.exception.NotFoundException;
import io.biza.deepthought.shared.payloads.requests.RequestListAccounts;
import io.biza.deepthought.shared.persistence.model.grant.GrantCustomerAccountData;
import io.biza.deepthought.shared.persistence.model.grant.GrantData;
import io.biza.deepthought.shared.persistence.model.grant.GrantResourceData;
import io.biza.deepthought.shared.persistence.repository.GrantCustomerAccountRepository;
import io.biza.deepthought.shared.persistence.repository.GrantRepository;
import io.biza.deepthought.shared.persistence.repository.GrantResourceRepository;
import io.biza.deepthought.shared.persistence.specification.GrantCustomerAccountSpecifications;
import io.biza.deepthought.shared.util.UserPrincipalUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GrantService {

  @Autowired
  private GrantCustomerAccountRepository customerAccountRepository;

  @Autowired
  private GrantResourceRepository resourceRepository;

  @Autowired
  private GrantRepository grantRepository;


  public UUID getObjectIdByResourceId(UUID id) throws NotFoundException, InvalidSubjectException {
    Optional<GrantResourceData> resourceData = resourceRepository.findById(id);

    if (resourceData.isPresent()) {
      return resourceData.get().objectId();
    } else {
      throw new NotFoundException("Requested Grant Resource not found");
    }
  }

  public UUID getObjectIdByAccountIdAndResourceId(UUID accountId, UUID id)
      throws NotFoundException {
    GrantCustomerAccountData account = getGrantAccount(accountId);
    Optional<GrantResourceData> resourceData =
        resourceRepository.findByGrantIdAndId(account.grant().id(), id);

    if (resourceData.isPresent()) {
      return resourceData.get().objectId();
    } else {
      throw new NotFoundException("Requested Grant Resource not found");
    }
  }

  public UUID getOrCreateResourceIdByGrantIdAndObjectId(UUID grantId, UUID objectId)
      throws NotFoundException {
    Optional<GrantResourceData> resourceData =
        resourceRepository.findByGrantIdAndObjectId(grantId, objectId);

    if (resourceData.isPresent()) {
      return resourceData.get().id();
    } else {
      Optional<GrantData> grant = grantRepository.findById(grantId);
      if (grant.isPresent()) {
        GrantResourceData grantResource = resourceRepository
            .save(GrantResourceData.builder().objectId(objectId).grant(grant.get()).build());
        return grantResource.id();
      } else {
        throw new NotFoundException("Requested Grant not found");
      }
    }
  }

  public UUID getOrCreateResourceIdByAccountIdAndObjectId(UUID accountId, UUID objectId)
      throws NotFoundException {
    GrantCustomerAccountData account = getGrantAccount(accountId);
    Optional<GrantResourceData> resourceData =
        resourceRepository.findByGrantIdAndObjectId(account.grant().id(), objectId);

    if (resourceData.isPresent()) {
      return resourceData.get().id();
    } else {
      GrantResourceData grantResource = resourceRepository
          .save(GrantResourceData.builder().objectId(objectId).grant(account.grant()).build());
      return grantResource.id();
    }
  }

  public List<GrantCustomerAccountData> listGrantAccountByIds(UUID... accountIds) {
    return customerAccountRepository
        .findAll(GrantCustomerAccountSpecifications.accountIds(accountIds)
            .and(GrantCustomerAccountSpecifications.expiryBefore(OffsetDateTime.now()))
            .and(GrantCustomerAccountSpecifications.subject(UserPrincipalUtil.getSubject())));
  }

  public Page<GrantCustomerAccountData> listPaginatedGrantAccountByIds(Integer page,
      Integer pageSize, UUID... accountIds) {
    return customerAccountRepository.findAll(
        GrantCustomerAccountSpecifications.accountIds(accountIds)
            .and(GrantCustomerAccountSpecifications.expiryBefore(OffsetDateTime.now()))
            .and(GrantCustomerAccountSpecifications.subject(UserPrincipalUtil.getSubject())),
        PageRequest.of(page - 1, pageSize));
  }

  public GrantCustomerAccountData getGrantAccount(UUID accountId) throws NotFoundException {
    LOG.debug("Retrieving account with grant identifier of {}", accountId);

    Specification<GrantCustomerAccountData> filterSpecification =
        GrantCustomerAccountSpecifications.accountId(accountId)
            .and(GrantCustomerAccountSpecifications.expiryBefore(OffsetDateTime.now()))
            .and(GrantCustomerAccountSpecifications.subject(UserPrincipalUtil.getSubject()));

    List<GrantCustomerAccountData> grantList =
        customerAccountRepository.findAll(filterSpecification);

    // Despite being a list the accountId filter should be exclusive so we just pull the first
    // record of the first account returned (there should only be zero or one tuple)
    if (grantList != null && grantList.size() > 0) {
      return grantList.iterator().next();
    } else {
      throw new NotFoundException("Account " + accountId + " cannot be found");
    }
  }

  private Specification<GrantCustomerAccountData> generateListAccountsSpecification(
      RequestListAccounts requestList) {
    Specification<GrantCustomerAccountData> filterSpecifications = Specification.where(null);

    /**
     * Subject and optionally ownership status
     */
    if (requestList.isOwned() != null) {
      filterSpecifications = filterSpecifications.and(GrantCustomerAccountSpecifications
          .ownerStatus(UserPrincipalUtil.getSubject(), requestList.isOwned()));
    } else {
      filterSpecifications = filterSpecifications
          .and(GrantCustomerAccountSpecifications.subject(UserPrincipalUtil.getSubject()));
    }

    /**
     * Account status
     */
    if (List.of(BankingAccountStatusWithAll.OPEN, BankingAccountStatusWithAll.CLOSED)
        .contains(requestList.accountStatus())) {
      filterSpecifications = filterSpecifications
          .and(GrantCustomerAccountSpecifications.accountStatus(requestList.accountStatus()));
    }

    /**
     * Product Category
     */
    if (requestList.productCategory() != null) {
      filterSpecifications = filterSpecifications
          .and(GrantCustomerAccountSpecifications.productCategory(requestList.productCategory()));
    }

    return filterSpecifications;
  }

  public Page<GrantCustomerAccountData> listGrantAccountsPaginated(
      RequestListAccounts requestList) {
    LOG.debug("Retrieving a paginated list of accounts with input request of {}", requestList);

    return customerAccountRepository.findAll(generateListAccountsSpecification(requestList),
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));
  }

  public List<GrantCustomerAccountData> listGrantAccounts(RequestListAccounts requestList) {
    LOG.debug("Retrieving a list of accounts with input request of {}", requestList);
    return customerAccountRepository.findAll(generateListAccountsSpecification(requestList));
  }
}
