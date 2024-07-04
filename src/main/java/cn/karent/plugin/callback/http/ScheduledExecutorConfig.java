package cn.karent.plugin.callback.http;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

/**
 * @author wanshengdao
 * @date 2024/7/2
 */
@Configuration
public class ScheduledExecutorConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return new TraceableScheduledExecutorService(1);
    }

    public static class TraceableScheduledExecutorService extends ScheduledThreadPoolExecutor {

        public static final String TRACE_ID = "traceId";
        public static final String SPAN_ID = "spanId";

        public TraceableScheduledExecutorService(int corePoolSize) {
            super(corePoolSize);
        }

        private Runnable decorateCommand(Runnable command) {
            String traceId = MDC.get(TRACE_ID);
            String spanId = MDC.get(SPAN_ID);
            return () -> {
                MDC.put(TRACE_ID, traceId);
                MDC.put(SPAN_ID, spanId);
                try {
                    command.run();
                } finally {
                    MDC.remove(TRACE_ID);
                    MDC.remove(SPAN_ID);
                }
            };
        }

        @NotNull
        @Override
        public ScheduledFuture<?> schedule(@NotNull Runnable command, long delay, @NotNull TimeUnit unit) {
            command = decorateCommand(command);
            return super.schedule(command, delay, unit);
        }

    }

}
