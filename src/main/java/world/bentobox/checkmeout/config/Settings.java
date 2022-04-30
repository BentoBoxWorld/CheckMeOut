package world.bentobox.checkmeout.config;

import org.bukkit.Material;
import java.util.HashSet;
import java.util.Set;

import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.ConfigObject;
import world.bentobox.bentobox.api.configuration.StoreAt;


@StoreAt(filename="config.yml", path="addons/CheckMeOut")
@ConfigComment("CheckMeOut Configuration [version]")
@ConfigComment("")
public class Settings implements ConfigObject
{
    @ConfigComment("")
    @ConfigComment("Icon that will be displayed in submission list. SIGN counts for any kind of sign and the type of")
    @ConfigComment("wood used will be reflected in the panel if the server supports it.")
    @ConfigComment("It uses native Minecraft material strings, but using string 'PLAYER_HEAD', it is possible to")
    @ConfigComment("use player heads instead. Beware that Mojang API rate limiting may prevent heads from loading.")
    @ConfigEntry(path = "icon")
    private Material islandIcon = Material.GRASS_BLOCK;

    @ConfigComment("")
    @ConfigComment("This list stores GameModes in which Level addon should not work.")
    @ConfigComment("To disable addon it is necessary to write its name in new line that starts with -. Example:")
    @ConfigComment("disabled-gamemodes:")
    @ConfigComment(" - BSkyBlock")
    @ConfigEntry(path = "disabled-gamemodes")
    private Set<String> disabledGameModes = new HashSet<>();

    @ConfigComment("")
    @ConfigComment("User and admin commands. You can change them if they clash with other addons or plugins.")
    @ConfigEntry(path = "commands.user-command")
    private String userCommand = "checkmeout cmo";

    @ConfigComment("")
    @ConfigComment("View command for user to see other submissions.")
    @ConfigEntry(path = "commands.view-command")
    private String viewCommand = "view";

    @ConfigEntry(path = "commands.admin-command")
    private String adminCommand = "checkmeout cmo";


    // ---------------------------------------------------------------------
    // Section: Constructor
    // ---------------------------------------------------------------------


    /**
     * Loads the various settings from the config.yml file into the plugin
     */
    public Settings()
    {
        // empty constructor
    }


    // ---------------------------------------------------------------------
    // Section: Methods
    // ---------------------------------------------------------------------

    /**
     * This method returns the disabledGameModes object.
     * @return the disabledGameModes object.
     */
    public Set<String> getDisabledGameModes()
    {
        return disabledGameModes;
    }


    /**
     * This method sets the disabledGameModes object value.
     * @param disabledGameModes the disabledGameModes object new value.
     *
     */
    public void setDisabledGameModes(Set<String> disabledGameModes)
    {
        this.disabledGameModes = disabledGameModes;
    }


    /**
     * This method returns the icon object.
     * @return the icon object.
     */
    public Material getIslandIcon()
    {
        return islandIcon;
    }


    /**
     * This method sets the icon object value.
     * @param islandIcon the icon object new value.
     */
    public void setIslandIcon(Material islandIcon)
    {
        this.islandIcon = islandIcon;
    }

    /**
     * @return the userCommand
     */
    public String getUserCommand() {
        return userCommand;
    }


    /**
     * @param userCommand the userCommand to set
     */
    public void setUserCommand(String userCommand) {
        this.userCommand = userCommand;
    }


    /**
     * @return the adminCommand
     */
    public String getAdminCommand() {
        return adminCommand;
    }


    /**
     * @param adminCommand the adminCommand to set
     */
    public void setAdminCommand(String adminCommand) {
        this.adminCommand = adminCommand;
    }


    /**
     * Gets view command.
     *
     * @return the view command
     */
    public String getViewCommand()
    {
        return viewCommand;
    }


    /**
     * Sets view command.
     *
     * @param viewCommand the view command
     */
    public void setViewCommand(String viewCommand)
    {
        this.viewCommand = viewCommand;
    }
}
