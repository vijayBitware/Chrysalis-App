package android.support.v4.os;

import android.os.AsyncTask;

/**
 * Created by Admin on 1/21/2018.
 */

public class AsyncTaskCompatHoneycomb {
    static <Params, Progress, Result> void executeParallel(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
    }
}
