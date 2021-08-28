package norn;

public class Util {

    private static final ContinuationScope SCOPE = new ContinuationScope("NORN");

    public static Continuation cc() {
        return Continuation.getCurrentContinuation(SCOPE);
    }

    private static class BContinuation extends Continuation implements IYield {
        private Object value;

        public boolean yield(Object o) {
            ((BContinuation) cc()).value = o;
            return Continuation.yield(SCOPE);
        }
        private BContinuation(Runnable body) {
            super(SCOPE, body);
        }
    }

    public static BContinuation create(Runnable body) {
        return new BContinuation(body);
    }

    public static boolean yield(Object o) {
        Continuation cont = cc();
        if (cont instanceof IYield y) {
            return y.yield(o);
        }
        return Continuation.yield(SCOPE);
    }

    public static Object get(Continuation c) {
        if (c instanceof BContinuation bc) {
            return bc.value;
        }
        return null;
    }
}
