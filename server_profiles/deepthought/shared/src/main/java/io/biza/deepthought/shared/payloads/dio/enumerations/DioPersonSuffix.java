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
package io.biza.deepthought.shared.payloads.dio.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Person Suffixes", enumAsRef = true)
public enum DioPersonSuffix implements LabelValueEnumInterface {
  // @formatter:off
  II("II", "Second"),
  III("III", "Third"),
  IV("IV", "Fourth"),
  AC("AC", "Companion of the Order of Australia"),
  CH("CH", "Companion of Honour"),
  ESQ("ESQ", "Esquire"),
  JNR("JNR", "Junior"),
  MHA("MHA", "Member of the House of Assembly"),
  MLC("MLC", "Member of the Legislative Council"),
  MP("MP", "Member of Parliament"),
  SNR("SNR", "Senior"),
  BM("BM", "Bravery Medal"),
  BEM("BEM", "British Empire Medal"),
  COMDC("COMDC", "Commissioner of Declarations"),
  CV("CV", "Cross of Valour"),
  DFM("DFM", "Distinguished Flying Medal"),
  DSC("DSC", "Distinguished Service Cross"),
  DSM("DSM", "Distinguished Service Medal"),
  GC("GC", "George Cross"),
  KB("KB", "Knight Bachelor"),
  KCMG_DCMG("KCMG_DCMG", "Knight/Dame Commander of the Order of Saint Michael and Saint George"),
  KCB_DCB("KCB_DCB", "Knight/Dame Commander of the Order of the Bath"),
  KBE_DBE("KBE_DBE", "Knight/Dame Commander of the Order of the British Empire"),
  KCVO_DCVO("KCVO_DCVO", "Knight/Dame Commander of the Royal Victorian Order"),
  AK_AD("AK_AD", "Knight/Dame of the Order of Australia"),
  KG("KG", "Knight of the Garter"),
  KT("KT", "Knight of the Thistle"),
  OAM("OAM", "Medal of the Order of Australia – Order of St John"),
  MHR("MHR", "Member of the House of Representatives"),
  MLA("MLA", "Member of the Legislative Assembly"),
  AM("AM", "Member of the Order or Australian"),
  MBE("MBE", "Member of the Order of the British Empire"),
  MC("MC", "Military Cross"),
  OC("OC", "Officer Commanding"),
  AO("AO", "Officer of the Order of Australia"),
  OBE("OBE", "Office of the Order of the British Empire"),
  OM("OM", "Order of Merit"),
  QC("QC", "Queens Council"),
  SC("SC", "Star of Courage"),
  VC("VC", "Victoria Cross");
  // @formatter:on

  private String value;

  private String label;

  DioPersonSuffix(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DioPersonSuffix fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (DioPersonSuffix b : DioPersonSuffix.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of DioPersonPrefix from " + text,
        DioPersonSuffix.class.getSimpleName(), DioPersonSuffix.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}