package io.axoniq.demo.bikerental.bikerental.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RentBikeCommand(@TargetAggregateIdentifier String bikeId, String renter) {

}
