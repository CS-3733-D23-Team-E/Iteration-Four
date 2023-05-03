package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class MedicalSuppliesData extends ServiceRequestData {

  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String notes;

  @Getter @Setter private String bandaids;
  @Getter @Setter private String firstAid;
  @Getter @Setter private String stethoscope;
  @Getter @Setter private String scalpel;
  @Getter @Setter private String gloves;
  @Getter @Setter private String syringe;

  public MedicalSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String bandaids,
      String gloves,
      String firstAid,
      String stethoscope,
      String scalpel,
      String syringe,
      String notes) {
    super(requestID, RequestType.MEDICALSUPPLIESDELIVERY, Status.PENDING, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.bandaids = bandaids;
    this.gloves = gloves;
    this.firstAid = firstAid;
    this.stethoscope = stethoscope;
    this.scalpel = scalpel;
    this.syringe = syringe;
    this.notes = notes;
  }

  public MedicalSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String bandaids,
      String gloves,
      String firstAid,
      String stethoscope,
      String scalpel,
      String syringe,
      String notes,
      Status status) {
    super(requestID, RequestType.MEDICALSUPPLIESDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.bandaids = bandaids;
    this.gloves = gloves;
    this.firstAid = firstAid;
    this.stethoscope = stethoscope;
    this.scalpel = scalpel;
    this.syringe = syringe;
    this.notes = notes;
  }

  public MedicalSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String notes,
      Status status) {
    super(requestID, RequestType.MEDICALSUPPLIESDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.bandaids = "0";
    this.gloves = "0";
    this.firstAid = "0";
    this.stethoscope = "0";
    this.scalpel = "0";
    this.syringe = "0";
    this.notes = notes;
  }
}
