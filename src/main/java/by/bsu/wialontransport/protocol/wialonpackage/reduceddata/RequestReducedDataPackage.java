package by.bsu.wialontransport.protocol.wialonpackage.reduceddata;

import by.bsu.wialontransport.crud.dto.Data;
import lombok.Value;

@Value
public class RequestReducedDataPackage{
    public static final String PREFIX = "#SD#";

    Data data;
}
