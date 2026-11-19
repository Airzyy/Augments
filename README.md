# Augments

A configurable roguelike augment selection mod for Minecraft 1.21.1 NeoForge.

## Shortbow special augment

Create the built-in shortbow augment with:

```mcfunction
/augment create "Shortbow" rare special shortbow
/augment give @s shortbow
```

While holding a normal bow in your main hand, left-click (punch) to release a fully charged shot immediately. It uses the vanilla bow's release logic, so arrow availability, Infinity, bow enchantments, and the normal projectile setup are handled by Minecraft rather than spawning a bare arrow. The augment has a short server-side firing interval to prevent duplicate packet shots.
