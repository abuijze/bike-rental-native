package io.axoniq.demo.bikerental.bikerental.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record RegisterBikeCommand(@TargetAggregateIdentifier String bikeId, String location) {

}
