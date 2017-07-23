## Generic Tools
A spigot plugin aims at creating highly customizable weapons and tools.

Inspired by RPGItems.

## Example Usage

```
/gt create test                           # create new tool template
/gt attach test trig right_click_air      # this trigger is placed at trigger index 0
/gt attach test func cooldown 3           # this function is placed at function index 0
/gt attach test func consume              # this function is placed at function index 1
/gt attach test func command `say line1`  # this function is placed at function index 2
/gt attach test func command `say line2`  # this function is placed at function index 3
/gt link test trig 0 to 0 to 1 to 2 3     # right_click -> cooldown -> consume -> command(line1)
                                                                               -> command(line2)
/gt give test 5                           # give five of this new item to you
```
When right click on air, you should see `line1` and `line2` printed in chat, by server, in that order.

If the cooldown time is not finish, neither the commands will be executed nor the item will be consumed.