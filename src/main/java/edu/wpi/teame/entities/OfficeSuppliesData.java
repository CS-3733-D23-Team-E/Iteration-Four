package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class OfficeSuppliesData extends ServiceRequestData {

  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String pen;
  @Getter @Setter private String pencil;
  @Getter @Setter private String ruler;
  @Getter @Setter private String tape;
  @Getter @Setter private String holepuncher;
  @Getter @Setter private String stapler;

  @Getter @Setter private String notes;

  public OfficeSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String pen,
      String pencil,
      String ruler,
      String tape,
      String holepuncher,
      String stapler,
      String notes,
      Status status) {
    super(requestID, RequestType.OFFICESUPPLIESDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.pen = pen;
    this.pencil = pencil;
    this.ruler = ruler;
    this.tape = tape;
    this.holepuncher = holepuncher;
    this.stapler = stapler;
    this.notes = notes;
  }

  public OfficeSuppliesData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String assignedStaff,
      String notes,
      Status status) {
    super(requestID, RequestType.OFFICESUPPLIESDELIVERY, status, assignedStaff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.pen = "0";
    this.pencil = "0";
    this.ruler = "0";
    this.tape = "0";
    this.holepuncher = "0";
    this.stapler = "0";
    this.notes = notes;
  }
}
