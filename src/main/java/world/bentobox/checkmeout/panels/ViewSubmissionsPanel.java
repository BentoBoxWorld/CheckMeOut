//
// Created by BONNe
// Copyright - 2022
//

package world.bentobox.checkmeout.panels;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import world.bentobox.bentobox.api.addons.GameModeAddon;
import world.bentobox.bentobox.api.panels.PanelItem;
import world.bentobox.bentobox.api.panels.TemplatedPanel;
import world.bentobox.bentobox.api.panels.builders.PanelItemBuilder;
import world.bentobox.bentobox.api.panels.builders.TemplatedPanelBuilder;
import world.bentobox.bentobox.api.panels.reader.ItemTemplateRecord;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.checkmeout.CheckMeOut;
import world.bentobox.likes.LikesAddon;
import world.bentobox.likes.database.objects.LikesObject;


/**
 * This class shows how to set up easy panel by using BentoBox PanelBuilder API
 */
public class ViewSubmissionsPanel
{
// ---------------------------------------------------------------------
// Section: Constructor
// ---------------------------------------------------------------------


    /**
     * This is internal constructor. It is used internally in current class to avoid creating objects everywhere.
     *
     * @param addon VisitAddon object
     * @param world World where user will be teleported
     * @param user User who opens panel
     */
    private ViewSubmissionsPanel(CheckMeOut addon,
        World world,
        User user)
    {
        this.addon = addon;
        this.user = user;
        this.world = world;

        this.elementList = new ArrayList<>(this.addon.getSubmissionsManager().getSortedSubmissions(this.world));
    }


// ---------------------------------------------------------------------
// Section: Methods
// ---------------------------------------------------------------------


    /**
     * Build method manages current panel opening. It uses BentoBox PanelAPI that is easy to use and users can get nice
     * panels.
     */
    private void build()
    {
        // Do not open gui if there is no magic sticks.
        if (this.elementList.isEmpty())
        {
            this.addon.logError("There are no available islands for viewing!");
            Utils.sendMessage(this.user, "checkmeout.conversations.no-submissions-yet");
            return;
        }

        // Start building panel.
        TemplatedPanelBuilder panelBuilder = new TemplatedPanelBuilder();

        // Set main template.
        panelBuilder.template("view_panel", new File(this.addon.getDataFolder(), "panels"));
        panelBuilder.user(this.user);
        panelBuilder.world(this.user.getWorld());

        // Register button builders
        panelBuilder.registerTypeBuilder("ISLAND", this::createIslandButton);
        panelBuilder.registerTypeBuilder("RANDOM", this::createRandomButton);

        // Register next and previous builders
        panelBuilder.registerTypeBuilder("NEXT", this::createNextButton);
        panelBuilder.registerTypeBuilder("PREVIOUS", this::createPreviousButton);

        // Register unknown type builder.
        panelBuilder.build();
    }


// ---------------------------------------------------------------------
// Section: Buttons
// ---------------------------------------------------------------------


