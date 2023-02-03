package by.bsu.wialontransport.protocol.core.service.receivingdatapackage.validator;

import by.bsu.wialontransport.crud.dto.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public final class DataFilter {
    private final DataPropertyValidator propertyValidator;

    public boolean isValid(final Data data) {
        return this.propertyValidator.isValidDateTime(data)
                && this.propertyValidator.isValidAmountOfSatellites(data)
                && this.propertyValidator.isValidDOPParameters(data);
    }

    public boolean isValid(final Data current, final Data previous) {
        return this.isValid(current) && isCorrectOrder(current, previous);
    }

    public boolean isNeedToBeSkipped(final Data data) {
        return !this.propertyValidator.isValidDateTime(data) || !this.propertyValidator.isValidDOPParameters(data);
    }

    public boolean isNeedToBeSkipped(final Data current, final Data previous) {
        return !isCorrectOrder(current, previous)
                || !this.propertyValidator.isValidDateTime(current)
                || !this.propertyValidator.isValidDOPParameters(current);
    }

    private static boolean isCorrectOrder(final Data current, final Data previous) {
        final LocalDateTime dateTimeOfCurrentData = LocalDateTime.of(current.getDate(), current.getTime());
        final LocalDateTime dateTimeOfPreviousData = LocalDateTime.of(previous.getDate(), previous.getTime());
        return dateTimeOfCurrentData.isAfter(dateTimeOfPreviousData);
    }
}