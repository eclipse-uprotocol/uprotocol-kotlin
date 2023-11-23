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
 */
package org.eclipse.uprotocol.transport.datamodel

import nl.jqno.equalsverifier.EqualsVerifier
import nl.jqno.equalsverifier.Warning
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*


internal class UStatusTest {
    @Test
    @DisplayName("Make sure the equals and hash code works")
    fun testHashCodeEquals() {
        EqualsVerifier.forClass(UStatus::class.java)
            .suppress(Warning.INHERITED_DIRECTLY_FROM_OBJECT)
            .usingGetClass().verify()
    }

    @Test
    @DisplayName("Make sure the equals and hash code for ok scenarios")
    fun testHashCodeEquals_ok_scenarios() {
        // Sets call equals and hash code - using them to test functionality
        val statuses: MutableSet<Any> = HashSet()
        statuses.add(UStatus.ok())
        statuses.add(UStatus.ok())
        statuses.add(UStatus.ok("ackId"))
        statuses.add("5")
        assertEquals(3, statuses.size)


    }

    @Test
    @DisplayName("Make sure the equals and hash code for fail scenarios")
    fun testHashCodeEquals_fail_scenarios() {
        // Sets call equals and hash code - using them to test functionality
        val statuses: MutableSet<Any> = HashSet()
        // these two are the same
        statuses.add(UStatus.failed())
        statuses.add(UStatus.failed())

        // these two are the same
        statuses.add(UStatus.failed("boom"))
        statuses.add(UStatus.failed("boom", UStatus.Code.UNKNOWN))
        statuses.add(UStatus.failed("bam"))
        statuses.add(UStatus.failed("boom", UStatus.Code.UNSPECIFIED.value()))
        statuses.add(UStatus.failed("boom", UStatus.Code.INVALID_ARGUMENT))
        statuses.add("5")
        assertEquals(6, statuses.size)
    }

    @Test
    @DisplayName("Make sure the equals barfs when we compare uStatus that are not the same")
    fun testHashCodeEquals_barfs_when_not_same() {
        val ok: UStatus = UStatus.ok()
        val failed: UStatus = UStatus.failed()
        assertFalse(failed == ok)
        assertFalse(ok == failed)
    }

    @Test
    @DisplayName("Test equals when there are differences in message and code")
    fun testHashCodeEquals_different_message_and_code() {
        val failed: UStatus = UStatus.failed("boom", UStatus.Code.UNKNOWN)
        val failed1: UStatus = UStatus.failed("boom", UStatus.Code.INVALID_ARGUMENT)
        val failed2: UStatus = UStatus.failed("bang", UStatus.Code.UNKNOWN)
        val failed3: UStatus = UStatus.failed("bang", UStatus.Code.INVALID_ARGUMENT)
        assertFalse(failed == failed1)
        assertFalse(failed == failed2)
        assertFalse(failed == failed3)
        assertFalse(failed1 == failed)
        assertFalse(failed1 == failed2)
        assertFalse(failed1 == failed3)
        assertFalse(failed2 == failed)
        assertFalse(failed2 == failed1)
        assertFalse(failed2 == failed3)
        assertFalse(failed3 == failed)
        assertFalse(failed3 == failed2)
        assertFalse(failed3 == failed1)
    }

    @Test
    @DisplayName("Make sure the equals is successful when comparing the same failed status")
    fun testHashCodeEquals_same_failed_status() {
        val failed: UStatus = UStatus.failed("boom", UStatus.Code.UNKNOWN)
        val failed1: UStatus = UStatus.failed("boom", UStatus.Code.UNKNOWN)
        assertTrue(failed == failed1)
    }

    @Test
    @DisplayName("Make sure the equals is successful when comparing the same ok status")
    fun testHashCodeEquals_same_ok_status() {
        val ok: UStatus = UStatus.ok()
        val ok1: UStatus = UStatus.ok()
        assertTrue(ok == ok1)
    }

    @Test
    @DisplayName("Make sure the equals passing null reports not equals")
    fun testHashCodeEquals_null() {
        val ok: UStatus = UStatus.ok()
        val failed: UStatus = UStatus.failed()
        assertFalse(ok.equals(null))
        assertFalse(failed.equals(null))
    }

    @Test
    @DisplayName("Make sure the toString works on ok status")
    fun testToString_for_ok_status() {
        val ok: UStatus = UStatus.ok()
        assertEquals("UStatus ok id=ok code=0", ok.toString())
    }

    @Test
    @DisplayName("Make sure the toString works on ok status with Id")
    fun testToString_for_ok_status_with_id() {
        val ok: UStatus = UStatus.ok("boo")
        assertEquals("UStatus ok id=boo code=0", ok.toString())
    }

