package dev.rosewood.rosestacker.command.command;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.CommandContext;
import dev.rosewood.rosegarden.command.framework.RoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import dev.rosewood.rosegarden.command.framework.annotation.RoseExecutable;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import dev.rosewood.rosestacker.manager.LocaleManager;
import dev.rosewood.rosestacker.manager.StackManager;
import dev.rosewood.rosestacker.stack.Stack;
import dev.rosewood.rosestacker.utils.StackerUtils;
import dev.rosewood.rosestacker.utils.ThreadUtils;

public class StatsCommand extends RoseCommand {

    public StatsCommand(RosePlugin rosePlugin, RoseCommandWrapper parent) {
        super(rosePlugin, parent);
    }

    @RoseExecutable
    public void execute(CommandContext context) {
        StackManager stackManager = this.rosePlugin.getManager(StackManager.class);
        LocaleManager localeManager = this.rosePlugin.getManager(LocaleManager.class);

        int threadAmount = stackManager.getStackingThreads().size();

        int entityStackAmount = stackManager.getStackedEntities().size();
        int itemStackAmount = stackManager.getStackedItems().size();
        int blockStackAmount = stackManager.getStackedBlocks().size();
        int spawnerStackAmount = stackManager.getStackedSpawners().size();

        int entityAmount = stackManager.getStackedEntities().values().stream().mapToInt(Stack::getStackSize).sum();
        int itemAmount = stackManager.getStackedItems().values().stream().mapToInt(Stack::getStackSize).sum();
        int blockAmount = stackManager.getStackedBlocks().values().stream().mapToInt(Stack::getStackSize).sum();
        int spawnerAmount = stackManager.getStackedSpawners().values().stream().mapToInt(Stack::getStackSize).sum();

        localeManager.sendMessage(context.getSender(), "command-stats-header");
        localeManager.sendSimpleMessage(context.getSender(), "command-stats-threads", StringPlaceholders.single("amount", StackerUtils.formatNumber(threadAmount)));
        localeManager.sendSimpleMessage(context.getSender(), "command-stats-stacked-entities", StringPlaceholders.builder("stackAmount", entityStackAmount).addPlaceholder("total", StackerUtils.formatNumber(entityAmount)).build());
        localeManager.sendSimpleMessage(context.getSender(), "command-stats-stacked-items", StringPlaceholders.builder("stackAmount", itemStackAmount).addPlaceholder("total", StackerUtils.formatNumber(itemAmount)).build());
        localeManager.sendSimpleMessage(context.getSender(), "command-stats-stacked-blocks", StringPlaceholders.builder("stackAmount", blockStackAmount).addPlaceholder("total", StackerUtils.formatNumber(blockAmount)).build());
        localeManager.sendSimpleMessage(context.getSender(), "command-stats-stacked-spawners", StringPlaceholders.builder("stackAmount", spawnerStackAmount).addPlaceholder("total", StackerUtils.formatNumber(spawnerAmount)).build());
        localeManager.sendSimpleMessage(context.getSender(), "command-stats-active-tasks", StringPlaceholders.single("amount", StackerUtils.formatNumber(ThreadUtils.getActiveThreads())));
    }

    @Override
    protected String getDefaultName() {
        return "stats";
    }

    @Override
    public String getDescriptionKey() {
        return "command-stats-description";
    }

    @Override
    public String getRequiredPermission() {
        return "rosestacker.stats";
    }

}
