package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.crud.dto.ExtendedData.extendedDataBuilder;

@Component
public final class ExtendedDataParser {

    public ExtendedData parse(final String source) {
        final DataComponentsParser parser = new DataComponentsParser(source);
        final LocalDateTime dateTime = parser.parseDateTime();
        return extendedDataBuilder()
                .date(dateTime.toLocalDate())
                .time(dateTime.toLocalTime())
                .latitude(parser.parseLatitude())
                .longitude(parser.parseLongitude())
                .speed(parser.parseSpeed())
                .course(parser.parseCourse())
                .height(parser.parseAltitude())
                .amountOfSatellites(parser.parseAmountSatellites())
                .reductionPrecision(parser.parseReductionPrecision())
                .inputs(parser.parseInputs())
                .outputs(parser.parseOutputs())
                .analogInputs(parser.parseAnalogInputs())
                .driverKeyCode(parser.parseDriverKeyCode())
                .build();
    }
}