    /**
     * Create next button panel item.
     *
     * @param template the template
     * @param slot the slot
     * @return the panel item
     */
    @Nullable
    private PanelItem createNextButton(@NonNull ItemTemplateRecord template, TemplatedPanel.ItemSlot slot)
    {
        int size = this.elementList.size();

        if (size <= slot.amountMap().getOrDefault("ISLAND", 1) ||
            1.0 * size / slot.amountMap().getOrDefault("ISLAND", 1) <= this.pageIndex + 1)
        {
            // There are no next elements
            return null;
        }

        int nextPageIndex = this.pageIndex + 2;

        PanelItemBuilder builder = new PanelItemBuilder();

        if (template.icon() != null)
        {
            ItemStack clone = template.icon().clone();

            if ((Boolean) template.dataMap().getOrDefault("indexing", false))
            {
                clone.setAmount(nextPageIndex);
            }

            builder.icon(clone);
        }

        if (template.title() != null)
        {
            builder.name(this.user.getTranslation(this.world, template.title()));
        }

        if (template.description() != null)
        {
            builder.description(this.user.getTranslation(this.world, template.description(),
                "[number]", String.valueOf(nextPageIndex)));
        }

        // Add ClickHandler
        builder.clickHandler((panel, user, clickType, i) ->
        {
            template.actions().forEach(action -> {
                if (clickType == action.clickType()  || action.clickType() == ClickType.UNKNOWN)
                {
                    if ("NEXT".equalsIgnoreCase(action.actionType()))
                    {
                        // Next button ignores click type currently.
                        this.pageIndex++;
                        this.build();
                    }
                }
            });

            // Always return true.
            return true;
        });

        // Collect tooltips.
        List<String> tooltips = template.actions().stream().
            filter(action -> action.tooltip() != null).
            map(action -> this.user.getTranslation(this.world, action.tooltip())).
            filter(text -> !text.isBlank()).
            collect(Collectors.toCollection(() -> new ArrayList<>(template.actions().size())));

        // Add tooltips.
        if (!tooltips.isEmpty())
        {
            // Empty line and tooltips.
            builder.description("");
            builder.description(tooltips);
        }

        return builder.build();
    }


    /**
     * Create previous button panel item.
     *
     * @param template the template
     * @param slot the slot
     * @return the panel item
     */
    @Nullable
    private PanelItem createPreviousButton(@NonNull ItemTemplateRecord template, TemplatedPanel.ItemSlot slot)
    {
        if (this.pageIndex == 0)
        {
            // There are no next elements
            return null;
        }

        int previousPageIndex = this.pageIndex;

        PanelItemBuilder builder = new PanelItemBuilder();

        if (template.icon() != null)
        {
            ItemStack clone = template.icon().clone();

            if ((Boolean) template.dataMap().getOrDefault("indexing", false))
            {
                clone.setAmount(previousPageIndex);
            }

            builder.icon(clone);
        }

        if (template.title() != null)
        {
            builder.name(this.user.getTranslation(this.world, template.title()));
        }

        if (template.description() != null)
        {
            builder.description(this.user.getTranslation(this.world, template.description(),
                "[number]", String.valueOf(previousPageIndex)));
        }

        // Add ClickHandler
        // Add ClickHandler
        builder.clickHandler((panel, user, clickType, i) ->
        {
            template.actions().forEach(action -> {
                if (clickType == action.clickType()  || action.clickType() == ClickType.UNKNOWN)
                {
                    if ("PREVIOUS".equalsIgnoreCase(action.actionType()))
                    {
                        // Next button ignores click type currently.
                        this.pageIndex--;
                        this.build();
                    }
                }
            });

            // Always return true.
            return true;
        });

        // Collect tooltips.
        List<String> tooltips = template.actions().stream().
            filter(action -> action.tooltip() != null).
            map(action -> this.user.getTranslation(this.world, action.tooltip())).
            filter(text -> !text.isBlank()).
            collect(Collectors.toCollection(() -> new ArrayList<>(template.actions().size())));

        // Add tooltips.
        if (!tooltips.isEmpty())
        {
            // Empty line and tooltips.
            builder.description("");
            builder.description(tooltips);
        }

        return builder.build();
    }


    /**
     * This method creates and returns island button.
     *
     * @return PanelItem that represents island button.
     */
    @Nullable
    private PanelItem createIslandButton(ItemTemplateRecord template, TemplatedPanel.ItemSlot slot)
    {
        if (this.elementList.isEmpty())
        {
            // Does not contain any sticks.
            return null;
        }

        int index = this.pageIndex * slot.amountMap().getOrDefault("ISLAND", 1) + slot.slot();

        if (index >= this.elementList.size())
        {
            // Out of index.
            return null;
        }

        return this.createIslandButton(template, this.elementList.get(index), "island");
    }


