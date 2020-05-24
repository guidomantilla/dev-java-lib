/*
 *
 */
package dev.java.common.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The Class TaskExecutionAdapter.
 *
 * @param <TaskResult> the generic type
 */
public class TaskExecutionAdapter<TaskResult> {

	/** The executor service. */
	private ExecutorService executorService;

	/** The completion service. */
	private CompletionService<TaskResult> completionService;

	/**
	 * Instantiates a new task execution adapter.
	 */
	public TaskExecutionAdapter() {

		this(true);
	}

	/**
	 * Instantiates a new task execution adapter.
	 *
	 * @param fixed the fixed
	 */
	public TaskExecutionAdapter(boolean fixed) {

		if (fixed) {
			executorService = Executors.newSingleThreadExecutor();
		} else {
			executorService = Executors.newCachedThreadPool();
		}

		createCompletionService();
	}

	/**
	 * Instantiates a new task execution adapter.
	 *
	 * @param poolSize the pool size
	 */
	public TaskExecutionAdapter(int poolSize) {

		this(poolSize, true);
	}

	/**
	 * Instantiates a new task execution adapter.
	 *
	 * @param poolSize the pool size
	 * @param fixed    the fixed
	 */
	public TaskExecutionAdapter(int poolSize, boolean fixed) {

		if (fixed) {
			executorService = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<>());
		} else {
			executorService = new ThreadPoolExecutor(poolSize, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
					new SynchronousQueue<>());
		}

		createCompletionService();
	}

	/**
	 * Creates the completion service.
	 */
	private void createCompletionService() {

		completionService = new ExecutorCompletionService<>(executorService);

	}

	/**
	 * Submit task.
	 *
	 * @param task the task
	 * @return the future
	 * @throws Exception the exception
	 */
	public Future<TaskResult> submitTask(Callable<TaskResult> task) throws Exception {

		return completionService.submit(task);
	}

	/**
	 * Retrieve task result.
	 *
	 * @param timeout the timeout
	 * @return the task result
	 * @throws Exception the exception
	 */
	public TaskResult retrieveTaskResult(long timeout) throws Exception {

		Future<TaskResult> futureTaskResult = completionService.poll(timeout, TimeUnit.MILLISECONDS);

		TaskResult taskResult = null;
		if (futureTaskResult != null) {
			taskResult = futureTaskResult.get();
		}

		return taskResult;
	}

	/**
	 * Retrieve task result.
	 *
	 * @return the task result
	 * @throws Exception the exception
	 */
	public TaskResult retrieveTaskResult() throws Exception {

		Future<TaskResult> futureTaskResult = completionService.take();

		TaskResult taskResult = null;
		if (futureTaskResult != null) {
			taskResult = futureTaskResult.get();
		}

		return taskResult;
	}

	/**
	 * Shutdown task executor.
	 */
	public void shutdownTaskExecutor() {

		executorService.shutdownNow();
	}
}
