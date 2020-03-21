package world.bentobox.checkmeout.config;

import java.util.HashSet;
import java.util.Set;

import world.bentobox.bentobox.api.configuration.ConfigComment;
import world.bentobox.bentobox.api.configuration.ConfigEntry;
import world.bentobox.bentobox.api.configuration.ConfigObject;
import world.bentobox.bentobox.api.configuration.StoreAt;


@StoreAt(filename="config.yml", path="addons/CheckMeOut")
@ConfigComment("CheckMeOut Configuration [version]")
@ConfigComment("This config file is dynamic and saved when the server is shutdown.")
@ConfigComment("You cannot edit it while the server is running because changes will")
@ConfigComment("be lost! Use in-game settings GUI or edit when server is offline.")
@ConfigComment("")
public class Settings implements ConfigObject
{
    @ConfigComment("")
    @ConfigComment("Icon that will be displayed in submission list. SIGN counts for any kind of sign and the type of")
    @ConfigComment("wood used will be reflected in the panel if the server supports it.")
    @ConfigComment("It uses native Minecraft material strings, but using string 'PLAYER_HEAD', it is possible to")
    @ConfigComment("use player heads instead. Beware that Mojang API rate limiting may prevent heads from loading.")
    @ConfigEntry(path = "icon")
    private String icon = "GRASS_BLOCK";

    @ConfigComment("")
    @ConfigComment("This list stores GameModes in which Level addon should not work.")
    @ConfigComment("To disable addon it is necessary to write its name in new line that starts with -. Example:")
    @ConfigComment("disabled-gamemodes:")
    @ConfigComment(" - BSkyBlock")
    @ConfigEntry(path = "disabled-gamemodes")
    private Set<String> disabledGameModes = new HashSet<>();

    @ConfigComment("")
    @ConfigComment("CheckMeOut panel name formatting.")
    @ConfigComment("Example: &c will make names red. &f is white")
    @ConfigEntry(path = "name-format")
    private String nameFormat = "&f";

    @ConfigComment("")
    @ConfigComment("Allow random checking - adds a button to the panel that goes to a random island")
    @ConfigEntry(path = "random-allowed")
    private boolean randomAllowed = true;

    @ConfigComment("")
    @ConfigComment("User and admin commands. You can change them if they clash with other addons or plugins.")
    @ConfigEntry(path = "user-command")
    String userCommand = "checkmeout";
    @ConfigEntry(path = "admin-command")
    String adminCommand = "cmo";


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
    public String getIcon()
    {
        return icon;
    }


    /**
     * This method sets the icon object value.
     * @param icon the icon object new value.
     */
    public void setIcon(String icon)
    {
        this.icon = icon;
    }


    /**
     * @return the nameFormat
     */
    public String getNameFormat() {
        return nameFormat;
    }


    /**
     * @param nameFormat the nameFormat to set
     */
    public void setNameFormat(String nameFormat) {
        this.nameFormat = nameFormat;
    }

    /**
     * @return the randomAllowed
     */
    public boolean isRandomAllowed() {
        return randomAllowed;
    }


    /**
     * @param randomAllowed the randomAllowed to set
     */
    public void setRandomAllowed(boolean randomAllowed) {
        this.randomAllowed = randomAllowed;
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


}
