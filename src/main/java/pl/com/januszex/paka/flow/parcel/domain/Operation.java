package pl.com.januszex.paka.flow.parcel.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Operation {
    private final OperationType operationType;
}
