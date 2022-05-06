package world.bentobox.checkmeout.commands.admin;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.Utils;
import world.bentobox.checkmeout.panels.ViewSubmissionsPanel;


/**
 *  * The `/[admin_cmd] cmo seesubmissions` command
 *
 * @author tastybento
 */
class SeeSubmissionsCommand extends CompositeCommand
{
    public SeeSubmissionsCommand(CompositeCommand parent)
    {
        super(parent, "seesubmissions", "seesubs");
    }


    @Override
    public void setup()
    {
        this.setPermission("checkmeout.admin.seesubs");
        this.setOnlyPlayer(true);
        this.setDescription("checkmeout.commands.admin.seesubs.description");
    }


    @Override
    public boolean canExecute(User user, String label, List<String> args)
    {
        if (this.<CheckMeOut>getAddon().getSubmissionsManager().listSubmissions(this.getWorld()).isEmpty())
        {
            Utils.sendMessage(user, "checkmeout.conversations.no-submissions-yet");
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public boolean execute(User user, String label, List<String> args)
    {
        if (args.isEmpty())
        {
            // No args
            ViewSubmissionsPanel.openPanel(this.getAddon(), this.getWorld(), user);
            return true;
        }
        else
        {
            this.showHelp(this, user);
            return false;
        }
    }
}
