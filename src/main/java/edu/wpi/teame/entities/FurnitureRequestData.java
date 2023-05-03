package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class FurnitureRequestData extends ServiceRequestData {

  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String notes;

  @Getter @Setter private String deskChair;
  @Getter @Setter private String stool;
  @Getter @Setter private String cot;
  @Getter @Setter private String filingCabinet;
  @Getter @Setter private String table;
  @Getter @Setter private String bed;

  public FurnitureRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String deskChair,
      String stool,
      String cot,
      String filingCabinet,
      String table,
      String bed,
      String notes,
      Status status) {
    super(requestID, RequestType.FURNITUREDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.deskChair = deskChair;
    this.stool = stool;
    this.cot = cot;
    this.filingCabinet = filingCabinet;
    this.table = table;
    this.bed = bed;
    this.notes = notes;
  }

  public FurnitureRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String notes,
      Status status) {
    super(requestID, RequestType.FURNITUREDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.deskChair = "0";
    this.stool = "0";
    this.cot = "0";
    this.filingCabinet = "0";
    this.table = "0";
    this.bed = "0";
    this.notes = notes;
  }
}
