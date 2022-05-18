package world.bentobox.checkmeout;


import java.util.HashSet;
import java.util.Set;

import org.bukkit.World;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.configuration.Config;
import world.bentobox.bentobox.util.Util;
import world.bentobox.checkmeout.commands.admin.AdminSubmissionCommand;
import world.bentobox.checkmeout.commands.island.IslandSubmissionCommand;
import world.bentobox.checkmeout.config.Settings;
import world.bentobox.checkmeout.listeners.IslandDeleteListener;
import world.bentobox.checkmeout.managers.SubmissionsManager;
import world.bentobox.level.Level;
import world.bentobox.likes.LikesAddon;
import world.bentobox.visit.VisitAddon;
import world.bentobox.warps.Warp;


/**
 * Addon to BentoBox that enables island submissions
 * @author tastybento
 *
 */
public class CheckMeOut extends Addon {
    // ---------------------------------------------------------------------
    // Section: Variables
    // ---------------------------------------------------------------------

    /**
     * Submissions manager.
     */
    private SubmissionsManager submissionsManager;

    /**
     * This variable stores in which worlds this addon is working.
     */
    private Set<World> registeredWorlds;

    /**
     * This variable stores if addon settings.
     */
    private Settings settings;

    /**
     * This variable stores if addon is hooked or not.
     */
    private boolean hooked;

    /**
     * Settings config object
     */
    private Config<Settings> settingsConfig;

    /**
     * Level addon instance.
     */
    private Level levelAddon;

    /**
     * Likes addon instance.
     */
    private LikesAddon likesAddon;

    /**
     * Visit addon instance.
     */
    private VisitAddon visitAddon;

    /**
     * Warp addon instance.
     */
    private Warp warpAddon;


    // ---------------------------------------------------------------------
    // Section: Methods
    // ---------------------------------------------------------------------


    /**
     * Executes code when loading the addon. This is called before {@link #onEnable()}. This should preferably
     * be used to setup configuration and worlds.
     */
    @Override
    public void onLoad()
    {
        super.onLoad();
        // Save default config.yml
        this.saveDefaultConfig();
        // Load the plugin's config
        this.loadSettings();
    }


    /**
     * Executes code when reloading the addon.
     */
    @Override
    public void onReload()
    {
        super.onReload();

        if (this.hooked) {
            this.submissionsManager.saveSubmissions();

            this.loadSettings();
            this.getLogger().info("CheckMeOut addon reloaded.");
        }
    }


    @Override
    public void onEnable() {
        // Check if it is enabled - it might be loaded, but not enabled.
        if (!this.getPlugin().isEnabled()) {
            this.setState(State.DISABLED);
            return;
        }

        registeredWorlds = new HashSet<>();

        // Register commands
        this.getPlugin().getAddonsManager().getGameModeAddons().forEach(gameModeAddon -> {
            if (!this.settings.getDisabledGameModes().contains(gameModeAddon.getDescription().getName())
                    && gameModeAddon.getPlayerCommand().isPresent()
                    && gameModeAddon.getAdminCommand().isPresent())
            {
                this.registeredWorlds.add(gameModeAddon.getOverWorld());

                new AdminSubmissionCommand(this, gameModeAddon.getAdminCommand().get());
                new IslandSubmissionCommand(this, gameModeAddon.getPlayerCommand().get());
                this.hooked = true;
            }
        });

        if (hooked)
        {
            // Start managers
            submissionsManager = new SubmissionsManager(this, this.getPlugin());
            // Load the listener
            this.registerListener(new IslandDeleteListener(this));
        } else {
            logWarning("Addon did not hook into any game modes - disabling");
            this.setState(State.DISABLED);
        }
    }


    @Override
    public void onDisable(){
        // Save the submissions
        if (submissionsManager != null)
        {
            submissionsManager.saveSubmissions();
        }
    }


    /**
     * Check addon hooks.
     */
    public void allLoaded()
    {
        // Try to find Level addon and if it does not exist, display a warning
        this.getAddonByName("Level").ifPresentOrElse(addon ->
        {
            this.levelAddon = (Level) addon;
            this.log("CheckMeOut Addon hooked into Level addon.");
        }, () ->
        {
            this.levelAddon = null;
        });

        // Try to find Likes addon and if it does not exist, display a warning
        this.getAddonByName("Likes").ifPresentOrElse(addon ->
        {
            this.likesAddon = (LikesAddon) addon;
            this.log("CheckMeOut Addon hooked into Likes addon.");
        }, () ->
        {
            this.likesAddon = null;
        });

        // Try to find Visit addon and if it does not exist, display a warning
        this.getAddonByName("Visit").ifPresentOrElse(addon ->
        {
            this.visitAddon = (VisitAddon) addon;
            this.log("CheckMeOut Addon hooked into Visit addon.");
        }, () ->
        {
            this.visitAddon = null;
        });

        // Try to find Warps addon and if it does not exist, display a warning
        this.getAddonByName("Warps").ifPresentOrElse(addon ->
        {
            this.warpAddon = (Warp) addon;
            this.log("CheckMeOut Addon hooked into Warps addon.");
        }, () ->
        {
            this.warpAddon = null;
        });
    }


    /**
     * This method loads addon configuration settings in memory.
     */
    private void loadSettings() {
        if (settingsConfig == null) {
            settingsConfig = new Config<>(this, Settings.class);
        }

        this.settings = settingsConfig.loadConfigObject();

        if (this.settings == null) {
            // Disable
            this.logError("CheckMeOut settings could not load! Addon disabled.");
            this.setState(State.DISABLED);
            return;
        }
        settingsConfig.saveConfigObject(settings);

        this.saveResource("panels/view_panel.yml", false);
    }


    public SubmissionsManager getSubmissionsManager() {
        return submissionsManager;
    }

    public String getPermPrefix(World world) {
        return this.getPlugin().getIWM().getPermissionPrefix(world);
    }

    /**
     * Check if an event is in a registered world
     * @param world - world to check
     * @return true if it is
     */
    public boolean inRegisteredWorld(World world) {
        return registeredWorlds.contains(Util.getWorld(world));
    }

    /**
     * @return the settings
     */
    public Settings getSettings() {
        return settings;
    }


    /**
     * Gets level addon.
     *
     * @return the level addon
     */
    public Level getLevelAddon()
    {
        return levelAddon;
    }


    /**
     * Gets likes addon.
     *
     * @return the likes addon
     */
    public LikesAddon getLikesAddon()
    {
        return likesAddon;
    }


    /**
     * Gets visit addon.
     *
     * @return the visit addon
     */
    public VisitAddon getVisitAddon()
    {
        return visitAddon;
    }


    /**
     * Gets warp addon.
     *
     * @return the warp addon
     */
    public Warp getWarpAddon()
    {
        return warpAddon;
    }
}
