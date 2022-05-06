package world.bentobox.checkmeout.commands.admin;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.Utils;


/**
 * The `/[admin_cmd] cmo check <name>` command
 *
 * @author tastybento
 */
class CheckSubmissionCommand extends CompositeCommand
{
    public CheckSubmissionCommand(CompositeCommand parent)
    {
        super(parent, "check");
    }


    @Override
    public void setup()
    {
        this.setPermission("checkmeout.admin.check");
        this.setOnlyPlayer(true);
        this.setParametersHelp("checkmeout.commands.admin.check.parameters");
        this.setDescription("checkmeout.commands.admin.check.description");
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
        if (args.size() == 1)
        {
            // Get submissions
            Set<UUID> submissionList = this.<CheckMeOut>getAddon().getSubmissionsManager().listSubmissions(this.getWorld());

            // Check if this is part of a name
            UUID found = submissionList.stream().filter(u -> getPlayers().getName(u).equalsIgnoreCase(args.get(0)) ||
                this.getPlayers().getName(u).toLowerCase().startsWith(args.get(0).toLowerCase())).findFirst().orElse(null);

            if (found == null)
            {
                Utils.sendMessage(user, "checkmeout.conversations.does-not-exist");
                return false;
            }
            else
            {
                // Submission exists!
                this.<CheckMeOut>getAddon().getSubmissionsManager().warpPlayer(this.getWorld(), user, found);
                return true;
            }
        }
        else
        {
            this.showHelp(this, user);
            return false;
        }
    }


    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args)
    {
        return Optional.of(this.<CheckMeOut>getAddon().getSubmissionsManager().
            listSubmissions(this.getWorld()).
            stream().
            map(this.getPlayers()::getName).
            collect(Collectors.toList()));
    }
}
