package world.bentobox.checkmeout.commands.island;

import java.util.List;

import world.bentobox.bentobox.api.commands.CompositeCommand;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.checkmeout.panels.ViewSubmissionsPanel;


/**
 * @author tastybento
 *
 */
class ViewSubmissionsCommand extends CompositeCommand {

    private final CheckMeOut addon;

    public ViewSubmissionsCommand(CheckMeOut addon, CompositeCommand parent) {
        super(parent, addon.getSettings().getViewCommand().split(" ")[0], addon.getSettings().getViewCommand().split(" "));
        this.addon = addon;
    }

    @Override
    public void setup() {
        this.setPermission("checkmeout.seesubs");
        this.setOnlyPlayer(true);
        this.setDescription("checkmeout.commands.seesubs.description");
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
