package tyra314.toolprogression.command;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.MaterialOverwrite;
import tyra314.toolprogression.harvest.ToolOverwrite;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToolProgressionCommand extends CommandBase
{
    private final List<String> aliases = Lists.newArrayList(ToolProgressionMod.MODID);
    private static final String[] COMMAND_COMPLETIONS = {"reload", "set", "unset"};
    private static final String[] SET_COMPLETIONS = {"pickaxe", "axe", "shovel"};


    @Override
    public @Nonnull
    String getName()
    {
        return "tpr";
    }

    @Override
    public @Nonnull
    String getUsage(@Nonnull ICommandSender sender)
    {
        return "tpr reload | set <class> <level> | unset";
    }

    @Override
    public @Nonnull
    List<String> getAliases()
    {
        return aliases;
    }

    private void sendMessage(ICommandSender sender, String msg)
    {
        sender.sendMessage(new TextComponentString(TextFormatting.YELLOW.toString() +
                                                   TextFormatting.ITALIC.toString() +
                                                   "ToolProgression " +
                                                   TextFormatting.RESET.toString() +
                                                   msg));
    }

    private boolean handleReload(@Nonnull ICommandSender sender)
    {
        ConfigHandler.readBaseConfig();

        final IForgeRegistry<Block> block_registry = GameRegistry.findRegistry(Block.class);

        for (Block block : block_registry)
        {
            BlockOverwrite.applyToAllStates(block);
        }

        final IForgeRegistry<Item> item_registry = GameRegistry.findRegistry(Item.class);

        for (Item item : item_registry)
        {
            if (item instanceof ItemTool)
            {
                ToolOverwrite.applyToItem(item);
            }
        }

        for (ItemTool.ToolMaterial mat : ItemTool.ToolMaterial.values())
        {
            MaterialOverwrite.applyToMaterial(mat);
        }

        sendMessage(sender, "configuration reloaded.");

        return true;
    }

    private boolean handleUnset(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender)
    {
        // FIXME Assuming singleplayer
        assert server.isSinglePlayer();

        EntityPlayer player = server.getPlayerList().getPlayers().get(0);


        ItemStack stack = player.getHeldItemMainhand();


        if (stack.isEmpty())
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED +
                                                       "Can't unset overwrite for an empty item."));

            return true;
        }

        Item item = stack.getItem();

        if (item instanceof ItemTool)
        {
            ConfigHandler.toolOverwrites.unset(item);
            ConfigHandler.toolOverwrites.save();

            sendMessage(sender, "deleted the tool overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            sendMessage(sender, "needs to restart, in order to restore the default.");

            return true;
        }

        if (item instanceof ItemBlock)
        {
            ItemBlock iblock = (ItemBlock) item;
            Block block = iblock.getBlock();

            // I don't know, why we shouldn't use that here. Enlighten me, if you do. Thanks.
            @SuppressWarnings("deprecation") IBlockState
                    state =
                    block.getStateFromMeta(item.getDamage(stack));

            ConfigHandler.blockOverwrites.unset(BlockHelper.getKeyString(state));
            ConfigHandler.blockOverwrites.save();

            sendMessage(sender, "deleted the block overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            sendMessage(sender, "needs to restart, in order to restore the default.");

            return true;
        }

        return false;
    }

    private boolean handleSet(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender,
                              String itemclass, int level)
    {
        // FIXME Assuming singleplayer
        assert server.isSinglePlayer();

        EntityPlayer player = server.getPlayerList().getPlayers().get(0);


        ItemStack stack = player.getHeldItemMainhand();


        if (stack.isEmpty())
        {
            sender.sendMessage(new TextComponentString(TextFormatting.RED +
                                                       "Can't set overwrite for an empty item."));

            return true;
        }

        Item item = stack.getItem();

        if (item instanceof ItemTool)
        {
            ToolOverwrite overwrite = ConfigHandler.toolOverwrites.get(item);

            if (overwrite == null)
            {
                overwrite = new ToolOverwrite();
            }

            overwrite.addOverwrite(itemclass, level);
            overwrite.apply((ItemTool) item);

            ConfigHandler.toolOverwrites.set(item, overwrite);
            ConfigHandler.toolOverwrites.save();

            sendMessage(sender, "added the tool overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            return true;
        }

        if (item instanceof ItemBlock)
        {
            ItemBlock iblock = (ItemBlock) item;
            Block block = iblock.getBlock();

            // I don't know, why we shouldn't use that here. Enlighten me, if you do. Thanks.
            @SuppressWarnings("deprecation") IBlockState
                    state =
                    block.getStateFromMeta(item.getDamage(stack));

            String key = BlockHelper.getKeyString(state);

            BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(key);

            if (overwrite == null)
            {
                overwrite = new BlockOverwrite(itemclass, level);
            }
            else
            {
                overwrite.addOverwrite(itemclass, level);
            }

            overwrite.apply(state);

            ConfigHandler.blockOverwrites.set(key, overwrite);
            ConfigHandler.blockOverwrites.save();

            sendMessage(sender, "added the block overwrite for '" + TextFormatting.BOLD.toString
                    () +
                                stack.getDisplayName() + TextFormatting.RESET.toString() + "' " +
                                ".");

            return true;
        }

        return false;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server,
                        @Nonnull ICommandSender sender,
                        @Nonnull String[] args) throws CommandException
    {

        if (args.length == 1 && args[0].equals("reload"))
        {
            if (handleReload(sender))
            {
                return;
            }
        }
        else if (args.length == 1 && args[0].equals("unset"))
        {
            if (handleUnset(server, sender))
            {
                return;
            }
        }
        else if (args.length == 3 && args[0].equals("set"))
        {
            if (handleSet(server, sender, args[1], Integer.parseInt(args[2])))
            {
                return;
            }
        }

        sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error parsing command"));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return server.isSinglePlayer();
    }

    private List<String> completeTo(String input, String[] completions)
    {
        List<String> comps = new ArrayList<>();

        for (String comp : completions)
        {
            if (comp.startsWith(input))
            {
                comps.add(comp);
            }
        }

        return comps;
    }

    @Override
    public @Nonnull
    List<String> getTabCompletions(@Nonnull MinecraftServer server,
                                   @Nonnull ICommandSender sender,
                                   @Nonnull String[] args,
                                   @Nullable BlockPos targetPos)
    {
        List<String> comp = completeTo(args[0], COMMAND_COMPLETIONS);

        if (comp.size() > 0 && args.length < 2)
        {
            return comp;
        }

        if (args.length == 2 && comp.contains("set"))
        {
            List<String> itemClass = completeTo(args[1], SET_COMPLETIONS);

            return itemClass;
        }

        return Collections.emptyList();
    }
}
