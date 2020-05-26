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

@Schema(description = "Person Prefixes", enumAsRef = true)
public enum DioPersonPrefix implements LabelValueEnumInterface {
  // @formatter:off
  // Source from ABR Data Dictionary: https://www.abr.gov.au/government-agencies/accessing-abr-data/abr-data-dictionary/appendix-b-name-title
  MISS("MISS", "Miss"),
  MR("MR", "Mister"),
  MRS("MRS", "Mrs"),
  MS("MS", "Ms"),
  LORD("LORD", "Lord"),
  LADY("LADY", "Lady"),
  DAME("DAME", "Dame"),
  BR("BR", "Brother"),
  SR("SR", "Sister"), 
  LT2("LT2", "Second Lieutenant"),
  AB("AB", "Able Seaman"),
  ABBOTT("ABBOTT", "Abbott"),
  AC("AC", "Aircraftman"),
  ACM("ACM", "Air Chief Marshal"),
  ACW("ACW", "Aircraftwoman"),
  ADML("ADML", "Admiral"),
  AIRCDRE("AIRCDRE", "Air Commodore"),
  ALD("ALD", "Alderman"),
  AM("AM", "Air Marshal"),
  AMBSR("AMBSR", "Ambassador"),
  ARCHBISHOP("ARCHBISHOP", "Archbishop"),
  ARCHDEACON("ARCHDEACON", "Archdeacon"),
  ASSOC_PROF("ASSOC_PROF", "Associate Professor"),
  AVM("AVM", "Air Vice Marshal"),
  BARON("BARON", "Baron"),
  BARONESS("BARONESS", "Baroness"),
  BDR("BDR", "Bombardier"),
  BISHOP("BISHOP", "Bishop"),
  BRIG("BRIG", "Brigadier"),
  CANON("CANON", "Canon"),
  CAPT("CAPT", "Captain (Army)"),
  CARDNL("CARDNL", "Cardinal"),
  CDRE("CDRE", "Commodore"),
  CDT("CDT", "Cadet"),
  CHAP("CHAP", "Chaplin"),
  CMDR("CMDR", "Commander"),
  CMM("CMM", "Commissioner"),
  COL("COL", "Colonel"),
  CONST("CONST", "Constable"),
  CONSUL("CONSUL", "Consul"),
  COUNT("COUNT", "Count"),
  COUNTESS("COUNTESS", "Countess"),
  CPL("CPL", "Corporal"),
  CPO("CPO", "Chief Petty Officer"),
  DEACON("DEACON", "Deacon"),
  DEACONESS("DEACONESS", "Deaconess"),
  DEAN("DEAN", "Dean"),
  DEPUTY_SUPT("DEPUTY_SUPT", "Deputy Superintendent"),
  DIRECTOR("DIRECTOR", "Director"),
  DR("DR", "Doctor"),
  EARL("EARL", "Earl"),
  ENGR("ENGR", "Engineer"),
  FLGOFF("FLGOFF", "Flying Officer"),
  FLTLT("FLTLT", "Flight Lieutenant"),
  FR("FR", "Farther"),
  FSGT("FSGT", "Flight Sergeant"),
  GEN("GEN", "General"),
  GP_CAPT("GP CAPT", "Group Captain"),
  HON("HON", "Honourable"),
  JUDGE("JUDGE", "Judge"),
  JUSTICE("JUSTICE", "Justice"),
  LAC("LAC", "Leading Aircraftman"),
  LACW("LACW", "Leading Aircraftwoman"),
  LBDR("LBDR", "Lance Bombardier"),
  LCPL("LCPL", "Lance Corporal"),
  LEUT("LEUT", "Lieutenant (Navy)"),
  LS("LS", "Leading Seaman"),
  LT("LT", "Lieutenant (Army)"),
  LTCOL("LTCOL", "Lieutenant Colonel"),
  LTGEN("LTGEN", "Lieutenant General"),
  MADAM("MADAM", "Madam"),
  MADAME("MADAME", "Madame"),
  MAJ("MAJ", "Major"),
  MAJGEN("MAJGEN", "Major General"),
  MAYOR("MAYOR", "Mayor"),
  MAYORESS("MAYORESS", "Mayoress"),
  MGR("MGR", "Manager"),
  MIDN("MIDN", "Midshipman"),
  MON("MON", "Monsignor"),
  MOST_REV("MOST_REV", "Most Reverend"),
  MSTR("MSTR", "Master"),
  MTHR("MTHR", "Mother"),
  NURSE("NURSE", "Nurse"),
  PASTOR("PASTOR", "Pastor"),
  PLTOFF("PLTOFF", "Pilot Officer"),
  PO("PO", "Petty Officer"),
  PROF("PROF", "Professor"),
  PTE("PTE", "Private"),
  RABBI("RABBI", "Rabbi"),
  RADM("RADM", "Rear Admiral"),
  RECTOR("RECTOR", "Rector"),
  REV("REV", "Reverend"),
  RT_REV("RT REV", "Right Reverend"),
  RTHON("RTHON", "Right Honourable"),
  SBLT("SBLT", "Sub Lieutenant"),
  SCDT("SCDT", "Staff Cadet"),
  SEN("SEN", "Senator"),
  SGT("SGT", "Sergeant"),
  SIR("SIR", "Sir"),
  SISTER_SUP("SISTER_SUP", "Sister Superior"),
  SMN("SMN", "Seaman"),
  SQNLDR("SQNLDR", "Squadron Leader"),
  SSGT("SSGT", "Staff Sergeant"),
  SUPT("SUPT", "Superintendent"),
  SWAMI("SWAMI", "Swami"),
  VADM("VADM", "Vice Admiral"),
  VCE_CMNDR("VCE_CMNDR", "Vice Commander"),
  VISCOUNT("VISCOUNT", "Viscount"),
  WCDR("WCDR", "Wing Commander"),
  WO("WO", "Warrant Officer (Navy)"),
  WO1("WO1", "Warrant Officer Class 1"),
  WO2("WO2", "Warrant Officer Class 2"),
  WOFF("WOFF", "Warrant Officer (Air Force)"),
  CAPT_RAN("CAPT_RAN", "Captain (Navy)"),
  GOV("GOV", "Governor"),
  LCDR("LCDR", "Lieutenant Commander"),
  LTGOV("LTGOV", "Lieutenant Governor"),
  OCDT("OCDT", "Officer Cadet"),
  RSM("RSM", "Regimental Sergeant Major"),
  RSM_A("RSM_A", "Regimental Sergeant Major of the Army"),
  SNR("SNR", "Senior"),
  SM("SM", "Station Master"),
  WOFF_AF("WOFF_AF", "Warrant Officer of the Air Force"),
  WO_N("WO_N", "Warrant Officer of the Navy");
  // @formatter:on

  private String value;

  private String label;

  DioPersonPrefix(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DioPersonPrefix fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (DioPersonPrefix b : DioPersonPrefix.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of DioPersonPrefix from " + text,
        DioPersonPrefix.class.getSimpleName(), DioPersonPrefix.values(), text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}
