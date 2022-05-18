///
// Created by BONNe
// Copyright - 2022
///


package world.bentobox.checkmeout.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import world.bentobox.bentobox.api.events.island.IslandDeleteEvent;
import world.bentobox.checkmeout.CheckMeOut;


/**
 * This listener removes submitted island if player deletes his island.
 */
public class IslandDeleteListener implements Listener
{
    /**
     * @param addon - addon
     */
    public IslandDeleteListener(CheckMeOut addon)
    {
        this.addon = addon;
    }


    /**
     * This method handles island deletion. On island deletion it should remove generator data too.
     *
     * @param event IslandDeleteEvent instance.
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIslandDelete(IslandDeleteEvent event)
    {
        this.addon.getSubmissionsManager().removeSubmission(event.getIsland().getWorld(),
            event.getIsland().getOwner());
    }


    /**
     * stores addon instance
     */
    private final CheckMeOut addon;
}