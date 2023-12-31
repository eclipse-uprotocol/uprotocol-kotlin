/*
 * Copyright (c) 2023 General Motors GTO LLC
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * SPDX-FileType: SOURCE
 * SPDX-FileCopyrightText: 2023 General Motors GTO LLC
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.uprotocol.transport

import org.eclipse.uprotocol.v1.UStatus;
import org.eclipse.uprotocol.v1.UAttributes
import org.eclipse.uprotocol.v1.UEntity
import org.eclipse.uprotocol.v1.UUri
import org.eclipse.uprotocol.v1.UPayload

/**
 * UTransport is the  uP-L1 interface that provides a common API for uE developers to send and receive messages.
 * UTransport implementations contain the details for connecting to the underlying transport technology and
 * sending UMessage using the configured technology. For more information please refer to
 * https://github.com/eclipse-uprotocol/uprotocol-spec/blob/main/up-l1/README.adoc.
 */
interface UTransport {
    /**
     * API used to authenticate with the underlining transport layer that the uEntity passed
     * matches the transport specific identity. MUST pass a resolved UUri.
     * @param uEntity Resolved UEntity
     * @return Returns OKSTATUS if authenticate was successful, FAILSTATUS if the calling uE
     * is not authenticated.
     */
    fun authenticate(uEntity: UEntity): UStatus

    /**
     * Transmit UPayload to the topic using the attributes defined in UTransportAttributes.
     * @param topic Resolved UUri topic to send the payload to.
     * @param payload Actual payload.
     * @param attributes Additional transport attributes.
     * @return Returns OKSTATUS if the payload has been successfully sent (ACK'ed), otherwise it
     * returns FAILSTATUS with the appropriate failure.
     */
    fun send(topic: UUri, payload: UPayload, attributes: UAttributes): UStatus

    /**
     * Register listener to be called when UPayload is received for the specific topic.
     * @param topic Resolved UUri for where the message arrived via the underlying transport technology.
     * @param listener The method to execute to process the date for the topic.
     * @return Returns OKSTATUS if the listener is unregistered correctly, otherwise it returns FAILSTATUS
     * with the appropriate failure.
     */
    fun registerListener(topic: UUri, listener: UListener): UStatus

    /**
     * Unregister a listener for a given topic. Messages arriving on this topic will no longer be processed
     * by this listener.
     * @param topic Resolved UUri for where the listener was registered to receive messages from.
     * @param listener The method to execute to process the date for the topic.
     * @return Returns OKSTATUS if the listener is unregistered correctly, otherwise it returns FAILSTATUS
     * with the appropriate failure.
     */
    fun unregisterListener(topic: UUri, listener: UListener): UStatus
}