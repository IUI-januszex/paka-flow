package pl.com.januszex.paka.flow.parcel.domain;

public enum OperationType {
    ASSIGN_TO_COURIER,
    DELIVER_TO_CLIENT,
    DELIVER_TO_WAREHOUSE,
    NO_OPERATION,
    PICKUP,
    PAY_PARCEL,
    PAY_FEE,
    EDIT,
    DELETE,
    DELIVERY_ATTEMPT,
    RETURN_TO_WAREHOUSE
}