    @Test
    @DisplayName("Make sure the toString works on failed status")
    fun testToString_for_failed_status() {
        val failed: UStatus = UStatus.failed()
        assertEquals("UStatus failed msg=failed code=2", failed.toString())
    }

    @Test
    @DisplayName("Make sure the toString works on failed status with message")
    fun testToString_for_failed_status_with_getMessage() {
        val failed: UStatus = UStatus.failed("boom")
        assertEquals("UStatus failed msg=boom code=2", failed.toString())
    }

    @Test
    @DisplayName("Make sure the toString works on failed status with message and failure reason")
    fun testToString_for_failed_status_with_message_and_failure_reason() {
        val failed: UStatus = UStatus.failed("boom", UStatus.Code.INVALID_ARGUMENT.value())
        assertEquals("UStatus failed msg=boom code=3", failed.toString())
    }

    @Test
    @DisplayName("Make sure the toString works on failed status with message and Code")
    fun testToString_for_failed_status_with_message_and_code() {
        val failed: UStatus = UStatus.failed("boom", UStatus.Code.INVALID_ARGUMENT)
        assertEquals("UStatus failed msg=boom code=3", failed.toString())
    }

    @Test
    @DisplayName("Create ok status")
    fun create_ok_status() {
        val ok: UStatus = UStatus.ok()
        assertTrue(ok.isSuccess())
        assertFalse(ok.isFailed)
        assertEquals("ok", ok.msg())
        assertEquals(0, ok.getCode())
    }

    @Test
    @DisplayName("Create ok status with Id")
    fun create_ok_status_with_id() {
        val ok: UStatus = UStatus.ok("boo")
        assertTrue(ok.isSuccess())
        assertFalse(ok.isFailed)
        assertEquals("boo", ok.msg())
        assertEquals(0, ok.getCode())
    }

    @Test
    @DisplayName("Create failed status")
    fun create_failed_status() {
        val failed: UStatus = UStatus.failed()
        assertFalse(failed.isSuccess())
        assertTrue(failed.isFailed)
        assertEquals("failed", failed.msg())
        assertEquals(2, failed.getCode())
    }

    @Test
    @DisplayName("Create failed status with message")
    fun create_failed_status_with_getMessage() {
        val failed: UStatus = UStatus.failed("boom")
        assertFalse(failed.isSuccess())
        assertTrue(failed.isFailed)
        assertEquals("boom", failed.msg())
        assertEquals(2, failed.getCode())
    }

    @Test
    @DisplayName("Create failed status with message and failure reason")
    fun create_failed_status_with_message_and_failure_reason() {
        val failed: UStatus = UStatus.failed("boom", UStatus.Code.INVALID_ARGUMENT.value())
        assertFalse(failed.isSuccess())
        assertTrue(failed.isFailed)
        assertEquals("boom", failed.msg())
        assertEquals(3, failed.getCode())
    }

    @Test
    @DisplayName("Create failed status with message and Code")
    fun create_failed_status_with_message_and_code() {
        val failed: UStatus = UStatus.failed("boom", UStatus.Code.INVALID_ARGUMENT)
        assertFalse(failed.isSuccess())
        assertTrue(failed.isFailed)
        assertEquals("boom", failed.msg())
        assertEquals(3, failed.getCode())
    }

    @Test
    @DisplayName("Code from a known int code")
    fun code_from_a_known_int_code() {
        val code: Optional<UStatus.Code> = UStatus.Code.from(4)
        assertTrue(code.isPresent)
        assertEquals("DEADLINE_EXCEEDED", code.get().name)
    }

    @Test
    @DisplayName("Code from a unknown int code")
    fun code_from_a_unknown_int_code() {
        val code: Optional<UStatus.Code> = UStatus.Code.from(299)
        assertTrue(code.isEmpty)
    }

    @Test
    @DisplayName("Code from a known google code")
    fun code_from_a_known_google_code() {
        val code: Optional<UStatus.Code> = UStatus.Code.from(com.google.rpc.Code.INVALID_ARGUMENT)
        assertTrue(code.isPresent)
        assertEquals("INVALID_ARGUMENT", code.get().name)
    }

    @Test
    @DisplayName("Code from a null google code")
    fun code_from_a_null_google_code() {
        val code: Optional<UStatus.Code> = UStatus.Code.from(null)
        assertTrue(code.isEmpty)
    }

    @Test
    @DisplayName("Code from a UNRECOGNIZED google code")
    fun code_from_a_UNRECOGNIZED_google_code() {
        val code: Optional<UStatus.Code> = UStatus.Code.from(com.google.rpc.Code.UNRECOGNIZED)
        assertTrue(code.isEmpty)
    }
}