# About
Every block has baked in whether it can only be harvested with a tool. And if so,
which tool class and which harvest level of the tool is required. For example, 
redstone ore requires a pickaxe with at least the mining level 2, while glowstone
can be harvested with an empty hand. On the other hand, every tool has baked in 
the information, what kind of tools it is and if used as such a tool, which harvest
level it has. For instance, an iron pickaxe is a pickaxe - and only a pickaxe - 
with mining level 2, where a paxel is a combination of a pickaxe, an axe, and a shovel.
This mod allows you to modify the tool class and the harvest levels of each class 
for all tools. You can also modify the required tool class and the harvest level for
blocks. This allows mod pack makers to define a progression for tools.

# Configuration
See the [Wiki](https://github.com/tyra314/ToolProgression/wiki).

# Known issues:
- WAILA Harvestability is lying to you. If you define a block overwrite, which 
  changes a block to require a tool that didn't require one before, WAILA still shows, 
  that the block is harvestable. Don't worry though, you can break the block.
- Block Overwrites can't be done on a per metadata basis, so only one overwrite for all 
  metadata within one block. A future update will solve that. Block Overwrites are now only
  done per metadata. Will add support for per block overwrites again.

# Modpacks
Yes, you can use this shitty mod in any mod pack you like. This includes redistribution as a part of a modpack.
If you make a shitload of money, please send some beer and pizza.
