package world.bentobox.checkmeout.commands.admin;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.ConfirmableCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.Utils;


/**
 * The `/[admin_cmd] cmo clearall` command
 *
 * @author tastybento
 */
class ClearSubmissionsCommand extends ConfirmableCommand
{
    public ClearSubmissionsCommand(CompositeCommand parent)
    {
        super(parent, "clearall");
    }


    @Override
    public void setup()
    {
        this.setPermission("checkmeout.admin.clearall");
        this.setOnlyPlayer(false);
        this.setDescription("checkmeout.commands.admin.clearall.description");
    }


    @Override
    public boolean execute(User user, String label, List<String> args)
    {
        this.askConfirmation(user, () ->
        {
            this.<CheckMeOut>getAddon().getSubmissionsManager().clearAll();
            Utils.sendMessage(user, "general.success");
        });

        return true;
    }
}
