package io.axoniq.demo.bikerental.bikerental.command;

import io.axoniq.demo.bikerental.bikerental.coreapi.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class Bike {

    @AggregateIdentifier
    private String bikeId;

    private boolean isAvailable;

    public Bike() {
    }

    @CommandHandler
    public Bike(RegisterBikeCommand command) {
        apply(new BikeRegisteredEvent(command.bikeId(), command.location()));
    }

    @CommandHandler
    public void handle(RentBikeCommand command) {
        if (!this.isAvailable) {
            throw new IllegalStateException("Bike is already rented");
        }
        apply(new BikeRentedEvent(command.bikeId(), command.renter()));
    }

    @CommandHandler
    public void handle(ReturnBikeCommand command) {
        if (this.isAvailable) {
            throw new IllegalStateException("Bike was already returned");
        }
        apply(new BikeReturnedEvent(command.bikeId(), command.location()));
    }

    @EventSourcingHandler
    protected void handle(BikeRegisteredEvent event) {
        this.bikeId = event.bikeId();
        this.isAvailable = true;
    }

    @EventSourcingHandler
    protected void handle(BikeReturnedEvent event) {
        this.isAvailable = true;
    }

    @EventSourcingHandler
    protected void handle(BikeRentedEvent event) {
        this.isAvailable = false;
    }
}
