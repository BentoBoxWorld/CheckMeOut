package world.bentobox.checkmeout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;

import world.bentobox.bentobox.api.panels.PanelItem;
import world.bentobox.bentobox.api.panels.builders.PanelBuilder;
import world.bentobox.bentobox.api.panels.builders.PanelItemBuilder;
import world.bentobox.bentobox.api.user.User;

public class SubmissionPanelManager {

    private static final int PANEL_MAX_SIZE = 52;
    private final CheckMeOut addon;

    public SubmissionPanelManager(CheckMeOut addon) {
        this.addon = addon;
    }

    private PanelItem getPanelItem(World world, UUID owner) {
        PanelItemBuilder pib = new PanelItemBuilder()
                .name(addon.getSettings().getNameFormat() + addon.getPlugin().getPlayers().getName(owner))
                .clickHandler((panel, clicker, click, slot) -> hander(world, clicker, owner));
        Material icon = Material.matchMaterial(addon.getSettings().getIcon());
        if (icon == null) icon = Material.GRASS_BLOCK;
        if (icon.equals(Material.PLAYER_HEAD)) {
            return pib.icon(addon.getPlayers().getName(owner)).build();
        } else {
            return pib.icon(icon).build();
        }
    }

    private boolean hander(World world, User clicker, UUID owner) {
        clicker.closeInventory();
        String adminCommand = addon.getPlugin().getIWM().getAddon(world).map(gm -> gm.getAdminCommand().map(Command::getLabel).orElse("")).orElse("");
        String command = addon.getSettings().getAdminCommand() + " check " + addon.getPlayers().getName(owner);
        clicker.getPlayer().performCommand((adminCommand.isEmpty() ? "" : adminCommand + " ") + command);
        return true;
    }

    private PanelItem getRandomButton(World world, User user, UUID owner) {
        return new PanelItemBuilder()
                .name(addon.getSettings().getNameFormat() + user.getTranslation("checkmeout.random"))
                .clickHandler((panel, clicker, click, slot) -> hander(world, clicker, owner))
                .icon(Material.END_CRYSTAL).build();
    }

    /**
     * Show the submissions panel for the user
     * @param world - world
     * @param user - user
     * @param index - page to show - 0 is first
     */
    public void showSubmissionsPanel(World world, User user, int index) {
        List<UUID> submissions = new ArrayList<>(addon.getSubmissionsManager().getSortedSubmissions(world));
        UUID randomSubmission = null;
        // Add random UUID
        if (!submissions.isEmpty() && addon.getSettings().isRandomAllowed()) {
            randomSubmission = submissions.get(new Random().nextInt(submissions.size()));
            submissions.add(0, randomSubmission);
        }
        if (index < 0) {
            index = 0;
        } else if (index > (submissions.size() / PANEL_MAX_SIZE)) {
            index = submissions.size() / PANEL_MAX_SIZE;
        }
        PanelBuilder panelBuilder = new PanelBuilder()
                .user(user)
                .name(user.getTranslation("checkmeout.title") + " " + (index + 1));

        int i = index * PANEL_MAX_SIZE;
        for (; i < (index * PANEL_MAX_SIZE + PANEL_MAX_SIZE) && i < submissions.size(); i++) {
            if (i == 0 && randomSubmission != null) {
                panelBuilder.item(getRandomButton(world, user, randomSubmission));
            } else {
                panelBuilder.item(getPanelItem(world, submissions.get(i)));
            }
        }
        final int panelNum = index;
        // Add signs
        if (i < submissions.size()) {
            // Next
            panelBuilder.item(new PanelItemBuilder()
                    .name(user.getTranslation("checkmeout.next"))
                    .icon(new ItemStack(Material.STONE))
                    .clickHandler((panel, clicker, click, slot) -> {
                        user.closeInventory();
                        showSubmissionsPanel(world, user, panelNum+1);
                        return true;
                    }).build());
        }
        if (i > PANEL_MAX_SIZE) {
            // Previous
            panelBuilder.item(new PanelItemBuilder()
                    .name(user.getTranslation("checkmeout.previous"))
                    .icon(new ItemStack(Material.COBBLESTONE))
                    .clickHandler((panel, clicker, click, slot) -> {
                        user.closeInventory();
                        showSubmissionsPanel(world, user, panelNum-1);
                        return true;
                    }).build());
        }
        panelBuilder.build();
    }

}
