package world.bentobox.checkmeout.commands;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;

/**
 * @author tastybento
 *
 */
public class CheckMeOutCommand extends CompositeCommand {

    public CheckMeOutCommand(CheckMeOut addon, CompositeCommand parent) {
        super(addon, parent, addon.getSettings().getUserCommand());
    }

    /* (non-Javadoc)
     * @see world.bentobox.bentobox.api.commands.BentoBoxCommand#setup()
     */
    @Override
    public void setup() {
        this.setPermission("checkmeout");
        this.setOnlyPlayer(true);
        this.setDescription("checkmeout.commands.user.description");
    }

    @Override
    public boolean canExecute(User user, String label, List<String> args) {
        // Check if user has an island
        if (!getIslands().hasIsland(getWorld(), user) && !getIslands().inTeam(getWorld(), user.getUniqueId())) {
            user.sendMessage("general.errors.no-island");
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see world.bentobox.bentobox.api.commands.BentoBoxCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)
     */
    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (((CheckMeOut)getAddon()).getSubmissionsManager().addSubmission(user.getUniqueId(), getIslands().getHomeLocation(getWorld(), user.getUniqueId()))) {
            user.sendMessage("general.success");
            return true;
        }
        user.sendMessage("general.errors.general");
        getAddon().logError("Could not submit island");
        return false;
    }

}
