package world.bentobox.checkmeout.commands;

import java.util.List;
import java.util.UUID;

import org.eclipse.jdt.annotation.Nullable;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.commands.ConfirmableCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;

/**
 * The admin command
 *
 * @author tastybento
 *
 */
public class CMOAdminCommand extends ConfirmableCommand {

    private @Nullable UUID target;

    public CMOAdminCommand(CheckMeOut addon, CompositeCommand bsbIslandCmd) {
        super(bsbIslandCmd, addon.getSettings().getAdminCommand());
        new CheckCommand(addon, this);
        new DeleteSubmissionCommand(addon, this);
        new ClearSubmissionsCommand(addon, this);
        new SeeSubmissionsCommand(addon, this);
    }

    @Override
    public void setup() {
        this.setPermission("checkmeout.admin");
        this.setOnlyPlayer(false);
        this.setDescription("checkmeout.commands.admin.description");
    }


    @Override
    public boolean execute(User user, String label, List<String> args) {
        this.showHelp(this, user);
        return false;
    }

}
