package org.practice.scala.domain;

import java.time.Duration;

public interface Handler {
    Duration timeout();

    void performOperation();
}
