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
package io.biza.deepthought.shared.payloads.dio.common;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioEmailType;
import io.biza.deepthought.shared.payloads.dio.enumerations.DioSchemeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "A Deep Thought Email Container")
public class DioEmail {

  @JsonProperty("id")
  @NotNull
  @NonNull
  @Schema(description = "Deep Thought Email Identifier",
      defaultValue = "00000000-0000-0000-0000-000000000000")
  @Builder.Default
  UUID id = new UUID(0, 0);
  
  @JsonProperty("schemeType")
  @NotNull
  @NonNull
  @Schema(description = "Scheme Type", defaultValue = "DIO_COMMON")
  @Builder.Default
  DioSchemeType schemeType = DioSchemeType.DIO_COMMON;
  
  @JsonProperty("isPreferred")
  @NotNull
  @Schema(description = "Preferred Number", defaultValue = "false")
  @Builder.Default
  Boolean isPreferred = false;
  
  @JsonProperty("type")
  @Schema(description = "Email Type")
  DioEmailType type;
  
  @JsonProperty("address")
  @Schema(description = "Phone Number in RFC3966 Format", format = "email")
  @Email
  public String address;  
  
}
