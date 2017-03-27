## Generic Tools
A spigot plugin aims at creating highly customizable weapons and tools.

Inspired by RPGItems.

## Example Usage

```
/gt create test                           # create new tool template
/gt attach test trig right_click_air      # this trigger is placed at trigger index 0
/gt attach test func command `say hello`  # this function is placed at function index 0
/gt attach test func command `say hi`     # this function is placed at function index 1
/gt link test trig 0 to 0 1               # link trigger@idx=0 to function@idx=0,1
/gt give test                             # give this new item to you
```
When right click on air, you should see `hello` and `hi` printed in chat, by server, in that order