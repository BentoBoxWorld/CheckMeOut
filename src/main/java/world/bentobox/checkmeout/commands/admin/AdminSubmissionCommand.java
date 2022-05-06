package world.bentobox.checkmeout.commands.admin;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.ConfirmableCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;

/**
 * The main admin command `/[admin_cmd] cmo`
 *
 * @author tastybento
 */
public class AdminSubmissionCommand extends ConfirmableCommand
{
    public AdminSubmissionCommand(CheckMeOut addon, CompositeCommand parent)
    {
        super(addon,
            parent,
            addon.getSettings().getAdminCommand().split(" ")[0],
            addon.getSettings().getAdminCommand().split(" "));
    }


    @Override
    public void setup()
    {
        this.setPermission("checkmeout.admin");
        this.setOnlyPlayer(false);
        this.setDescription("checkmeout.commands.admin.description");

        new CheckSubmissionCommand(this);
        new DeleteSubmissionCommand(this);
        new ClearSubmissionsCommand(this);
        new SeeSubmissionsCommand(this);
    }


    @Override
    public boolean execute(User user, String label, List<String> args)
    {
        this.showHelp(this, user);
        return false;
    }
}
