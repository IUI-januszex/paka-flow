package pl.com.januszex.paka.flow.state.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Operation {
    private final OperationType operationType;
}
