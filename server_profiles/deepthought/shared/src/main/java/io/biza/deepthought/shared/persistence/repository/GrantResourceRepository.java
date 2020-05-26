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
package io.biza.deepthought.shared.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import io.biza.deepthought.shared.persistence.model.grant.GrantResourceData;

@Repository
public interface GrantResourceRepository extends JpaRepository<GrantResourceData, UUID>, JpaSpecificationExecutor<GrantResourceData> {
  public Optional<GrantResourceData> findByIdAndGrantId(UUID id, UUID grantId);
  public Optional<GrantResourceData> findByGrantIdAndId(UUID grantId, UUID resourceId);
  public Optional<GrantResourceData> findByGrantIdAndObjectId(UUID grantId, UUID objectId);
  
}
