package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PayParcelOperation extends Operation {

    private final BigDecimal amount;

    public PayParcelOperation(BigDecimal amount) {
        super(OperationType.PAY_PARCEL);
        this.amount = amount;
    }
}
