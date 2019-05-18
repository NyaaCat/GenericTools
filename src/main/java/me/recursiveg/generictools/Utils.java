package me.recursiveg.generictools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {
    public static ItemStack createBook(String str) {
        ItemStack ret = new ItemStack(Material.WRITABLE_BOOK);
        BookMeta m = (BookMeta) ret.getItemMeta();
        m.setPage(0, str);
        ret.setItemMeta(m);
        return ret;
    }

    public static class Result<T, E> { // Similar to Rust
        boolean ok = false;
        private T t = null;
        private E e = null;

        public Result(T t, E e, boolean ok) {
            this.t = t;
            this.e = e;
            this.ok = ok;
        }

        public static <E> Result<Object, E> Err(E e) {
            return new Result<>(null, e, false);
        }

        public static <T> Result<T, Object> Ok(T t) {
            return new Result<>(t, null, true);
        }

        public boolean isOk() {
            return ok;
        }

        public <R> R match(Function<? super T, ? extends R> whenOk, Function<? super E, ? extends R> whenErr) {
            return ok ? whenOk.apply(t) : whenErr.apply(e);
        }

        public void match(Consumer<? super T> whenOk, Consumer<? super E> whenErr) {
            if (ok) {
                whenOk.accept(t);
            } else {
                whenErr.accept(e);
            }
        }
    }

    public static class Triple<P, Q, R> {
        public P p;
        public Q q;
        public R r;

        public Triple(P p, Q q, R r) {
            this.p = p;
            this.q = q;
            this.r = r;
        }

        public static <P, Q, R> Triple<P, Q, R> of(P p, Q q, R r) {
            return new Triple<>(p, q, r);
        }
    }
}
