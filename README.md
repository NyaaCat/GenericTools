## Generic Tools
A spigot plugin aims at creating highly customizable weapons and tools using javascript.

Inspired by RPGItems.

## Example Config
```yaml
itemMap:
  test:
    __class__: me.recursiveg.generictools.config.ItemTemplate
    item:
      ==: org.bukkit.inventory.ItemStack
      v: 1957
      type: STICK
      meta:
        ==: ItemMeta
        meta-type: UNSPECIFIC
        display-name: The Generic Crowbar
    actions:
      '0':
        script: command(ctx, "say hello")
        trigger:
          __class__: me.recursiveg.generictools.triggers.RightClickAirTrigger
```

The server will print `hello` when right clicks in air.