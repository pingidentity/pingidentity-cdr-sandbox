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
package io.biza.deepthought.shared.util;

import org.springframework.data.domain.Page;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.biza.babelfish.cdr.models.payloads.LinksPaginatedV1;
import io.biza.babelfish.cdr.models.payloads.LinksV1;
import io.biza.babelfish.cdr.models.payloads.MetaPaginatedV1;
import io.biza.babelfish.cdr.models.payloads.MetaV1;

public class CDRContainerAttributes {
  
  public static MetaPaginatedV1 toMetaPaginated(Page<?> inputPage) {
    return MetaPaginatedV1.builder().totalPages(inputPage.getTotalPages())
        .totalRecords(Long.valueOf(inputPage.getTotalElements()).intValue()).build();
  }

  public static MetaV1 toMeta() {
    return new MetaV1();
  }

  public static LinksV1 toLinks() {
    LinksV1 links = new LinksV1();
    ServletUriComponentsBuilder uriComponents = ServletUriComponentsBuilder.fromCurrentRequest();
    links.self(uriComponents.build().toUri());
    return links;
  }

  public static LinksPaginatedV1 toLinksPaginated(Page<?> inputPage) {
    LinksPaginatedV1 links = LinksPaginatedV1.builder().build();
    ServletUriComponentsBuilder uriComponents = ServletUriComponentsBuilder.fromCurrentRequest();

    links.self(uriComponents.build().toUri());

    if (!inputPage.isFirst()) {
      links.first(uriComponents.replaceQueryParam("page", 1)
          .replaceQueryParam("page-size", inputPage.getNumberOfElements()).build().toUri());
    }

    if (!inputPage.isLast()) {
      links.last(uriComponents.replaceQueryParam("page", inputPage.getTotalPages() + 1)
          .replaceQueryParam("page-size", inputPage.getNumberOfElements()).build().toUri());
    }

    if (inputPage.hasPrevious()) {
      links.prev(
          uriComponents.replaceQueryParam("page", inputPage.previousPageable().getPageNumber() + 1)
              .replaceQueryParam("page-size", inputPage.getNumberOfElements()).build().toUri());
    }

    if (inputPage.hasNext()) {
      links.next(
          uriComponents.replaceQueryParam("page", inputPage.nextPageable().getPageNumber() + 1)
              .replaceQueryParam("page-size", inputPage.getNumberOfElements()).build().toUri());
    }

    return links;
  }

}
