package org.linuxsenpai.konachan;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TaskRunner {
	/*	private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements*/
	public static final ExecutorService THREAD_POOL_EXECUTOR =
			new ThreadPoolExecutor(5, 128, 1,
					TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	private final Handler handler = new Handler(Looper.getMainLooper());

	public <R> void executeAsync(final Callable<R> callable, final Callback<R> callback) {
		THREAD_POOL_EXECUTOR.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final R result = callable.call();
					handler.post(new Runnable() {
						@Override
						public void run() {
							callback.onComplete(result);
						}
					});
				} catch (Exception ignored) {

				}
			}
		});
	}

	public interface Callback<R> {
		void onComplete(R result);
	}
}
