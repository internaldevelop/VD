/**
 * Copyright 2012 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.corundumstudio.socketio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.corundumstudio.socketio.misc.IterableCollection;
import com.corundumstudio.socketio.namespace.Namespace;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.corundumstudio.socketio.store.StoreFactory;
import com.corundumstudio.socketio.store.pubsub.DispatchMessage;
import com.corundumstudio.socketio.store.pubsub.PubSubStore;

/**
 * Fully thread-safe.
 *
 */
public class BroadcastOperations implements ClientOperations {

    private final Iterable<SocketIOClient> clients;
    private final Map<String, List<String>> namespaceRooms = new HashMap<String, List<String>>();
    private final StoreFactory storeFactory;

    public BroadcastOperations(Iterable<SocketIOClient> clients, StoreFactory storeFactory) {
        super();
        this.clients = clients;
        for (SocketIOClient socketIOClient : clients) {
            Namespace namespace = (Namespace)socketIOClient.getNamespace();
            Set<String> rooms = namespace.getRooms(socketIOClient);

            List<String> roomsList = namespaceRooms.get(namespace.getName());
            if (roomsList == null) {
                roomsList = new ArrayList<String>();
                namespaceRooms.put(namespace.getName(), roomsList);
            }
            roomsList.addAll(rooms);
        }
        this.storeFactory = storeFactory;
    }

    private void dispatch(Packet packet) {
        for (Entry<String, List<String>> entry : namespaceRooms.entrySet()) {
            for (String room : entry.getValue()) {
                storeFactory.pubSubStore().publish(PubSubStore.DISPATCH, new DispatchMessage(room, packet, entry.getKey()));
            }
        }
    }

    public Collection<SocketIOClient> getClients() {
        return new IterableCollection<SocketIOClient>(clients);
    }

    @Override
    public void send(Packet packet) {
        for (SocketIOClient client : clients) {
            client.send(packet);
        }
        dispatch(packet);
    }

    public <T> void send(Packet packet, BroadcastAckCallback<T> ackCallback) {
        for (SocketIOClient client : clients) {
            client.send(packet, ackCallback.createClientCallback(client));
        }
        ackCallback.loopFinished();
    }

    @Override
    public void disconnect() {
        for (SocketIOClient client : clients) {
            client.disconnect();
        }
    }

    @Override
    public void sendEvent(String name, Object... data) {
        Packet packet = new Packet(PacketType.MESSAGE);
        packet.setSubType(PacketType.EVENT);
        packet.setName(name);
        packet.setData(Arrays.asList(data));
        send(packet);
    }

    public <T> void sendEvent(String name, Object data, BroadcastAckCallback<T> ackCallback) {
        for (SocketIOClient client : clients) {
            client.sendEvent(name, ackCallback.createClientCallback(client), data);
        }
        ackCallback.loopFinished();
    }

}
