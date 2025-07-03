package br.com.openlibrary.open_library.model;

public enum ReservationStatus {
    ACTIVE, // The reservation is active and can be cancelled
    CANCELLED, // The reservation has been cancelled
    FULFILLED, // The reservation has been fulfilled
    EXPIRED // The reservation has expired (Initially 48 hours)
}
