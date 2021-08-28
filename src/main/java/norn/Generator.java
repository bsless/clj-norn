package norn;

import java.util.*;

public class Generator implements Iterable<Object>, IYield {

    private final Continuation cont;

    public Generator(Runnable body) {
        cont = Util.create(body);
    }

    public boolean yield(Object o) {
        return Util.yield(o);
    }

    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            public Object next() {
                cont.run();
                return Util.get(cont);
            }
            public boolean hasNext() {
                return !cont.isDone();
            }
        };
    }
}
