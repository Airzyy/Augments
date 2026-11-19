# Augments

A configurable roguelike augment selection mod for Minecraft 1.21.1 NeoForge.

## Shortbow special augment

Create the built-in shortbow augment with:

```mcfunction
/augment create "Shortbow" rare special shortbow
/augment give @s shortbow
```

While holding any item in your main hand that is tagged as a bow, left-click (punch) to trigger its own release behavior at full charge. Supported tags are `#minecraft:enchantable/bow`, `#c:tools/bows`, `#forge:tools/bows`, and the mod's extensible `#augments:bows` tag. Add modded bows to `data/augments/tags/item/bows.json` (or another datapack) if they are not already in one of those tags. The item’s own `releaseUsing` implementation handles its normal projectile, ammo, and enchantment behavior; the mod does not create a generic arrow. A short server-side firing interval prevents duplicate packet shots.