    /**
     * This method creates and returns random warp button.
     *
     * @return PanelItem that represents random warp button.
     */
    @Nullable
    private PanelItem createRandomButton(ItemTemplateRecord template, TemplatedPanel.ItemSlot slot)
    {
        if (this.elementList.size() < 2)
        {
            // Does not contain any sticks.
            return null;
        }

        int index = random.nextInt(this.elementList.size());
        return this.createIslandButton(template, this.elementList.get(index), "random");
    }


// ---------------------------------------------------------------------
// Section: Other methods
// ---------------------------------------------------------------------


    /**
     * This method creates and returns button that switch to next page in view mode.
     *
     * @return PanelItem that allows to select next owner view page.
     */
    private PanelItem createIslandButton(ItemTemplateRecord template, UUID ownerUUID, String type)
    {
        Island island = this.addon.getIslandsManager().getIsland(this.world, ownerUUID);

        if (island == null || island.getOwner() == null)
        {
            // return as island has no owner. Empty button will be created.
            return null;
        }

        final String reference = "checkmeout.gui.buttons.island.";
        User owner = User.getInstance(ownerUUID);

        // Get settings for island.
        PanelItemBuilder builder = new PanelItemBuilder();

        if (template.icon() != null)
        {
            if (template.icon().getType().equals(Material.PLAYER_HEAD))
            {
                builder.icon(owner.getName());
            }
            else
            {
                builder.icon(template.icon().clone());
            }
        }
        else
        {
            // Check owner for a specific icon
            Material material = Material.matchMaterial(
                Utils.getPermissionValue(owner, "checkmeout.icon",
                    this.addon.getSettings().getIslandIcon().name()));

            if (material == null)
            {
                // Set material to a default icon from settings.
                material = this.addon.getSettings().getIslandIcon();
            }

            if (material == Material.PLAYER_HEAD)
            {
                builder.icon(owner.getName());
            }
            else
            {
                builder.icon(material);
            }
        }

        if (template.title() != null)
        {
            builder.name(this.user.getTranslation(this.world, template.title(),
                "[name]", island.getName() != null ?
                    island.getName() : owner.getName()));
        }
        else
        {
            builder.name(this.user.getTranslation(reference + "name",
                "[name]", island.getName() != null ?
                    island.getName() : owner.getName()));
        }

        // Process Description of the button.

        // Generate [owner] text.
        String ownerText = this.user.getTranslationOrNothing(reference + "owner",
            "[player]", owner.getName());

        // Generate [members] text
        String memberText;

        if (island.getMemberSet().size() > 1)
        {
            StringBuilder memberBuilder = new StringBuilder(
                this.user.getTranslationOrNothing(reference + "members-title"));

            island.getMemberSet().stream().
                map(this.addon.getPlayers()::getName).
                forEach(user -> {
                    if (memberBuilder.length() > 0)
                    {
                        memberBuilder.append("\n");
                    }

                    memberBuilder.append(
                        this.user.getTranslationOrNothing(reference + "member",
                            "[player]", user));
                });

            memberText = memberBuilder.toString();
        }
        else
        {
            memberText = "";
        }

        // Generate [level] text
        String levelText = this.generateLevelText(ownerUUID);

        // Generate [likes] text
        String likesText = this.generateLikesText(island);

        String descriptionText;

        if (template.description() != null)
        {
            descriptionText = this.user.getTranslationOrNothing(template.description(),
                    "[owner]", ownerText,
                    "[members]", memberText,
                    "[level]", levelText,
                    "[likes]", likesText).
                replaceAll("(?m)^[ \\t]*\\r?\\n", "").
                replaceAll("(?<!\\\\)\\|", "\n").
                replaceAll("\\\\\\|", "|");
        }
        else
        {
            descriptionText = this.user.getTranslationOrNothing(reference + "description",
                "[owner]", ownerText,
                "[members]", memberText,
                "[level]", levelText,
                "[likes]", likesText);

            // Clean up description text and split it into parts.
            descriptionText = descriptionText.replaceAll("(?m)^[ \\t]*\\r?\\n", "").
                replaceAll("(?<!\\\\)\\|", "\n").
                replaceAll("\\\\\\|", "|");
        }

        builder.description(descriptionText);

        // Get only possible actions, by removing all inactive ones.
        List<ItemTemplateRecord.ActionRecords> activeActions = new ArrayList<>(template.actions());

        activeActions.removeIf(action ->
        {
            switch (action.actionType().toUpperCase())
            {
                case "WARP" -> {
                    return island.getOwner() == null ||
                        this.addon.getWarpAddon() == null ||
                        !this.addon.getWarpAddon().getWarpSignsManager().hasWarp(this.world, island.getOwner());
                }
                case "VISIT" -> {
                    return island.getOwner() == null ||
                        this.addon.getVisitAddon() == null ||
                        !this.addon.getVisitAddon().getAddonManager().preprocessTeleportation(this.user, island, true);
                }
                case "CHECK" -> {
                    String gamemode = Utils.getGameMode(this.world);

                    return gamemode.isBlank() || !this.user.hasPermission(gamemode + ".checkmeout.admin.check");
                }
                default -> {
                    return false;
                }
            }
        });

        // Remove duplicated actions
        Set<ClickType> activeClicks = new HashSet<>();

        for (Iterator<ItemTemplateRecord.ActionRecords> iter = activeActions.iterator(); iter.hasNext(); )
        {
            ClickType clickType = iter.next().clickType();

            if (activeClicks.contains(clickType))
            {
                iter.remove();
            }
            else
            {
                activeClicks.add(clickType);
            }
        }

        // Add Click handler
        builder.clickHandler((panel, user, clickType, i) ->
        {
            for (ItemTemplateRecord.ActionRecords action : activeActions)
            {
                if (clickType == action.clickType() || action.clickType().equals(ClickType.UNKNOWN))
                {
                    switch (action.actionType().toUpperCase())
                    {
                        case "WARP" -> {
                            this.user.closeInventory();
                            this.addon.getWarpAddon().getWarpSignsManager().warpPlayer(this.world, this.user, island.getOwner());
                        }
                        case "VISIT" -> {
                            // Use Visit command because of delay. Delay is implemented via command executing.
                            String visitCommand = this.addon.getVisitAddon().getSettings().getPlayerMainCommand().split(" ")[0];

                            this.addon.getPlugin().getIWM().getAddon(this.world).
                                flatMap(GameModeAddon::getPlayerCommand).ifPresent(command ->
                                {
                                    this.user.performCommand(command.getTopLabel() + " " + visitCommand + " " + island.getOwner());
                                    this.user.closeInventory();
                                });
                        }
                        case "CHECK" -> {
                            this.user.closeInventory();
                            this.addon.getSubmissionsManager().warpPlayer(this.world, user, ownerUUID);
                        }
                    }
                }
            }

            return true;
        });

        // Collect tooltips.
        List<String> tooltips = activeActions.stream().
            filter(action -> action.tooltip() != null).
            map(action -> this.user.getTranslation(this.world, action.tooltip())).
            filter(text -> !text.isBlank()).
            collect(Collectors.toCollection(() -> new ArrayList<>(template.actions().size())));

        // Add tooltips.
        if (!tooltips.isEmpty())
        {
            // Empty line and tooltips.
            builder.description("");
            builder.description(tooltips);
        }

        return builder.build();
    }


