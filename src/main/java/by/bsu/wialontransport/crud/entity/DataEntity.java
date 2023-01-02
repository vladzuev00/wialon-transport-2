package by.bsu.wialontransport.crud.entity;

import by.bsu.wialontransport.crud.entity.converter.LatitudeTypeConverter;
import by.bsu.wialontransport.crud.entity.converter.LongitudeTypeConverter;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static java.util.Arrays.stream;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.InheritanceType.JOINED;

@Entity
@Table(name = "tracker_last_data")
@Inheritance(strategy = JOINED)
@NoArgsConstructor
@Setter
@Getter
@ToString(callSuper = true)
public class DataEntity extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "degrees", column = @Column(name = "latitude_degrees")),
                    @AttributeOverride(name = "minutes", column = @Column(name = "latitude_minutes")),
                    @AttributeOverride(name = "minuteShare", column = @Column(name = "latitude_minute_share")),
                    @AttributeOverride(name = "type", column = @Column(name = "latitude_type"))
            }
    )
    private Latitude latitude;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "degrees", column = @Column(name = "longitude_degrees")),
                    @AttributeOverride(name = "minutes", column = @Column(name = "longitude_minutes")),
                    @AttributeOverride(name = "minuteShare", column = @Column(name = "longitude_minute_share")),
                    @AttributeOverride(name = "type", column = @Column(name = "longitude_type"))
            }
    )
    private Longitude longitude;

    @Column(name = "speed")
    private int speed;

    @Column(name = "course")
    private int course;

    @Column(name = "height")
    private int height;

    @Column(name = "amount_of_satellites")
    private int amountOfSatellites;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tracker_id")
    @ToString.Exclude
    private TrackerEntity tracker;

    @Builder(builderMethodName = "dataEntityBuilder")
    public DataEntity(final Long id, final LocalDate date, final LocalTime time, final Latitude latitude,
                      final Longitude longitude, final int speed, final int course, final int height,
                      final int amountOfSatellites, final TrackerEntity tracker) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.course = course;
        this.height = height;
        this.amountOfSatellites = amountOfSatellites;
        this.tracker = tracker;
    }

    @MappedSuperclass
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static abstract class GeographicCoordinate {
        private int degrees;
        private int minutes;
        private int minuteShare;

        public GeographicCoordinate(final GeographicCoordinate other) {
            this.degrees = other.degrees;
            this.minutes = other.minutes;
            this.minuteShare = other.minuteShare;
        }
    }

    //lat (5544.6025;N)
    //градусы (2 знака) минуты (2 знака).доли минуты (количество знаков кастомное);
    //N-северная широта, S-Южная широта
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Latitude extends GeographicCoordinate {

        @Convert(converter = LatitudeTypeConverter.class)
        private Type type;

        @Builder
        public Latitude(final int degrees, final int minutes, final int minuteShare, final Type type) {
            super(degrees, minutes, minuteShare);
            this.type = type;
        }

        public Latitude(final Latitude other) {
            super(other);
            this.type = other.type;
        }

        public enum Type {
            NOT_DEFINED('-'), NORTH('N'), SOUTH('S');

            private final char value;

            Type(final char value) {
                this.value = value;
            }

            public final char getValue() {
                return this.value;
            }

            public static Type findByValue(final char value) {
                return stream(Type.values())
                        .filter(type -> type.value == value)
                        .findAny()
                        .orElse(NOT_DEFINED);
            }
        }
    }

    //lon (03739.6834;E),
    //градусы (3 знака) минуты (2 знака).доли минуты (количество знаков кастомное);
    //E- восточная долгота, W-западная долгота
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class Longitude extends GeographicCoordinate {

        @Convert(converter = LongitudeTypeConverter.class)
        private Type type;

        @Builder
        public Longitude(final int degrees, final int minutes, final int minuteShare, final Type type) {
            super(degrees, minutes, minuteShare);
            this.type = type;
        }

        public Longitude(final Longitude other) {
            super(other);
            this.type = other.type;
        }

        public enum Type {
            NOT_DEFINED('-'), EAST('E'), WESTERN('W');

            private final char value;

            Type(final char value) {
                this.value = value;
            }

            public final char getValue() {
                return this.value;
            }

            public static Type findByValue(final char value) {
                return stream(Type.values())
                        .filter(type -> type.value == value)
                        .findAny()
                        .orElse(NOT_DEFINED);
            }
        }
    }
}
