package edu.wpi.teame.utilities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.SignageComponentData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class SignageUtilities {
  Date today;
  SimpleDateFormat formatter;

  public SignageUtilities() {
    today = new Date();
    formatter = new SimpleDateFormat("yyyy-MM-dd");
  };

  public Date toDateFromLocal(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  public int afterDate(SignageComponentData direction, LocalDate localDate) {
    // cast localDate to Date
    return afterDate(direction, toDateFromLocal(localDate));
  }

  public int afterDate(SignageComponentData direction) {
    return afterDate(direction, today);
  }

  public int afterDate(SignageComponentData sign, Date day) {
    Date directionDate;
    try {
      directionDate = formatter.parse(sign.getDate());
    } catch (ParseException e) {
      directionDate = new Date();
      System.out.println(e);
    }

    if (formatter.format(day).equals(formatter.format(directionDate))) return 0;

    return directionDate
        .toInstant()
        .truncatedTo(ChronoUnit.DAYS)
        .compareTo(day.toInstant().truncatedTo(ChronoUnit.DAYS));
  }

  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(String kioskName, Date date) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(
            direction ->
                (direction.getKiosk_location().equals(kioskName)
                    && afterDate(direction, date) == 0))
        .toList();
  }

  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(String kioskName) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(
            direction ->
                (direction.getKiosk_location().equals(kioskName) && afterDate(direction) == 0))
        .toList();
  }

  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(
      String kioskName, LocalDate date) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(
            direction ->
                (direction.getKiosk_location().equals(kioskName)
                    && afterDate(direction, date) == 0))
        .toList();
  }

  public List<String> findAllCurrentLocationsWithDirectionsAtKiosk(String kioskName) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(direction -> (direction.getKiosk_location().equals(kioskName)))
        .map(SignageComponentData::getLocationNames)
        .toList();
  }

  public void deleteAllForASpecificDayInTheDatabase(LocalDate date, String kioskName) {
    for (SignageComponentData direction : SQLRepo.INSTANCE.getSignageList()) {
      System.out.println(Math.abs(date.until(LocalDate.parse(direction.getDate())).getDays()));
    }
    for (SignageComponentData data :
        SQLRepo.INSTANCE.getSignageList().stream()
            .filter(
                direction ->
                    (afterDate(direction, date) == 0)
                        && direction.getKiosk_location().equals(kioskName))
            .toList()) {

      SQLRepo.INSTANCE.deleteSignage(data);
    }
  }
}