    /**
     * This method generated Level Text for island.
     * @param ownerUUID Owner whoes island level must be displayed.
     * @return Level text.
     */
    private String generateLevelText(UUID ownerUUID)
    {
        final String reference = "checkmeout.gui.buttons.island.level";
        String levelText;

        if (this.addon.getLevelAddon() != null)
        {
            long level = this.addon.getLevelAddon().getIslandLevel(this.world, ownerUUID);
            int place = this.addon.getLevelAddon().getManager().getRank(this.world, ownerUUID);

            levelText = this.user.getTranslationOrNothing(reference,
                "[level]", String.valueOf(level),
                "[place]", String.valueOf(place));
        }
        else
        {
            levelText = "";
        }

        return levelText;
    }


    /**
     * This method generates text that will replace [likes] in button description.
     * @param island Island which likes text must be generated.
     * @return String that contains information about island likes.
     */
    private String generateLikesText(Island island)
    {
        final String reference = "checkmeout.gui.buttons.island.likes.";

        String likesText;

        if (this.addon.getLikesAddon() != null)
        {
            LikesAddon likesAddon = this.addon.getLikesAddon();

            LikesObject existingIslandLikes = likesAddon.getAddonManager().getExistingIslandLikes(island.getUniqueId());

            if (existingIslandLikes != null)
            {
                switch (likesAddon.getSettings().getMode())
                {
                    case LIKES -> {
                        long likes = existingIslandLikes.getLikes();
                        int place = likesAddon.getAddonManager().getIslandRankByLikes(this.world, existingIslandLikes);

                        likesText = this.user.getTranslationOrNothing(reference + "likes",
                            "[likes]", String.valueOf(likes),
                            "[place]", String.valueOf(place));
                    }
                    case LIKES_DISLIKES -> {
                        long likes = existingIslandLikes.getLikes();
                        long dislikes = existingIslandLikes.getDislikes();
                        long rank = existingIslandLikes.getRank();

                        int placeLikes = likesAddon.getAddonManager().getIslandRankByLikes(this.world, existingIslandLikes);
                        int placeDislikes = likesAddon.getAddonManager().getIslandRankByDislikes(this.world, existingIslandLikes);
                        int placeRank = likesAddon.getAddonManager().getIslandRankByRank(this.world, existingIslandLikes);

                        likesText = this.user.getTranslationOrNothing(reference + "likes_dislikes",
                            "[likes]", String.valueOf(likes),
                            "[dislikes]", String.valueOf(dislikes),
                            "[rank]", String.valueOf(rank),
                            "[place_likes]", String.valueOf(placeLikes),
                            "[place_dislikes]", String.valueOf(placeDislikes),
                            "[place_rank]", String.valueOf(placeRank));
                    }
                    case STARS -> {
                        double stars = existingIslandLikes.getStarsValue();
                        int place = likesAddon.getAddonManager().getIslandRankByStars(this.world, existingIslandLikes);

                        likesText = this.user.getTranslationOrNothing(reference + "stars",
                            "[stars]", String.valueOf(stars),
                            "[place]", String.valueOf(place));
                    }
                    default -> {
                        likesText = "";
                    }
                }
            }
            else
            {
                likesText = this.user.getTranslationOrNothing(reference + "no-likes");
            }
        }
        else
        {
            likesText = "";
        }

        return likesText;
    }


// ---------------------------------------------------------------------
// Section: Static methods
// ---------------------------------------------------------------------


    /**
     * This method is used to open UserPanel outside this class. It will be much easier to open panel with single method
     * call then initializing new object.
     *
     * @param addon CheckMeOut object
     * @param world World where user will be viewing
     * @param user User who opens panel
     */
    public static void openPanel(CheckMeOut addon,
        World world,
        User user)
    {
        new ViewSubmissionsPanel(addon, world, user).build();
    }


// ---------------------------------------------------------------------
// Section: Variables
// ---------------------------------------------------------------------


    /**
     * This variable allows to access addon object.
     */
    private final CheckMeOut addon;

    /**
     * This variable holds user who opens panel. Without it panel cannot be opened.
     */
    private final User user;

    /**
     * This variable holds world where panel is opened. Without it panel cannot be opened.
     */
    private final World world;

    /**
     * This variable stores filtered elements.
     */
    private List<UUID> elementList;

    /**
     * This variable holds current pageIndex for multi-page island choosing.
     */
    private int pageIndex;

    /**
     * Random for finding random warp.
     */
    private static final Random random = new Random();
}
