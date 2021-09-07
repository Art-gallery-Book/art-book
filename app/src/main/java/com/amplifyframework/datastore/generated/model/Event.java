package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Event type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Events")
public final class Event implements Model {
  public static final QueryField ID = field("Event", "id");
  public static final QueryField NAME = field("Event", "name");
  public static final QueryField DESCRIPTION = field("Event", "description");
  public static final QueryField DATE = field("Event", "date");
  public static final QueryField LAT = field("Event", "lat");
  public static final QueryField LON = field("Event", "lon");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String", isRequired = true) String description;
  private final @ModelField(targetType="String", isRequired = true) String date;
  private final @ModelField(targetType="Float", isRequired = true) Double lat;
  private final @ModelField(targetType="Float", isRequired = true) Double lon;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getDate() {
      return date;
  }
  
  public Double getLat() {
      return lat;
  }
  
  public Double getLon() {
      return lon;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Event(String id, String name, String description, String date, Double lat, Double lon) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.date = date;
    this.lat = lat;
    this.lon = lon;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Event event = (Event) obj;
      return ObjectsCompat.equals(getId(), event.getId()) &&
              ObjectsCompat.equals(getName(), event.getName()) &&
              ObjectsCompat.equals(getDescription(), event.getDescription()) &&
              ObjectsCompat.equals(getDate(), event.getDate()) &&
              ObjectsCompat.equals(getLat(), event.getLat()) &&
              ObjectsCompat.equals(getLon(), event.getLon()) &&
              ObjectsCompat.equals(getCreatedAt(), event.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), event.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getDescription())
      .append(getDate())
      .append(getLat())
      .append(getLon())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Event {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("date=" + String.valueOf(getDate()) + ", ")
      .append("lat=" + String.valueOf(getLat()) + ", ")
      .append("lon=" + String.valueOf(getLon()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Event justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Event(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      description,
      date,
      lat,
      lon);
  }
  public interface NameStep {
    DescriptionStep name(String name);
  }
  

  public interface DescriptionStep {
    DateStep description(String description);
  }
  

  public interface DateStep {
    LatStep date(String date);
  }
  

  public interface LatStep {
    LonStep lat(Double lat);
  }
  

  public interface LonStep {
    BuildStep lon(Double lon);
  }
  

  public interface BuildStep {
    Event build();
    BuildStep id(String id) throws IllegalArgumentException;
  }
  

  public static class Builder implements NameStep, DescriptionStep, DateStep, LatStep, LonStep, BuildStep {
    private String id;
    private String name;
    private String description;
    private String date;
    private Double lat;
    private Double lon;
    @Override
     public Event build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Event(
          id,
          name,
          description,
          date,
          lat,
          lon);
    }
    
    @Override
     public DescriptionStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public DateStep description(String description) {
        Objects.requireNonNull(description);
        this.description = description;
        return this;
    }
    
    @Override
     public LatStep date(String date) {
        Objects.requireNonNull(date);
        this.date = date;
        return this;
    }
    
    @Override
     public LonStep lat(Double lat) {
        Objects.requireNonNull(lat);
        this.lat = lat;
        return this;
    }
    
    @Override
     public BuildStep lon(Double lon) {
        Objects.requireNonNull(lon);
        this.lon = lon;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, String description, String date, Double lat, Double lon) {
      super.id(id);
      super.name(name)
        .description(description)
        .date(date)
        .lat(lat)
        .lon(lon);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder date(String date) {
      return (CopyOfBuilder) super.date(date);
    }
    
    @Override
     public CopyOfBuilder lat(Double lat) {
      return (CopyOfBuilder) super.lat(lat);
    }
    
    @Override
     public CopyOfBuilder lon(Double lon) {
      return (CopyOfBuilder) super.lon(lon);
    }
  }
  
}
