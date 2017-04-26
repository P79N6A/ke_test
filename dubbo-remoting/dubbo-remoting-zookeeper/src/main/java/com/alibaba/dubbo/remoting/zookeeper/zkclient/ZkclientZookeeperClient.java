package com.alibaba.dubbo.remoting.zookeeper.zkclient;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.zookeeper.ChildListener;
import com.alibaba.dubbo.remoting.zookeeper.StateListener;
import com.alibaba.dubbo.remoting.zookeeper.support.AbstractZookeeperClient;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class ZkclientZookeeperClient extends AbstractZookeeperClient<IZkChildListener> {

    private final ZkClient client;

    private volatile KeeperState state = KeeperState.SyncConnected;

    private String username;

    private String password;

    public ZkclientZookeeperClient(URL url) {
        super(url);
        client = new ZkClient(url.getBackupAddress());
        client.setZkSerializer(new SerializableSerializer());
        username = url.getUsername();
        password = url.getPassword() == null ? "" : url.getPassword();
        addAuth(client);
        client.subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(KeeperState state) throws Exception {
                ZkclientZookeeperClient.this.state = state;
                if (state == KeeperState.Disconnected) {
                    stateChanged(StateListener.DISCONNECTED);
                } else if (state == KeeperState.SyncConnected) {
                    stateChanged(StateListener.CONNECTED);
                }
            }

            public void handleNewSession() throws Exception {
                stateChanged(StateListener.RECONNECTED);
            }

            @Override
            public void handleSessionEstablishmentError(Throwable throwable) throws Exception {
                logger.info(throwable);
            }
        });
    }

    private void addAuth(ZkClient client) {
        if (username != null) {
            String auth = username + ":" + password;
            try {
                client.addAuthInfo("digest", auth.getBytes("utf8"));
            } catch (UnsupportedEncodingException e) {
            }
        }
    }

    public void createPersistent(String path) {
        try {
            if (username != null) {
                List<ACL> acls = buildAcls();
                if (!client.exists(path)){
                    client.createPersistent(path, true, acls);
                    client.setAcl(path,acls);
                }
            } else {
                client.createPersistent(path, true);

            }

        } catch (ZkNodeExistsException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private List<ACL> buildAcls() throws NoSuchAlgorithmException {
        List<ACL> acls = new ArrayList<>();
        String auth = username + ":" + password;

        Id aclId = new Id("digest", DigestAuthenticationProvider.generateDigest(auth));
        ACL acl = new ACL(ZooDefs.Perms.ALL, aclId);
        acls.add(acl);
        return acls;
    }

//    public void createPersistent(String path, List<ACL> acls, boolean createParents) {
//        try {
//            client.create(path, null, CreateMode.PERSISTENT);
//        } catch (ZkNodeExistsException e) {
//            if (!createParents) {
//                throw e;
//            }
//        } catch (ZkNoNodeException e) {
//            if (!createParents) {
//                throw e;
//            }
//            String parentDir = path.substring(0, path.lastIndexOf('/'));
//            createPersistent(parentDir, createParents);
//            createPersistent(path, createParents);
//        }
//    }

    public void createEphemeral(String path) {
        try {

            if (username != null) {
                List<ACL> acls = buildAcls();
                if (!client.exists(path)){
                    client.createEphemeral(path, true, acls);
//                    client.setAcl(path,acls);
                }

            } else {
                client.createEphemeral(path);

            }
        } catch (ZkNodeExistsException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public void delete(String path) {
        try {
            addAuth(client);
            client.delete(path);
        } catch (ZkNoNodeException e) {
        }
    }

    public List<String> getChildren(String path) {
        try {
            addAuth(client);
            return client.getChildren(path);
        } catch (ZkNoNodeException e) {
            return null;
        }
    }

    public boolean isConnected() {
        return state == KeeperState.SyncConnected;
    }

    public void doClose() {
        client.close();
    }

    public IZkChildListener createTargetChildListener(String path, final ChildListener listener) {
        return new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds)
                    throws Exception {
                listener.childChanged(parentPath, currentChilds);
            }
        };
    }

    public List<String> addTargetChildListener(String path, final IZkChildListener listener) {
        addAuth(client);
        return client.subscribeChildChanges(path, listener);
    }

    public void removeTargetChildListener(String path, IZkChildListener listener) {
        addAuth(client);
        client.unsubscribeChildChanges(path, listener);
    }

}
