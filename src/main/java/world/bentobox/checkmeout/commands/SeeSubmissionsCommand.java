package world.bentobox.checkmeout.commands;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;

/**
 * @author tastybento
 *
 */
public class SeeSubmissionsCommand extends CompositeCommand {

    private CheckMeOut addon;

    public SeeSubmissionsCommand(CheckMeOut addon, CompositeCommand parent) {
        super(parent, "seesubmissions", "seesubs");
        this.addon = addon;
    }

    @Override
    public void setup() {
        this.setPermission("checkmeout.admin.seesubs");
        this.setOnlyPlayer(true);
        this.setDescription("checkmeout.commands.admin.seesubs.description");
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

        if (args.isEmpty()) {
            // No args
            addon.getSubmissionsPanelManager().showSubmissionsPanel(getWorld(), user, 0);
            return true;
        }
        this.showHelp(this, user);
        return false;

    }

}
