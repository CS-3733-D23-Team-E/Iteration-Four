package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class RoomCleanupData extends ServiceRequestData {

  @Getter @Setter private String room;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String severityLevel;
  @Getter @Setter private String cleaningSupplies;
  @Getter @Setter private String restockSupplies;

  public RoomCleanupData(
      int requestID,
      String room,
      String deliverDate,
      String deliveryTime,
      String assignedStaff,
      String severityLevel,
      String cleaningSupplies,
      String restockSupplies,
      Status status) {
    super(requestID, RequestType.ROOMCLEANING, status, assignedStaff);
    this.room = room;
    this.deliveryDate = deliverDate;
    this.deliveryTime = deliveryTime;
    this.severityLevel = severityLevel;
    this.cleaningSupplies = cleaningSupplies;
    this.restockSupplies = restockSupplies;
  }
}
