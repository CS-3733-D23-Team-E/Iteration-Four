package edu.wpi.teame.utilities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.SignageComponentData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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

  //  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(String kioskName, Date date)
  // {
  //    return SQLRepo.INSTANCE.getSignageList().stream()
  //        .filter(
  //            direction ->
  //                (direction.getKiosk_location().equals(kioskName)
  //                    && afterDate(direction, date) <= 0))
  //        .toList();
  //  }

  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(String kioskName) {
    return findAllDirectionsOnDateAtKiosk(kioskName, today);
  }

  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(
      String kioskName, LocalDate date) {
    return findAllDirectionsOnDateAtKiosk(kioskName, toDateFromLocal(date));
  }

  public List<String> findAllCurrentLocationsWithDirectionsAtKiosk(String kioskName) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(direction -> (direction.getKiosk_location().equals(kioskName)))
        .map(SignageComponentData::getLocationNames)
        .toList();
  }

  public List<SignageComponentData> findAllDirectionsOnDateAtKiosk(String kioskName, Date date) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(
            direction ->
                (direction.getKiosk_location().equals(kioskName)
                    && afterDate(direction, date) <= 0))
        .map(SignageComponentData::getLocationNames)
        .map(name -> findMostRecentDirectionForName(name, kioskName, date))
        .distinct()
        .toList();
  }

  private SignageComponentData findMostRecentDirectionForName(
      String name, String kiosk, Date date) {
    return SQLRepo.INSTANCE.getSignageList().stream()
        .filter(
            direction ->
                (direction.getLocationNames().equals(name)
                    && direction.getKiosk_location().equals(kiosk)
                    && afterDate(direction, date) <= 0))
        .sorted(
            new Comparator<SignageComponentData>() {
              @Override
              public int compare(SignageComponentData o1, SignageComponentData o2) {
                try {
                  return formatter.parse(o1.getDate()).compareTo(formatter.parse(o2.getDate()));
                } catch (ParseException e) {
                  System.out.println(e);
                  return 0;
                }
              }
            })
        .toList()
        .get(0);
  }
}
