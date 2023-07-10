package io.axoniq.demo.bikerental.bikerental.coreapi;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ReturnBikeCommand(@TargetAggregateIdentifier String bikeId, String location) {
}