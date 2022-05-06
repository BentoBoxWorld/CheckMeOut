package world.bentobox.checkmeout.commands.island;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.Utils;
import world.bentobox.checkmeout.panels.ViewSubmissionsPanel;


/**
 * @author tastybento
 *
 */
class ViewSubmissionsCommand extends CompositeCommand
{
    public ViewSubmissionsCommand(CheckMeOut addon, CompositeCommand parent)
    {
        super(parent,
            addon.getSettings().getViewCommand().split(" ")[0],
            addon.getSettings().getViewCommand().split(" "));
    }


    @Override
    public void setup()
    {
        this.setPermission("checkmeout.seesubs");
        this.setOnlyPlayer(true);
        this.setDescription("checkmeout.commands.user.seesubs.description");
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
