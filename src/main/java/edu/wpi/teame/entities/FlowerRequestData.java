package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class FlowerRequestData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String rose;
  @Getter @Setter private String tulip;
  @Getter @Setter private String sunflower;
  @Getter @Setter private String muscari;
  @Getter @Setter private String lily;
  @Getter @Setter private String callalily;

  @Getter @Setter private String notes;

  public FlowerRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String rose,
      String tulip,
      String sunflower,
      String muscari,
      String lily,
      String callalily,
      String notes,
      Status status) {
    super(requestID, RequestType.FLOWERDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.rose = rose;
    this.tulip = tulip;
    this.sunflower = sunflower;
    this.muscari = muscari;
    this.lily = lily;
    this.callalily = callalily;
    this.notes = notes;
  }

  public FlowerRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String notes,
      Status status) {
    super(requestID, RequestType.FLOWERDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.rose = "0";
    this.tulip = "0";
    this.sunflower = "0";
    this.muscari = "0";
    this.lily = "0";
    this.callalily = "0";
    this.notes = notes;
  }
}
