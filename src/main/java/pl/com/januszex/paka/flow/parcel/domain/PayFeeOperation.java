package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PayFeeOperation extends Operation {

    private final BigDecimal amount;

    public PayFeeOperation(BigDecimal amount) {
        super(OperationType.PAY_FEE);
        this.amount = amount;
    }
}
