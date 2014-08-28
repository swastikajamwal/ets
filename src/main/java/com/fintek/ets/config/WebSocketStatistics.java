package com.fintek.ets.config;

import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.adapter.AbstractWebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.messaging.SubProtocolWebSocketHandler;
import org.springframework.web.socket.sockjs.transport.session.AbstractHttpSockJsSession;
import org.springframework.web.socket.sockjs.transport.session.WebSocketServerSockJsSession;

/**
 * 
 * @author sjamwal
 *
 */
public class WebSocketStatistics {
	
	private static Log logger = LogFactory.getLog(WebSocketStatistics.class);


	private final WebSocketSessionStats sessionStats;

	private final Executor inboundExecutor;

	private final Executor outboundExecutor;

	private final Executor sockJsScheduler;


	public WebSocketStatistics(SubProtocolWebSocketHandler webSocketHandler,
			ThreadPoolTaskExecutor clientInboundChannelExecutor,
			ThreadPoolTaskExecutor clientOutboundChannelExecutor,
			ThreadPoolTaskScheduler sockJsTaskScheduler) {

		this.sessionStats = new WebSocketSessionStats(webSocketHandler);
		this.inboundExecutor = clientInboundChannelExecutor.getThreadPoolExecutor();
		this.outboundExecutor = clientOutboundChannelExecutor.getThreadPoolExecutor();
		this.sockJsScheduler = sockJsTaskScheduler.getScheduledExecutor();
	}


	public String getWebSocketSessionStats() {
		return this.sessionStats.getStats();
	}

	public String getClientInboundExecutorStats() {
		return getExecutorStats(this.inboundExecutor);
	}

	public String getClientOutboundExecutorStats() {
		return getExecutorStats(this.outboundExecutor);
	}

	public String getSockJsSchedulerStats() {
		return getExecutorStats(this.sockJsScheduler);
	}

	private String getExecutorStats(Executor executor) {
		String s = executor.toString();
		s = s.substring(s.indexOf("pool"));
		return "[" + s;
	}

	public void logStats() {
		if (logger.isDebugEnabled()) {
			logger.debug("WebSocket[" + getWebSocketSessionStats() + "]" +
					", inboundChannel" + getClientInboundExecutorStats() +
					", outboundChannel" + getClientOutboundExecutorStats() +
					", sockJsScheduler" + getSockJsSchedulerStats());
		}
	}

	private static String formatByteCount(long bytes) {
		int unit = 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), "KMGTPE".charAt(exp - 1));
	}


	private static class WebSocketSessionStats {

		private final Map<String, ?> sessions;


		@SuppressWarnings("unchecked")
		private WebSocketSessionStats(SubProtocolWebSocketHandler webSocketHandler) {
			this.sessions = (Map<String, ?>) new DirectFieldAccessor(
					webSocketHandler).getPropertyValue("sessions");
		}

		public String getStats() {
			int httpSockJsSessions = 0;
			int wsSockJsSessions = 0;
			int wsSessions = 0;
			int sendBufferSize = 0;
			for (Map.Entry<String, ?> entry : this.sessions.entrySet()) {
				Object session = new DirectFieldAccessor(entry.getValue()).getPropertyValue("session");
				ConcurrentWebSocketSessionDecorator concurrentSession = (ConcurrentWebSocketSessionDecorator) session;
				sendBufferSize += concurrentSession.getBufferSize();
				session = concurrentSession.getLastSession();
				if (session instanceof AbstractHttpSockJsSession) {
					httpSockJsSessions++;
				}
				else if (session instanceof WebSocketServerSockJsSession) {
					wsSockJsSessions++;
				}
				else if (session instanceof AbstractWebSocketSession) {
					wsSessions++;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append(this.sessions.size()).append(" sessions (");
			sb.append(httpSockJsSessions).append(" SockJS-HTTP, ");
			sb.append(wsSockJsSessions).append(" SockJS-WebSocket, ");
			sb.append(wsSessions).append(" WebSocket), ");
			sb.append(formatByteCount(sendBufferSize)).append(" send buffer");
			return sb.toString();
		}

	}

}
