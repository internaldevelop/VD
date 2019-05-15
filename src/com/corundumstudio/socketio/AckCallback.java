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


/**
 * Base ack callback class.
 *
 * Notifies about acknowledgement received from client
 * via {@link #onSuccess} callback method.
 *
 * By default it may wait acknowledgement from client
 * while {@link SocketIOClient} is alive. Timeout can be
 * defined {@link #timeout} as constructor argument.
 *
 * This object is NOT actual anymore if {@link #onSuccess} or
 * {@link #onTimeout} was executed.
 *
 * @param <T> - any serializable type
 *
 * @see com.corundumstudio.socketio.VoidAckCallback
 * @see com.corundumstudio.socketio.MultiTypeAckCallback
 *
 */
/**
 *基础ACK回调类。
 *
 *通知有关从客户端收到确认
 *通过{@link#onSuccess}回调方法。
 *
 *默认情况下它会等待来自客户端的确认
 *{同时@link SocketIOClient}是活的。超时可
 *定义{@link#timeout}作为构造函数参数。
 *
 *此对象不实际了，如果{@link#onSuccess}或
 *{@link#onTimeout}被执行死刑。
 *
 *@param<T> - 任何序列化类型
 *
 *@see com.corundumstudio.socketio.VoidAckCallback
 *@see com.corundumstudio.socketio.MultiTypeAckCallback
 *
 */
public abstract class AckCallback<T> {

    protected final Class<T> resultClass;
    protected final int timeout;

    /**
     * Create AckCallback
     *
     * @param resultClass - result class
     */
    public AckCallback(Class<T> resultClass) {
        this(resultClass, -1);
    }

    /**
     * Creates AckCallback with timeout
     *
     * @param resultClass - result class
     * @param timeout - callback timeout in seconds
     */
    public AckCallback(Class<T> resultClass, int timeout) {
        this.resultClass = resultClass;
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    /**
     * Executes only once when acknowledgement received from client.
     *
     * @param result - object sended by client
     */
    /**
     *执行时，只有从客户收到的确认一次。
     *
     *参数的结果 - 通过客户端对象sended
     */
    public abstract void onSuccess(T result);

    /**
     * Invoked only once then <code>timeout</code> defined
     *
     */
    /**
     *仅调用一次，然后<代码>超时</ code>的定义
     *
     */
    public void onTimeout() {

    }

    /**
     * Returns class of argument in {@link #onSuccess} method
     *
     * @return - result class
     */
    public Class<T> getResultClass() {
        return resultClass;
    }

}
