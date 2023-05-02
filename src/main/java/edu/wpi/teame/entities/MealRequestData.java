package edu.wpi.teame.entities;

import lombok.Getter;
import lombok.Setter;

public class MealRequestData extends ServiceRequestData {
  @Getter @Setter private String name;
  @Getter @Setter private String room;
  @Getter @Setter private String deliveryTime;
  @Getter @Setter private String deliveryDate;
  @Getter @Setter private String cheeseburgers;
  @Getter @Setter private String sandwich;
  @Getter @Setter private String salad;
  @Getter @Setter private String nuggets;
  @Getter @Setter private String orangejuice;
  @Getter @Setter private String water;

  @Getter @Setter private String notes;

  public MealRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String cheeseburgers,
      String sandwich,
      String salad,
      String nuggets,
      String orangejuice,
      String water,
      String notes,
      Status status) {
    super(requestID, RequestType.MEALDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.cheeseburgers = cheeseburgers;
    this.sandwich = sandwich;
    this.salad = salad;
    this.nuggets = nuggets;
    this.orangejuice = orangejuice;
    this.water = water;
    this.notes = notes;
  }

  public MealRequestData(
      int requestID,
      String name,
      String room,
      String deliveryDate,
      String deliveryTime,
      String staff,
      String notes,
      Status status) {
    super(requestID, RequestType.MEALDELIVERY, status, staff);
    this.name = name;
    this.room = room;
    this.deliveryDate = deliveryDate;
    this.deliveryTime = deliveryTime;
    this.cheeseburgers = "0";
    this.sandwich = "0";
    this.salad = "0";
    this.nuggets = "0";
    this.orangejuice = "0";
    this.water = "0";
    this.notes = notes;
  }
}
