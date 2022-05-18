//
// Created by BONNe
// Copyright - 2022
//


package world.bentobox.checkmeout.events;


import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import world.bentobox.bentobox.api.events.BentoBoxEvent;


/**
 * The type Island submitted event.
 */
public class IslandSubmittedEvent extends BentoBoxEvent
{
    /**
     * Instantiates a new Island submitted event.
     *
     * @param uuid the uuid
     * @param location the location
     */
    public IslandSubmittedEvent(@NotNull UUID uuid, @NotNull Location location)
    {
        this.location = location;
        this.uuid = uuid;
    }


    /**
     * Gets location.
     *
     * @return the location
     */
    @NotNull
    public Location getLocation()
    {
        return location;
    }


    /**
     * Gets uuid.
     *
     * @return the uuid
     */
    @NotNull
    public UUID getUuid()
    {
        return uuid;
    }


    /**
     * The Location of submission.
     */
    @NotNull
    private final Location location;

    /**
     * The submitter.
     */
    @NotNull
    private final UUID uuid;

    /**
     * Event listener list for current
     */
    private static final HandlerList handlers = new HandlerList();
}
