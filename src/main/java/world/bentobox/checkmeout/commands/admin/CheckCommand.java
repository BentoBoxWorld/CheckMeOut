package world.bentobox.checkmeout.commands.admin;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;

/**
 * The /bsbadmin check <name> command
 *
 * @author tastybento
 *
 */
class CheckCommand extends CompositeCommand {

    private final CheckMeOut addon;

    public CheckCommand(CheckMeOut addon, CompositeCommand parent) {
        super(parent, "check");
        this.addon = addon;
    }

    @Override
    public void setup() {
        this.setPermission("checkmeout.admin.check");
        this.setOnlyPlayer(true);
        this.setParametersHelp("checkmeout.commands.admin.check.parameters");
        this.setDescription("checkmeout.commands.admin.check.description");
    }

    @Override
    public boolean canExecute(User user, String label, List<String> args) {
        if (addon.getSubmissionsManager().listSubmissions(getWorld()).isEmpty()) {
            user.sendMessage("checkmeout.error.no-submissions-yet");
            return false;
        }
        return true;
    }
    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (args.size() == 1) {
            // Get submissions
            Set<UUID> submissionList = addon.getSubmissionsManager().listSubmissions(getWorld());
            if (submissionList.isEmpty()) {
                user.sendMessage("checkmeout.error.no-submissions-yet");
                return false;
            } else {
                // Check if this is part of a name
                UUID found = submissionList.stream().filter(u -> getPlayers().getName(u).equalsIgnoreCase(args.get(0))
                        || getPlayers().getName(u).toLowerCase().startsWith(args.get(0).toLowerCase())).findFirst().orElse(null);
                if (found == null) {
                    user.sendMessage("checkmeout.error.does-not-exist");
                    return false;
                } else {
                    // Submission exists!
                    addon.getSubmissionsManager().warpPlayer(getWorld(), user, found);
                    return true;
                }
            }
        } else {
            this.showHelp(this, user);
            return false;
        }
    }

    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args) {
        return Optional.of(addon
                .getSubmissionsManager()
                .listSubmissions(getWorld())
                .stream()
                .map(getPlayers()::getName)
                .collect(Collectors.toList()));
    }


}
