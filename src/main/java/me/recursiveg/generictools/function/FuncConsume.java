package me.recursiveg.generictools.function;

import me.recursiveg.generictools.I18n;
import me.recursiveg.generictools.runtime.WrappedEvent;

@IFunction.Function(FuncConsume.NAME)
public class FuncConsume implements IFunction {
    public static final String NAME = "consume";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public WrappedEvent<?> accept(WrappedEvent<?> we) {
        int amount = we.wis.getItem().getAmount();
        we.wis.getItem().setAmount(amount - 1);
        return we;
    }

    @Override
    public String getInfoString() {
        return I18n.format("function_help.consume.info_string");
    }
}
