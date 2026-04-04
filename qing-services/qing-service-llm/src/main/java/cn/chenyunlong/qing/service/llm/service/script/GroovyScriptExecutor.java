package cn.chenyunlong.qing.service.llm.service.script;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class GroovyScriptExecutor implements ScriptExecutor {

    private static final int MAX_QUEUE_SIZE = 256;
    private static final int POOL_SIZE = Math.max(2, Runtime.getRuntime().availableProcessors());

    private final ExecutorService executorService = new ThreadPoolExecutor(
            POOL_SIZE,
            POOL_SIZE,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(MAX_QUEUE_SIZE),
            new NamedDaemonThreadFactory("groovy-script"),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private volatile boolean closed = false;

    @Override
    public String getLanguage() {
        return "Groovy";
    }

    @Override
    public Object execute(String script, Map<String, Object> context) {
        if (script == null || script.trim().isEmpty()) {
            return null;
        }
        if (closed) {
            throw new IllegalStateException("Script executor is closed");
        }

        Future<Object> future = null;
        try {
            Callable<Object> task = () -> {
                Binding binding = new Binding();
                if (context != null) {
                    for (Map.Entry<String, Object> entry : context.entrySet()) {
                        binding.setVariable(entry.getKey(), entry.getValue());
                    }
                }
                GroovyShell shell = new GroovyShell(binding);
                return shell.evaluate(script);
            };
            future = executorService.submit(task);
            return future.get(getTimeout(), TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            log.error("Script execution timed out after {} ms", getTimeout());
            throw new RuntimeException("Script execution timed out", e);
        } catch (RejectedExecutionException e) {
            log.warn("Script execution rejected (pool saturated): {}", e.getMessage());
            throw new RuntimeException("Script execution rejected: too many concurrent scripts", e);
        } catch (InterruptedException e) {
            future.cancel(true);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Script execution interrupted", e);
        } catch (ExecutionException e) {
            Throwable cause = Objects.requireNonNullElse(e.getCause(), e);
            log.error("Script execution error: {}", cause.getMessage());
            throw new RuntimeException("Script execution error: " + cause.getMessage(), cause);
        } catch (Exception e) {
            log.warn("Script execution failed: {}", e.getMessage());
            throw new RuntimeException("Script execution failed: " + e.getMessage(), e);
        }
    }

    @Override
    public long getTimeout() {
        return 5000;
    }

    @Override
    public void close() {
        closed = true;
        executorService.shutdownNow();
    }

    private static final class NamedDaemonThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger sequence = new AtomicInteger(1);

        private NamedDaemonThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName(prefix + "-" + sequence.getAndIncrement());
            return thread;
        }
    }
}
