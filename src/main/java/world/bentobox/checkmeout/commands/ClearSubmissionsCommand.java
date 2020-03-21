package world.bentobox.checkmeout.commands;

import java.util.List;

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
class ClearSubmissionsCommand extends ConfirmableCommand {

    private final CheckMeOut addon;

    public ClearSubmissionsCommand(CheckMeOut addon, CompositeCommand bsbIslandCmd) {
        super(bsbIslandCmd, "clearall");
        this.addon = addon;
    }

    @Override
    public void setup() {
        this.setPermission("checkmeout.admin.clearall");
        this.setOnlyPlayer(false);
        this.setDescription("checkmeout.commands.admin.clearall.description");
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        this.askConfirmation(user, () -> {
            addon.getSubmissionsManager().clearAll();
            user.sendMessage("general.success");
        });
        return true;
    }

}
