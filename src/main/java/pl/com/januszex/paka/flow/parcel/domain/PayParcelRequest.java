package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PayParcelRequest extends Operation {

    private final BigDecimal amount;

    public PayParcelRequest(BigDecimal amount) {
        super(OperationType.PAY_PARCEL);
        this.amount = amount;
    }
}
