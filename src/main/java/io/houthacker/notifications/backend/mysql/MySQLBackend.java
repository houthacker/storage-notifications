package io.houthacker.notifications.backend.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.hibernate.AbstractDAO;
import io.houthacker.notifications.backend.Backend;
import io.houthacker.notifications.notifier.Notifier;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.util.Objects.requireNonNull;

/**
 * @author houthacker
 */

public class MySQLBackend extends AbstractDAO<MySQLNotification> implements Backend {

    private final Notifier notifier;

    private final ObjectMapper mapper;

    private final String myHost;

    private final int myPort;

    private boolean bootstrapped;

    private boolean stopRequested;

    private final ReentrantReadWriteLock lock;

    public MySQLBackend(SessionFactory sessionFactory, final Notifier notifier, final String myHost, int myPort) {
        super(sessionFactory);

        requireNonNull(notifier);
        requireNonNull(myHost);

        this.notifier = notifier;
        this.mapper = new ObjectMapper();
        this.myHost = myHost;
        this.myPort = myPort;
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void bootstrap() {
        setupServerSocket();

        final Session currentSession = currentSession();
        currentSession.doWork(connection -> {
            final PreparedStatement stmt = connection.prepareStatement("SET GLOBAL async_notifications_service_endpoint_host = ?");
            stmt.setString(1, MySQLBackend.this.myHost);
            stmt.execute();
        });
        currentSession.doWork(connection -> {
            final PreparedStatement stmt = connection.prepareStatement("SET GLOBAL async_notifications_service_endpoint_port = ?");
            stmt.setInt(1, MySQLBackend.this.myPort);
            stmt.execute();
        });
        currentSession.doWork(connection -> {
            final PreparedStatement stmt = connection.prepareStatement("SET GLOBAL async_notifications_service_endpoint_registered = ?");
            stmt.setInt(1, 1);
            stmt.execute();
        });

        this.bootstrapped = true;
    }

    @Override
    public boolean isBootstrapped() {
        this.lock.readLock().lock();
        try {
            return this.bootstrapped;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public void stop() {
        this.lock.writeLock().lock();
        try {
            this.stopRequested = true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    private boolean isStopRequested() {
        this.lock.readLock().lock();
        try {
            return this.stopRequested;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private void setupServerSocket() {
        new Thread(() -> {
            ServerSocketChannel ssc = null;
            try {
                ssc = ServerSocketChannel.open();
                ssc.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                ssc.configureBlocking(false);
                ssc.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), myPort));

                while (!this.isStopRequested()) {
                    final SocketChannel channel = ssc.accept();

                    if (channel != null) {
                        while(!this.isStopRequested()) {
                            this.notifier.notifySubscribersOf(mapper.readValue(nextMessage(channel), MySQLNotification.class));
                        }
                    }

                    Thread.sleep(10L);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                // ignore
            } finally {
                if (ssc != null && ssc.isOpen()) {
                    try {
                        ssc.close();
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
        }).start();
    }

    private String nextMessage(final SocketChannel channel) throws IOException {
        final String messageSeparator = "\n\n";
        final StringBuffer buffer = new StringBuffer();

        while(buffer.lastIndexOf(messageSeparator) == -1) {
            final ByteBuffer byteBuffer = ByteBuffer.allocate(256);
            int result = channel.read(byteBuffer);
            if (result > 0) {
                byteBuffer.flip();
                CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
                buffer.append(charBuffer);
            } else if (result == -1) {
                break;
            }
        }

        return buffer.substring(0, buffer.length() - messageSeparator.length());
    }
}
