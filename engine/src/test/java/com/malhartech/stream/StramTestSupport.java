/**
 * Copyright (c) 2012-2012 Malhar, Inc.
 * All rights reserved.
 */
package com.malhartech.stream;

import static org.junit.Assert.assertTrue;
import junit.framework.AssertionFailedError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.malhartech.bufferserver.Buffer;
import com.malhartech.engine.EndWindowTuple;
import com.malhartech.engine.OperatorContext;
import com.malhartech.engine.Tuple;
import com.malhartech.engine.WindowGenerator;
import com.malhartech.stram.ManualScheduledExecutorService;
import com.malhartech.stram.PhysicalPlan.PTOperator;
import com.malhartech.stram.StramLocalCluster;
import com.malhartech.stram.StramLocalCluster.LocalStramChild;

/**
 * Bunch of utilities shared between tests.
 */
abstract public class StramTestSupport
{
  private static final Logger LOG = LoggerFactory.getLogger(StramTestSupport.class);
  public static final long DEFAULT_TIMEOUT_MILLIS = 30000;

  public static Object generateTuple(Object payload, int windowId)
  {
    return payload;
  }

  public static Tuple generateBeginWindowTuple(String nodeid, int windowId)
  {
    Tuple bwt = new Tuple(Buffer.Message.MessageType.BEGIN_WINDOW);
    bwt.setWindowId(windowId);
    return bwt;
  }

  public static Tuple generateEndWindowTuple(String nodeid, int windowId)
  {
    EndWindowTuple t = new EndWindowTuple();
    t.setWindowId(windowId);
    return t;
  }

  public static void checkStringMatch(String print, String expected, String got)
  {
    assertTrue(
            print + " doesn't match, got: " + got + " expected: " + expected,
            got.matches(expected));
  }

  public static WindowGenerator setupWindowGenerator(ManualScheduledExecutorService mses)
  {
    WindowGenerator gen = new WindowGenerator(mses);
    gen.setResetWindow(0);
    gen.setFirstWindow(0);
    gen.setWindowWidth(1);
    return gen;
  }

  @SuppressWarnings("SleepWhileInLoop")
  public static void waitForWindowComplete(OperatorContext nodeCtx, long windowId) throws InterruptedException
  {
    LOG.debug("Waiting for end of window {} at node {}", windowId, nodeCtx.getId());
    while (nodeCtx.getLastProcessedWindowId() < windowId) {
      Thread.sleep(20);
    }
  }

  public interface WaitCondition {
    boolean isComplete();
  }

  public static boolean awaitCompletion(WaitCondition c, long timeoutMillis) throws InterruptedException {
    long startMillis = System.currentTimeMillis();
    while (System.currentTimeMillis() < (startMillis + timeoutMillis)) {
      if (c.isComplete()) {
        return true;
      }
      Thread.sleep(50);
    }
    return false;
  }

  /**
   * Wait until instance of operator is deployed into a container and return the container reference.
   * Asserts non null return value.
   *
   * @param localCluster
   * @param node
   * @return
   * @throws InterruptedException
   */
  @SuppressWarnings("SleepWhileInLoop")
  public static LocalStramChild waitForActivation(StramLocalCluster localCluster, PTOperator node) throws InterruptedException
  {
    LocalStramChild container = null;
    long startMillis = System.currentTimeMillis();
    while (System.currentTimeMillis() < (startMillis + DEFAULT_TIMEOUT_MILLIS)) {
      if ((container = localCluster.getContainer(node)) != null) {
         return container;
      }
      LOG.debug("Waiting for {} in container {}", node, node.getContainer());
      Thread.sleep(500);
    }
    throw new AssertionFailedError("timeout waiting for operator deployment " + node);
  }

}
