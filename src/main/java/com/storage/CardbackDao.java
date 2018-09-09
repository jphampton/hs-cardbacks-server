package com.storage;


import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.models.Month;
import com.models.Cardback;

import java.util.logging.Logger;

public class CardbackDao {
  private static final Logger log = Logger.getLogger(CardbackDao.class.getName());
  private DatastoreService datastore;

  public CardbackDao() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  public void makeTestEntity() {
    Entity example = new Entity("Cardback","120");
    example.setProperty("year",2018);
    example.setProperty("month",8);
    example.setProperty("img","https://storage.googleapis.com/hearthstonecardbacks-215221.appspot.com/Dr.%20Boom\'s%20Lab.jpg");
    datastore.put(example);
  }
  public Cardback getMonthYear(Month month, int year) {
    Entity e = queryMonthYear(month, year);
    if(e==null){
      log.warning(String.format("No cardback found for %s %d", month, year));
      return new Cardback.Builder().build();
    }
    Key k = e.getKey();
    String name = k.getName();
    int id = Integer.parseInt(name);
    String gamepediaName = (String) e.getProperty(Constants.GAMEPEDIANAME);
    String mashapeName = (String) e.getProperty(Constants.MASHAPENAME);
    String imgURL = (String) e.getProperty(Constants.IMG);
    String imgAnimatedURL = (String) e.getProperty(Constants.IMGANIMATED);
    Month m = Month.values()[(int) (long) e.getProperty(Constants.MONTH)];
    int y = (int) (long) e.getProperty(Constants.YEAR);

    Cardback.Builder builder = new Cardback.Builder();
    builder.setGamepediaName(gamepediaName)
        .setId(id)
        .setImgAnimatedURL(imgAnimatedURL)
        .setImgURL(imgURL)
        .setMashapeName(mashapeName)
        .setMonth(m)
        .setYear(y);
    return builder.build();
  }

  private Entity queryMonthYear(Month month, int year) {
    Filter monthEquals = new FilterPredicate(Constants.MONTH, FilterOperator.EQUAL, month.ordinal());
    Filter yearEquals = new FilterPredicate(Constants.YEAR, FilterOperator.EQUAL, year);
    CompositeFilter monthYearFilter = CompositeFilterOperator.and(monthEquals, yearEquals);
    log.warning(String.format("Month and year: %d %d", month.ordinal(), year));
    Query q = new Query(Constants.CARDBACK_KIND).setFilter(monthYearFilter);
    PreparedQuery pq = datastore.prepare(q);
    return pq.asSingleEntity();
  }

}