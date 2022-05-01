package world.bentobox.checkmeout.commands.admin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.World;
import org.eclipse.jdt.annotation.Nullable;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.ConfirmableCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.Utils;


/**
 * The `/[admin_cmd] cmo check <name>` command
 *
 * @author tastybento
 */
class DeleteSubmissionCommand extends ConfirmableCommand
{
    private @Nullable UUID target;


    public DeleteSubmissionCommand(CompositeCommand parent)
    {
        super(parent, "delete");
    }


    @Override
    public void setup()
    {
        this.setPermission("checkmeout.admin.delete");
        this.setOnlyPlayer(true);
        this.setParametersHelp("checkmeout.commands.admin.delete.parameters");
        this.setDescription("checkmeout.commands.admin.delete.description");
    }


    @Override
    public boolean canExecute(User user, String label, List<String> args)
    {
        if (args.isEmpty())
        {
            this.showHelp(this, user);
            return false;
        }

        this.target = getPlayers().getUUID(args.get(0));

        if (this.target != null)
        {
            if (this.<CheckMeOut>getAddon().getSubmissionsManager().
                listSubmissions(this.getWorld()).contains(this.target))
            {
                return true;
            }
        }

        Utils.sendMessage(user, "checkmeout.conversations.does-not-exist");

        return false;
    }


    @Override
    public boolean execute(User user, String label, List<String> args)
    {
        this.askConfirmation(user, () -> this.delete(user, target));
        return true;
    }


    private void delete(User user, @Nullable UUID uuid)
    {
        this.<CheckMeOut>getAddon().getSubmissionsManager().removeSubmission(this.getWorld(), uuid);
        Utils.sendMessage(user, "general.success");
    }


    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args)
    {
        World world = this.getWorld() == null ? user.getWorld() : this.getWorld();

        return Optional.of(this.<CheckMeOut>getAddon().getSubmissionsManager().
            listSubmissions(world).
            stream().
            map(this.getPlayers()::getName).
            collect(Collectors.toList()));
    }
}
