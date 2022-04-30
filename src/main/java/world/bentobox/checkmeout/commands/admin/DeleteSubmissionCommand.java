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

/**
 * The /bsbadmin check <name> command
 *
 * @author tastybento
 *
 */
class DeleteSubmissionCommand extends ConfirmableCommand {

    private final CheckMeOut addon;
    private @Nullable UUID target;

    public DeleteSubmissionCommand(CheckMeOut addon, CompositeCommand bsbIslandCmd) {
        super(bsbIslandCmd, "delete");
        this.addon = addon;
    }

    @Override
    public void setup() {
        this.setPermission("checkmeout.admin.delete");
        this.setOnlyPlayer(true);
        this.setParametersHelp("checkmeout.commands.admin.delete.parameters");
        this.setDescription("checkmeout.commands.admin.delete.description");
    }

    @Override
    public boolean canExecute(User user, String label, List<String> args) {
        if (args.isEmpty()) {
            this.showHelp(this, user);
            return false;
        }
        target = getPlayers().getUUID(args.get(0));
        if (target != null) {
            if (addon.getSubmissionsManager().listSubmissions(getWorld()).contains(target)) {
                return true;
            }
        }
        user.sendMessage("checkmeout.error.does-not-exist");
        return false;
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        this.askConfirmation(user, ()-> delete(user, target));
        return true;
    }

    private void delete(User user, @Nullable UUID uuid) {
        addon.getSubmissionsManager().removeSubmission(getWorld(), uuid);
        user.sendMessage("general.success");
    }

    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args) {
        World world = getWorld() == null ? user.getWorld() : getWorld();
        return Optional.of(addon.getSubmissionsManager().listSubmissions(world).stream().map(getPlayers()::getName).collect(Collectors.toList()));
    }


}
