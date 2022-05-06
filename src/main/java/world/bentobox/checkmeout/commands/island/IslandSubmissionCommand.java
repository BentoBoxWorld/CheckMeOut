package world.bentobox.checkmeout.commands.island;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.Utils;


/**
 * @author tastybento
 *
 */
public class IslandSubmissionCommand extends CompositeCommand
{
    public IslandSubmissionCommand(CheckMeOut addon, CompositeCommand parent)
    {
        super(addon,
            parent,
            addon.getSettings().getUserCommand().split(" ")[0],
            addon.getSettings().getUserCommand().split(" "));
    }


    /* (non-Javadoc)
     * @see world.bentobox.bentobox.api.commands.BentoBoxCommand#setup()
     */
    @Override
    public void setup()
    {
        this.setPermission("checkmeout");
        this.setOnlyPlayer(true);
        this.setDescription("checkmeout.commands.user.description");

        new ViewSubmissionsCommand(this.getAddon(), this);
    }


    @Override
    public boolean canExecute(User user, String label, List<String> args)
    {
        // Check if user has an island
        if (!this.getIslands().hasIsland(this.getWorld(), user) &&
            !this.getIslands().inTeam(this.getWorld(), user.getUniqueId()))
        {
            Utils.sendMessage(user, "general.errors.no-island");
            return false;
        }
        else
        {
            return true;
        }
    }


    /* (non-Javadoc)
     * @see world.bentobox.bentobox.api.commands.BentoBoxCommand#execute(world.bentobox.bentobox.api.user.User, java.lang.String, java.util.List)
     */
    @Override
    public boolean execute(User user, String label, List<String> args)
    {
        if (this.<CheckMeOut>getAddon().getSubmissionsManager().addSubmission(user.getUniqueId(),
            this.getIslands().getHomeLocation(this.getWorld(), user.getUniqueId())))
        {
            Utils.sendMessage(user, "general.success");
            return true;
        }
        else
        {
            Utils.sendMessage(user, "general.errors.general");
            this.getAddon().logError("Could not submit island");
            return false;
        }
    }
}
