package world.bentobox.checkmeout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.Database;
import world.bentobox.bentobox.util.Util;
import world.bentobox.bentobox.util.teleport.SafeSpotTeleport;
import world.bentobox.checkmeout.objects.SubmissionData;

/**
 * Handles submissions
 *
 * @author tastybento
 *
 */
public class SubmissionsManager {
    private static final int MAX_SUBMISSIONS = 600;
    private BentoBox plugin;
    // Map of all submissions stored as player, Location
    private Map<World, Map<UUID, Location>> worldsSubmissionsList;
    // Database handler for level data
    private Database<SubmissionData> handler;

    private CheckMeOut addon;
    private SubmissionData submissionsData = new SubmissionData();

    /**
     * Get the map for this world
     * @param world - world
     * @return map of submissions
     */
    @NonNull
    public Map<UUID, Location> getSubmissionsMap(@Nullable World world) {
        return worldsSubmissionsList.computeIfAbsent(Util.getWorld(world), k -> new HashMap<>());
    }

    /**
     * @param addon - addon
     * @param plugin - plugin
     */
    public SubmissionsManager(CheckMeOut addon, BentoBox plugin) {
        this.addon = addon;
        this.plugin = plugin;
        // Set up the database handler
        // Note that these are saved by the BentoBox database
        handler = new Database<>(addon, SubmissionData.class);
        // Load the submissions
        loadSubmissionList();
    }

    /**
     * Stores submissions in the warp array. If successful, fires an event
     *
     * @param playerUUID - the player's UUID
     * @param loc - location of submission
     * @return true if successful, false if not
     */
    public boolean addSubmission(final UUID playerUUID, final Location loc) {
        // Do not allow null players to set submissions
        if (playerUUID == null || loc == null) {
            return false;
        }
        getSubmissionsMap(loc.getWorld()).put(playerUUID, loc);
        saveSubmissions();
        return true;
    }

    /**
     * Provides the location of the warp for player or null if one is not found
     *
     * @param world - world to search in
     * @param playerUUID - the player's UUID
     *            - the submissions requested
     * @return Location of submission or null
     */
    @Nullable
    public Location getSubmission(World world, UUID playerUUID) {
        return getSubmissionsMap(world).get(playerUUID);
    }

    /**
     * Get the name of the warp owner by location
     * @param location to search
     * @return Name of warp owner or empty string if there is none
     */
    @NonNull
    public String getSubmissionOwner(Location location) {
        return getSubmissionsMap(location.getWorld()).entrySet().stream().filter(en -> en.getValue().equals(location))
                .findFirst().map(en -> plugin.getPlayers().getName(en.getKey())).orElse("");
    }

    /**
     * Get sorted list of submissions with most recent players listed first
     * @return UUID list
     */
    @NonNull
    public List<UUID> getSortedSubmissions(@NonNull World world) {
        // Remove any null locations - this can happen if an admin changes the name of the world and signs point to old locations
        getSubmissionsMap(world).values().removeIf(Objects::isNull);
        // Bigger value of time means a more recent login
        TreeMap<Long, UUID> map = new TreeMap<>();
        getSubmissionsMap(world).entrySet().forEach(en -> {
            UUID uuid = en.getKey();
            // If never played, will be zero
            long lastPlayed = addon.getServer().getOfflinePlayer(uuid).getLastPlayed();
            // This aims to avoid the chance that players logged off at exactly the same time
            if (!map.isEmpty() && map.containsKey(lastPlayed)) {
                lastPlayed = map.firstKey() - 1;
            }
            map.put(lastPlayed, uuid);
        });
        Collection<UUID> result = map.descendingMap().values();
        List<UUID> list = new ArrayList<>(result);
        if (list.size() > MAX_SUBMISSIONS) {
            list.subList(0, MAX_SUBMISSIONS).clear();
        }
        return list;
    }

    /**
     * Lists all the known submissions for this world
     * @param world - world
     *
     * @return UUID set of submissions
     */
    @NonNull
    public Set<UUID> listSubmissions(@NonNull World world) {
        // Remove any null locations
        getSubmissionsMap(world).values().removeIf(Objects::isNull);
        return getSubmissionsMap(world).entrySet().stream().filter(e -> Util.sameWorld(world, e.getValue().getWorld())).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    /**
     * Load the submissions and check if they still exist
     */
    private void loadSubmissionList() {
        addon.getLogger().info("Loading submissions...");
        worldsSubmissionsList = new HashMap<>();
        if (handler.objectExists("submissions")) {
            submissionsData = handler.loadObject("submissions");
            // Load into map
            if (submissionsData != null) {
                worldsSubmissionsList = submissionsData.getSubmissions();
            }
        }
    }

    /**
     * Remove submission owned by UUID
     *
     * @param uuid
     */
    public void removeSubmission(World world, UUID uuid) {
        if (getSubmissionsMap(world).containsKey(uuid)) {
            getSubmissionsMap(world).remove(uuid);
        }
        saveSubmissions();
    }

    /**
     * Saves the submissions to the database
     */
    public void saveSubmissions() {
        submissionsData.setSubmissions(worldsSubmissionsList);
        handler.saveObject(submissionsData);
    }

    /**
     * Warps a user to the submitted location
     *
     * @param world - world to check
     * @param user - user who is warping
     * @param owner - owner of the submission
     */
    public void warpPlayer(@NonNull World world, @NonNull User user, @NonNull UUID owner) {
        final Location warpSpot = getSubmission(world, owner);
        user.getPlayer().playSound(user.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1F, 1F);
        new SafeSpotTeleport(plugin, user.getPlayer(), warpSpot, "", false, 0);
    }

    /**
     * Check if a player has a submission
     * @param playerUUID - player's UUID
     * @return true if they have submission
     */
    public boolean hasSubmission(@NonNull World world, @NonNull UUID playerUUID) {
        return getSubmissionsMap(world).containsKey(playerUUID);
    }

    /**
     * Clears all submissions
     */
    public void clearAll() {
        worldsSubmissionsList.clear();
        saveSubmissions();
    }

}
